package ru.zzemlyanaya.tfood.login.signup

data class SignUpFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)