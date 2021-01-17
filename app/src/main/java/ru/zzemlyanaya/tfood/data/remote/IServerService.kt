/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 17.01.2021, 20:20
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface IServerService {
    companion object {
        const val BASE_URL = "https://tfood-backend.herokuapp.com/"
    }

    @POST("/accounts/create")
    fun createAccount(@Body data: JsonObject): JsonObject

    @POST("/accounts/login")
    fun login(@Body data: JsonObject): JsonObject

    @DELETE("/accounts/logout")
    fun logout(@HeaderMap headers: Map<String, String>): JsonObject

    @POST("/accounts/add/data")
    fun addUserData(@Body data: JsonObject) : JsonObject

    @POST("/day/activity/add/sleep")
    fun addSleepData(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject
}