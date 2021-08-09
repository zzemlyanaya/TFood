/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.core.network.model.Loading
import ru.zzemlyanaya.core.network.model.Success
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.model.UserUpdateDTO
import toothpick.ktp.KTP
import javax.inject.Inject

class BasicQuizViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var localRepository: LocalRepository

    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    private val user = UserUpdateDTO()

    fun sendData() = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            val result = remoteRepository.updateUser(user)
            emit(Success(data = result))
        } catch (e: Exception) {
            log(e)
            emit(Error(message = e.message))
        }
    }

    fun update(key: String, value: Any) {
        when (key) {
            "username" -> user.username = value as String
            "birthday" -> user.birthdate = value as String
            "height" -> user.height = value as Int
            "weight" -> user.weight = value as Int
            "chest" -> user.chest = value as Int
            "gender" -> user.gender = value as Boolean
        }
    }

    fun isDataValid() = !(user.username == null || user.birthdate == null || user.gender == null ||
            user.height == null || user.weight == null || user.chest == null)

    fun getGender() = user.gender == true
}