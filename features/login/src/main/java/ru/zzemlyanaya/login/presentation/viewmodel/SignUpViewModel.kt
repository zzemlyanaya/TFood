/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.login.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import com.example.login.R
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.di.Scopes
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.login.data.repository.AuthRepository
import ru.zzemlyanaya.login.presentation.model.LoginFormState
import toothpick.ktp.KTP
import javax.inject.Inject


class SignUpViewModel : ViewModel() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var navController: NavController

    init {
        KTP.openScopes(Scopes.AUTH_FLOW_SCOPE).inject(this)
    }

    private val _signUpForm = MutableLiveData(LoginFormState())
    val signUpFormState: LiveData<LoginFormState> = _signUpForm

    fun onBackPressed() {
        navController.popBackStack()
    }

//    fun registr(email: String, password: String) = liveData(Dispatchers.IO) {
//
//    }


    fun signUpDataChanged(email: String, password: String) {
        _signUpForm.value =
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