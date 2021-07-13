/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
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
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.BasicQuizResult
import ru.zzemlyanaya.tfood.model.Day
import toothpick.ktp.KTP
import java.util.*
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var localRepository: LocalRepository

    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

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

    fun updateUserWeight(token: String, date: String, weight: Int) {
        if (token.isNotEmpty())
            CoroutineScope(Dispatchers.IO).launch {
                val res = remoteRepository.updateWeight(getStandardHeader(token), date, weight)
                if (res.error != null)
                    Log.d(DEBUG_TAG, res.error)
                else
                    res.data?.let { updateLocalData(it) }
            }
    }

    fun updateUserHeight(token: String, date: String, height: Int) {
        if (token.isNotEmpty())
            CoroutineScope(Dispatchers.IO).launch {
                val res = remoteRepository.updateWeight(getStandardHeader(token), date, height)
                if (res.error != null)
                    Log.d(DEBUG_TAG, res.error)
                else
                    res.data?.let { updateLocalData(it) }
            }
    }

    private fun updateLocalData(day: Day) {
        val macronow: ArrayList<Float> =
            localRepository.getPref(PrefsConst.FIELD_MACRO_NOW).toString()
                .split(';')
                .map { item -> item.toFloat() } as ArrayList<Float>
        macronow[0] = day.kkal.toFloat()
        macronow[1] = day.prots
        macronow[2] = day.fats
        macronow[3] = day.carbs
        macronow[4] = day.water.toFloat()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NOW, macronow.joinToString(";"))

        val kcalEaten = day.breakfastKkal + day.dinnerKkal + day.lunchKkal + day.snackKkal
        val kcalBurnt = kcalEaten - day.kkal
        val usernow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as ArrayList<Int>

        usernow[0] = kcalEaten
        usernow[1] = kcalBurnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, usernow.joinToString(";"))
    }

    private fun updateLocalData(res: BasicQuizResult) {
        val macronorm: ArrayList<Float> =
            localRepository.getPref(PrefsConst.FIELD_MACRO_NORM).toString()
                .split(';')
                .map { item -> item.toFloat() } as ArrayList<Float>
        macronorm[0] = res.energyNeed.toFloat()
        macronorm[1] = res.pfc.prots
        macronorm[2] = res.pfc.fats
        macronorm[3] = res.pfc.carbs
        macronorm[4] = res.water.toFloat()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NORM, macronorm.joinToString(";"))
    }

    fun addWater(date: String, token: String, water: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteRepository.addWater(getStandardHeader(token), date, water)
        }
    }
}