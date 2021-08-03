/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.login.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.api.model.Error
import ru.zzemlyanaya.core.api.model.Loading
import ru.zzemlyanaya.core.api.model.State
import ru.zzemlyanaya.core.api.model.Success
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.login.LoginFormState
import ru.zzemlyanaya.tfood.model.LoginDTO
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.TokenPair
import toothpick.ktp.KTP
import javax.inject.Inject


class SignInViewModel : ViewModel() {
    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        KTP.openScope(APP_SCOPE).inject(this)
    }

    private val _signInForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _signInForm

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            val result =
                remoteRepository.auth(LoginDTO(
                    email,
                    "fixme",
                    password
                ))
            emit(Success(data = result))
        } catch (e: Exception) {
            log(e)
            emit(Error(message = e.message))
        }
    }


    fun loginDataChanged(email: String, password: String) {
        _signInForm.value =
            LoginFormState(
                emailError = validateEmail(email),
                passwordError = validatePassword(password),
                isDataValid = isAllDataValid(email, password)
            )
    }

    private fun isAllDataValid(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() and (password.length >= 8)

    private fun validateEmail(email: String) =
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else R.string.invalid_email


    private fun validatePassword(password: String) =
        if (password.length >= 8) null else R.string.invalid_password
}