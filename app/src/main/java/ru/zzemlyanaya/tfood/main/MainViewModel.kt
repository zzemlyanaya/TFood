/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 26.01.2021, 13:49
 */

package ru.zzemlyanaya.tfood.main

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import java.util.*

class MainViewModel : ViewModel() {
    private val remoteRepository = RemoteRepository()
    private val localRepository = LocalRepository.getInstance()

    fun getOrCreateDay(token: String, date: String) {
        if (token.isNotEmpty())
            CoroutineScope(Dispatchers.IO).launch {
                val res = remoteRepository.getOrCreateDay(getStandardHeader(token), date)
                if (res.error != null)
                    Log.d(DEBUG_TAG, res.error)
                else
                    res.data?.let { updateLocalData(it) }
            }
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

    fun addWater(date: String, token: String, water: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteRepository.addWater(getStandardHeader(token), date, water)
        }
    }
}