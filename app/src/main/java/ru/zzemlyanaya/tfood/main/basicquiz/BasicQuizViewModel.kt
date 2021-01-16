/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:24
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.model.BasicQuizResult
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.User

class BasicQuizViewModel : ViewModel() {
    private val user = User()
    private var sleep = 0
    private val repository = RemoteRepository()

    fun sendData() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<BasicQuizResult> = repository.addUserData(user, sleep.toDouble())
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
        when(key){
            "id" -> user._id = value as String
            "name" -> user.username = value as String
            "birthday" -> user.birthdate = value as String
            "height" -> user.height = value as Int
            "weight" -> user.weight = value as Int
            "chest" -> user.chest = value as Int
            "gender" -> user.gender = value as Boolean
            "sleep" -> sleep = value as Int
        }
    }

    fun isDataValid() = !( user.username == null || user.birthdate == null || user.gender == null ||
            user.height == null || user.weight == null || user.chest == null)

    fun getData() = user.toString()
}