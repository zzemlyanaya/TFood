/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package com.example.login.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.login.LoginFlow
import com.example.login.R
import com.example.login.data.model.LoginDTO
import com.example.login.data.repository.AuthRepository
import com.example.login.di.LoginModule
import com.example.login.presentation.model.LoginFormState
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.di.Scopes.AUTH_SCOPE
import ru.zzemlyanaya.core.di.Scopes.NETWORK_SCOPE
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.PrefsConst.FINGERPRINT
import ru.zzemlyanaya.core.local.PrefsConst.USER_CREDENTIALS
import ru.zzemlyanaya.core.network.model.*
import ru.zzemlyanaya.core.network.module.NetworkModule
import ru.zzemlyanaya.core.network.module.SessionModule
import toothpick.ktp.KTP
import javax.inject.Inject

class LoginViewModel : ViewModel() {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var router: Router

    init {
        KTP.openScopes(APP_SCOPE, NETWORK_SCOPE)
            .installModules(NetworkModule(), LoginModule())
            .inject(this)
        checkIfAutoLogin()
    }

    private val _signInForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _signInForm

    private val _authState = MutableLiveData(Loading as State<*>)
    var authState: LiveData<State<*>> = _authState

    private fun checkIfAutoLogin() {
        val credentials = localRepository.getPref(USER_CREDENTIALS) as String
        if (credentials != ";") {
            val email = credentials.split(";")[0]
            val password = credentials.split(";")[1]
            authState = login(email, password)
        } else
            router.navigateTo(LoginFlow.loginFragment())
    }

    fun login(email: String, password: String) = liveData<State<*>>(Dispatchers.IO) {
        emit(Loading)
        try {
            val fingerprint = localRepository.getPref(FINGERPRINT) as String
            val result = authRepository.auth(
                LoginDTO(
                    email,
                    fingerprint,
                    password
                )
            )
            emit(Success(data = result))
            handleTokens(result, fingerprint)
        } catch (e: Exception) {
            log(e)
            emit(Error<TokenPair>(message = e.message ?: "Неизвестная ошибка"))
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

    private fun handleTokens(tokens: TokenPair, fingerprint: String) {
        val refreshDTO = RefreshDTO(fingerprint, tokens.refreshToken)
        KTP.openScopes(APP_SCOPE, NETWORK_SCOPE, AUTH_SCOPE)
            .installModules(SessionModule(tokens.accessToken, refreshDTO))
        router.backTo(null)
    }

    private fun isAllDataValid(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() and (password.length >= 8)

    private fun validateEmail(email: String) =
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else R.string.invalid_email


    private fun validatePassword(password: String) =
        if (password.length >= 8) null else R.string.invalid_password
}