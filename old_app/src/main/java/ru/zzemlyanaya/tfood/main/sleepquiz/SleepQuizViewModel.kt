/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.core.network.model.Loading
import ru.zzemlyanaya.core.network.model.Success
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import toothpick.ktp.KTP
import javax.inject.Inject

class SleepQuizViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        KTP.openScopes(APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    fun sendSleep(sleep: Int) = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            val result = remoteRepository.addSleep(sleep)
            emit(Success(data = "OK"))
        } catch (e: Exception) {
            log(e)
            emit(Error(message = e.message))
        }
    }
}