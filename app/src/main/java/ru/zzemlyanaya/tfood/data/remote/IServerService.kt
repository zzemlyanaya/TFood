/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 21.01.2021, 19:46
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.*

interface IServerService {
    companion object {
        const val BASE_URL = "https://tfood-nest.herokuapp.com/"
    }

    // /accounts
    @POST("/accounts/create")
    fun createAccount(@Body data: JsonObject): JsonObject

    @POST("/accounts/login")
    fun login(@Body data: JsonObject): JsonObject

    @DELETE("/accounts/logout")
    fun logout(@HeaderMap headers: Map<String, String>): JsonObject

    // /day

    @POST("/day/create")
    fun createDay(@HeaderMap headers: Map<String, String>, @Body date: String): JsonObject

    @GET("/day/week")
    fun getWeek(@HeaderMap headers: Map<String, String>, @Body firstDateOfWeek: String): JsonArray

    // /activity

    @POST("/activity/add/person")
    fun addUserData(@Body data: JsonObject) : JsonObject

    @POST("/activity/add/sleep")
    fun addSleepData(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject

    @POST("/activity/add/activity")
    fun addActivity(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject

    @POST("/activity/add/product")
    fun addProduct(@HeaderMap headers: Map<String, String>,
                   @Body data: JsonObject) : JsonObject
}