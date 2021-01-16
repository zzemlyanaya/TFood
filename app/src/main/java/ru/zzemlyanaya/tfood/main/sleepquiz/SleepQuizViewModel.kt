/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 19:48
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.SleepQuizResult

class SleepQuizViewModel : ViewModel() {
    val repository = RemoteRepository()

    fun sendSleep(token: String, sleep: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<SleepQuizResult> = repository.addSleepData(
                getStandardHeader(token),
                sleep
            )
            if (result.error == null)
                emit(Resource.success(data = result.data))
            else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = e.message ?: "505: server error"))
        }
    }
}