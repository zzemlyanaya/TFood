/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.tfood.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.core.network.model.Loading
import ru.zzemlyanaya.core.network.model.Success
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.UserUpdateDTO
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

    fun getToday() = liveData(Dispatchers.IO){
        emit(Loading)
        try {
            val res = remoteRepository.getToday()
            emit(Success(data = res))
            updateLocalData(res)
        } catch (exception: Exception) {
            log(exception)
            emit(Error(message = exception.message))
        }
    }

    fun updateUserWeight(weight: Int) = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            val res = remoteRepository.updateUser(UserUpdateDTO(weight = weight))
            emit(Success(data = res))
        } catch (exception: Exception) {
            log(exception)
            emit(Error(message = exception.message))
        }
    }

    fun updateUserHeight(height: Int) = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            val res = remoteRepository.updateUser(UserUpdateDTO(height = height))
            emit(Success(data = res))
        } catch (exception: Exception) {
            log(exception)
            emit(Error(message = exception.message))
        }
    }


    private fun updateLocalData(day: Day) {
        val kcalEaten = day.kcal
        val kcalBurnt = kcalEaten - day.kcalNeed
        val userNow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as ArrayList<Int>

        userNow[0] = kcalEaten
        userNow[1] = kcalBurnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, userNow.joinToString(";"))
    }

    fun addWater(amount: Int) = liveData(Dispatchers.IO) {
        emit(Loading)
        try {
            remoteRepository.addWater(amount)
            emit(Success(data = "OK"))
        } catch (e: Exception) {
            log(e)
            emit(Error(message = e.message))
        }
    }
}