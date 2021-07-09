/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.main.dairy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.Result
import toothpick.ktp.KTP
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class DairyViewModel : ViewModel() {

    @Inject
    lateinit var remoteRepository : RemoteRepository
    @Inject
    lateinit var localRepository : LocalRepository
    @Inject @field:Named("token")
    lateinit var token: String


    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    private val today = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String

    fun getOrCreateRecord(day: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val res: Result<Day> = remoteRepository.getOrCreateDay(getStandardHeader(token), day)
            if (res.error != null)
                emit(Resource.error(data = null, message = res.error))
            else {
                updateLocalData(res.data!!)
                emit(Resource.success(data = res.data))
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = "Ooops, try again"))
        }
    }

    fun setRating(rating: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteRepository.setRating(
                getStandardHeader(token), today, rating
            )
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

        val kcalEaten = day.breakfastKkal + day.dinnerKkal + day.lunchKkal + day.snackKkal
        val kcalBurnt = kcalEaten - day.kkal
        val usernow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as ArrayList<Int>

        usernow[0] = kcalEaten
        usernow[1] = kcalBurnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, usernow.joinToString(";"))
    }
}