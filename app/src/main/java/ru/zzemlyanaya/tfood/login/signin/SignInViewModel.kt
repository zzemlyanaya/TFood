/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 19:03
 */

package ru.zzemlyanaya.tfood.login.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.zzemlyanaya.tfood.R


class SignInViewModel : ViewModel() {
   // private val repository = RemoteRepository()

    private val _signInForm = MutableLiveData(SignInFormState())
    val signInFormState: LiveData<SignInFormState> = _signInForm

//    fun authorize(id: Int, password: String) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            val result: Result<User> = repository.authorize(id, password.hashCode())
//            if (result.error == null)
//                emit(Resource.success(data = result.data))
//            else
//                emit(Resource.error(data = null, message = result.error))
//        } catch (e: Exception) {
//            emit(Resource.error(data = null, message = e.message ?: "Ошибка сервера! Попробуйте снова."))
//        }
//    }


    fun loginDataChanged(email: String, password: String) {
        _signInForm.value =
                SignInFormState(emailError = validateEmail(email),
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