/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 25.01.2021, 15:51
 */

package ru.zzemlyanaya.tfood.main.dairy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result

class DairyViewModel : ViewModel() {
    val remoteRepository = RemoteRepository()

    fun getOrCreateRecord(token: String, today: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val res: Result<Day> = remoteRepository.getOrCreateDay(getStandardHeader(token), today)
            if (res.error != null)
                emit(Resource.error(data = null, message = res.error))
            else
                emit(Resource.success(data = res.data))
        }
        catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = "Ooops, try again"))
        }
    }
}