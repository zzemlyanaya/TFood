/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.SleepQuizResult
import toothpick.ktp.KTP
import javax.inject.Inject

class SleepQuizViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        KTP.openScopes(APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    fun sendSleep(token: String, sleep: Double) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result: Result<SleepQuizResult> = remoteRepository.addSleepData(
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