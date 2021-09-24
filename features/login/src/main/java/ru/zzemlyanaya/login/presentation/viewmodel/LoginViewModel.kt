/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.login.presentation.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.login.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.zzemlyanaya.core.di.Scopes.ACTIVITY_MAIN_SCOPE
import ru.zzemlyanaya.core.di.Scopes.AUTH_FLOW_SCOPE
import ru.zzemlyanaya.core.di.Scopes.SESSION_SCOPE
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.PrefsConst.FINGERPRINT
import ru.zzemlyanaya.core.local.PrefsConst.USER_CREDENTIALS
import ru.zzemlyanaya.core.local.ResourceProvider
import ru.zzemlyanaya.core.network.model.*
import ru.zzemlyanaya.core.network.module.SessionModule
import ru.zzemlyanaya.login.data.model.LoginDTO
import ru.zzemlyanaya.login.data.repository.AuthRepository
import ru.zzemlyanaya.login.presentation.model.LoginFormState
import toothpick.ktp.KTP
import javax.inject.Inject

class LoginViewModel() : ViewModel() {
    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var resources: ResourceProvider

    init {
        KTP.openScopes(AUTH_FLOW_SCOPE).inject(this)
    }

    private val _signInForm = MutableLiveData(LoginFormState())
    val loginFormState: LiveData<LoginFormState> = _signInForm

    private val _authState = MutableLiveData<Resource<*>>()
    val authState: LiveData<Resource<*>> = _authState

    fun checkIfAutoLogin() {
        val credentials = localRepository.getPref(USER_CREDENTIALS) as String
        if (credentials != ";") {
            val email = credentials.split(";")[0]
            val password = credentials.split(";")[1]
            login(email, password)
        } else {
            navController.navigate(R.id.action_loginFlowFragment_to_loginFragment)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _authState.postValue(Resource.loading(null))
        try {
            val fingerprint = localRepository.getPref(FINGERPRINT) as String
            val result = authRepository.auth(
                LoginDTO(
                    email,
                    fingerprint,
                    password.hashCode().toString()
                )
            )
            _authState.postValue(Resource.success(fingerprint))
            handleTokens(result, fingerprint)
        } catch (e: HttpException) {
            log(e)
            _authState.postValue(Resource.error(null, resources.getErrorText(e.code())))
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
        KTP.closeScope(AUTH_FLOW_SCOPE)
        KTP.openScopes(ACTIVITY_MAIN_SCOPE, SESSION_SCOPE)
            .installModules(SessionModule(tokens.accessToken, refreshDTO))
        navigateNext()
    }

    private fun navigateNext() {
        val uri = Uri.parse("myApp://dashboardFragment")
        navController.popBackStack()
        navController.navigate(
            uri,
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        )
    }

    fun navigateSignUp(){
        navController.navigate(R.id.signUpFragment)
    }

    fun navigatePassReset() {
//        navController.navigate(R.id.passResetFragment)
    }

    private fun isAllDataValid(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() and (password.length >= 8)

    private fun validateEmail(email: String) =
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else R.string.invalid_email


    private fun validatePassword(password: String) =
        if (password.length >= 8) null else R.string.invalid_password
}