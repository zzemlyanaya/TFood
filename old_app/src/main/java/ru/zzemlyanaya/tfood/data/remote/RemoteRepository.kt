/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.model.*
import javax.inject.Inject
import javax.inject.Named


class RemoteRepository @Inject constructor(
    private val api: IServerApi,
    private val gson: Gson,
    @field:Named("headers")
    private val headers: Map<String, String>
    ) {

    // /accounts
    suspend fun auth(loginDTO: LoginDTO) = api.auth(loginDTO)
    suspend fun logout(logoutDTO: LogoutDTO) = api.logout(headers, logoutDTO)
    suspend fun refresh(refreshDTO: RefreshDTO) = api.refreshToken(refreshDTO)

    // /user
    suspend fun createUser(userCreateDTO: UserCreateDTO) = api.createUser(userCreateDTO)
    suspend fun updateUser(userUpdateDTO: UserUpdateDTO) =
        api.updateUser(headers, userUpdateDTO)

    // /day
    suspend fun getToday() = api.getToday(headers)
    suspend fun getLastWeek() = api.getLastWeek(headers)
    suspend fun rateDay(rate: Float) = api.rateDay(headers, rate)

    // /activity
    suspend fun addActivity(addActivityDTO: AddActivityDTO) =
        api.addActivity(headers, addActivityDTO)
    suspend fun addProduct(addProductDTO: AddProductDTO) =
        api.addProduct(headers, addProductDTO)
    suspend fun addSleep(sleep: Int) = api.addSleep(headers, sleep)
    suspend fun addWater(amount: Int) = api.addWater(headers, amount)

    // /find
    suspend fun searchActivity(query: String, type: ActivityType) =
        api.searchActivity(headers, query, type)
    suspend fun searchProduct(query: String) =
        api.searchProduct(headers, query)
}