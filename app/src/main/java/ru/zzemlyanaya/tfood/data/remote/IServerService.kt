/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 23:41
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import ru.zzemlyanaya.tfood.model.Result

interface IServerService {
    companion object {
        const val BASE_URL = "https://tfood-backend.herokuapp.com/"
    }

    @POST("/accounts/create")
    fun createAccount(@Body data: JsonObject): JsonObject

    @POST("/accounts/login")
    fun login(@Body data: JsonObject): JsonObject

    @DELETE("/accounts/logout")
    fun logout(): Result<String>

    @POST("/accounts/adddata")
    fun addUserData(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject
}