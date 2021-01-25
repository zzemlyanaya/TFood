/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 25.01.2021, 15:51
 */

package ru.zzemlyanaya.tfood.main.info

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Resource

class InfoViewModel : ViewModel() {
    private val remoteRepository = RemoteRepository()

    val day = MutableLiveData(Day())

    fun getSth(id: String, whatToSearch: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = when(whatToSearch) {
                "product" -> remoteRepository.getProductInfo(id)
                "sport" -> remoteRepository.getSportInfo(id)
                else -> remoteRepository.getHouseworkInfo(id)
            }
            if (result.error == null) {
                emit(Resource.success(data = result.data))
            }
            else
                emit(Resource.error(data = null, message = result.error))
        }
        catch (e: Exception){
            e.printStackTrace()
            emit(Resource.error(data = null, message = "Ooops, try again."))
        }
    }

    fun addActivity(token: String, date: String, type: String, length: Float, id: String) =
            CoroutineScope(Dispatchers.IO).launch {
                val res = remoteRepository.addActivityData(getStandardHeader(token), date, id, length, type)
                if (res.error != null)
                    Log.d(DEBUG_TAG, res.error)
                else
                    day.postValue(res.data)
    }

    fun addFood(token: String, id: String, eating: String, date: String, weight: Float) =
            CoroutineScope(Dispatchers.IO).launch {
                val res = remoteRepository.addEatenProduct(getStandardHeader(token), id, eating, date, weight)
                if (res.error != null)
                    Log.d(DEBUG_TAG, res.error)
                else
                    day.postValue(res.data)
            }
}