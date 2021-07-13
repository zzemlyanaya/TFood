/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.model.BasicQuizResult
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.User
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

    private val user = User()
    private var sleep = 0.0

    fun saveData() {
        val data = "${user.username};${user.birthdate};${user.height};${user.weight};${user.chest}"
        localRepository.updatePref(PrefsConst.FIELD_USER_DATA, data)
    }

    fun sendData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<BasicQuizResult> = remoteRepository.addUserData(user, sleep)
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = e.message ?: "505: server error"))
        }
    }

    fun update(key: String, value: Any) {
        when (key) {
            "id" -> user._id = value as String
            "username" -> user.username = value as String
            "birthday" -> user.birthdate = value as String
            "height" -> user.height = value as Int
            "weight" -> user.weight = value as Int
            "chest" -> user.chest = value as Int
            "gender" -> user.gender = value as Boolean
            "sleep" -> sleep = value as Double
        }
    }

    fun isDataValid() = !(user.username == null || user.birthdate == null || user.gender == null ||
            user.height == null || user.weight == null || user.chest == null)

    fun getGender() = user.gender == true
}