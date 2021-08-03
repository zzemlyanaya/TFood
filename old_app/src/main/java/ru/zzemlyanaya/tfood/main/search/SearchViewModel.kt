/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.CongratsTypes
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.getStandardHeader
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Resource
import ru.zzemlyanaya.tfood.model.ShortView
import toothpick.ktp.KTP
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class SearchViewModel : ViewModel() {
    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    @field:Named("token")
    lateinit var token: String

    init {
        KTP.openScopes(Scopes.APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
    }

    private val searchResults = MutableLiveData<List<ShortView>>(emptyList())
    val date = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String

    val congratsLiveData = MutableLiveData(CongratsTypes.NONE)

    fun search(search: String, whatToSearch: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val result = when (whatToSearch) {
                "product" -> remoteRepository.searchProduct(search)
//                    Result(
//                            data = listOf(ShortView("0", "Яблоко"), ShortView("1", "Морковь")),
//                            error = null)
                "sport" -> remoteRepository.searchSport(search)
//                    Result(
//                            data = listOf(ShortView("0", "Бег в медленном темпе"), ShortView("1", "Скандинавская ходьба")),
//                            error = null)
                else -> remoteRepository.searchHousework(search)
//                    Result(
//                            data = listOf(ShortView("0", "Мытьё полов"), ShortView("1", "Мытьё посуды")),
//                            error = null)
            }
            if (result.error == null) {
                searchResults.postValue(result.data!!)
                emit(Resource.success(data = result.data))
            } else
                emit(Resource.error(data = null, message = result.error))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(data = null, message = "Ooops, try again."))
        }
    }

    fun getData() = searchResults.value

    fun addActivity(type: String, length: Float, id: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val res =
                remoteRepository.addActivityData(getStandardHeader(token), date, id, length, type)
            if (res.error != null)
                Log.d(DEBUG_TAG, res.error)
            else
                res.data?.let { updateLocalData(it) }
        }

    fun addFood(id: String, eating: String, weight: Float) =
        CoroutineScope(Dispatchers.IO).launch {
            val res =
                remoteRepository.addEatenProduct(getStandardHeader(token), id, eating, date, weight)
            if (res.error != null)
                Log.d(DEBUG_TAG, res.error)
            else
                res.data?.let { updateLocalData(it) }
        }

    private fun updateLocalData(day: Day) {
        val macronow: ArrayList<Float> =
            localRepository.getPref(PrefsConst.FIELD_MACRO_NOW).toString()
                .split(';')
                .map { item -> item.toFloat() } as ArrayList<Float>
        val macronorm: ArrayList<Float> =
            localRepository.getPref(PrefsConst.FIELD_MACRO_NORM).toString()
                .split(';')
                .map { item -> item.toFloat() } as ArrayList<Float>

        if (macronow[1] < macronorm[1] && day.prots >= macronorm[1]) {
            congratsLiveData.postValue(CongratsTypes.PROTS)
            if (macronow[2] < macronorm[2] && day.fats >= macronorm[2]) {
                congratsLiveData.postValue(CongratsTypes.FP)
                if (macronow[3] < macronorm[3] && day.carbs >= macronorm[3])
                    congratsLiveData.postValue(CongratsTypes.DIET_ALL)
            } else if (macronow[3] < macronorm[3] && day.carbs >= macronorm[3])
                congratsLiveData.postValue(CongratsTypes.CP)
        } else if (macronow[2] < macronorm[2] && day.fats >= macronorm[2]) {
            congratsLiveData.postValue(CongratsTypes.FATS)
            if (macronow[3] < macronorm[3] && day.carbs >= macronorm[3])
                congratsLiveData.postValue(CongratsTypes.CF)
        } else if (macronow[3] < macronorm[3] && day.carbs >= macronorm[3])
            congratsLiveData.postValue(CongratsTypes.CARBS)


        macronow[0] = day.kcal.toFloat()
        macronow[1] = day.prots
        macronow[2] = day.fats
        macronow[3] = day.carbs
        macronow[4] = day.water.toFloat()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NOW, macronow.joinToString(";"))


        val kcalEaten = day.breakfastKcal + day.dinnerKcal + day.lunchKcal + day.snackKcal
        val kcalBurnt = kcalEaten - day.kcal
        val usernow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as ArrayList<Int>

        usernow[0] = kcalEaten
        usernow[1] = kcalBurnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, usernow.joinToString(";"))
    }
}