/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:08
 */

package ru.zzemlyanaya.tfood.login.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.login.LoginFormState
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result


class SignUpViewModel : ViewModel() {
    private val repository = RemoteRepository()

    private val _signUpForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _signUpForm

    fun registr(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<String> = repository.createAccount(email, password.hashCode().toString())
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = e.message ?: "505: server error"))
        }
    }


    fun loginDataChanged(email: String, password: String) {
        _signUpForm.value =
                LoginFormState(emailError = validateEmail(email),
                        passwordError = validatePassword(password),
                        isDataValid = isAllDataValid(email, password))
    }

    private fun isAllDataValid(email: String, password: String)
            = Patterns.EMAIL_ADDRESS.matcher(email).matches() and (password.length >= 8)

    private fun validateEmail(email: String) =
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else R.string.invalid_email


    private fun validatePassword(password: String) =
            if (password.length >= 8) null else R.string.invalid_password
}