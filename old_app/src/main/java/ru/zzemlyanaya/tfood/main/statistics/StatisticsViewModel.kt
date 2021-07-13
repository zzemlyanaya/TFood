/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import toothpick.ktp.KTP

class StatisticsViewModel : ViewModel() {

    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    fun getStatistics() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
//            val result = remoteRepository.getStatistics()
            val weekNorm = listOf(1800, 1800, 1800, 1800, 1800, 1800, 1800)
            val weekActual = listOf(1900, 1860, 1980, 1780, 1810, 1800, 2000)
            val result = Result(error = null, data = listOf(weekNorm, weekActual))
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