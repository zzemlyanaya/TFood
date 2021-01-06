package ru.zzemlyanaya.tfood.login.signin

data class SignInFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)