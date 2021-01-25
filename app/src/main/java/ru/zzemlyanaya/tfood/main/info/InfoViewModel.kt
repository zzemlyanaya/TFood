/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 0:45
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
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Resource
import java.util.*

class InfoViewModel : ViewModel() {
    private val remoteRepository = RemoteRepository()
    private val localRepository = LocalRepository.getInstance()

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
                res.data?.let { updateLocalData(it) }
    }

    fun addFood(token: String, id: String, eating: String, date: String, weight: Float) =
        CoroutineScope(Dispatchers.IO).launch {
            val res = remoteRepository.addEatenProduct(getStandardHeader(token), id, eating, date, weight)
            if (res.error != null)
                Log.d(DEBUG_TAG, res.error)
            else
                res.data?.let { updateLocalData(it) }
        }

    private fun updateLocalData(day: Day) {
        val macronow: ArrayList<Float> = localRepository.getPref(PrefsConst.FIELD_MACRO_NOW).toString()
            .split(';')
            .map { item -> item.toFloat() } as ArrayList<Float>
        macronow[0] = day.kkal.toFloat()
        macronow[1] = day.prots
        macronow[2] = day.fats
        macronow[3] = day.carbs
        macronow[4] = day.water.toFloat()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NOW, macronow.joinToString(";"))

        val kcal_eaten = day.breakfastKkal + day.dinnerKkal + day.lunchKkal + day.snackKkal
        val kcal_burnt = kcal_eaten - day.kkal
        val usernow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as ArrayList<Int>

        usernow[0] = kcal_eaten
        usernow[1] = kcal_burnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, usernow.joinToString(";"))
    }
}