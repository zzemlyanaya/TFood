/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import retrofit2.http.*

interface IServerApi {
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

    @POST("/day/add")
    fun getOrCreateDay(@HeaderMap headers: Map<String, String>, @Body data: JsonObject): JsonObject

    @POST ("/day/add/rating")
    fun setRating(@HeaderMap headers: Map<String, String>, @Body data: JsonObject): JsonPrimitive

    @GET("/day/week")
    fun getWeek(@HeaderMap headers: Map<String, String>, @Body data: JsonObject): JsonArray

    // /activity

    @POST("/activity/add/person")
    fun addUserData(@Body data: JsonObject) : JsonObject

    @POST("/activity/add/sleep")
    fun addSleepData(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject

    @POST("/activity/add/water")
    fun addWater(@HeaderMap headers: Map<String, String>, @Body data: JsonObject): JsonPrimitive

    @POST("/activity/add")
    fun addActivity(@HeaderMap headers: Map<String, String>,
                    @Body data: JsonObject) : JsonObject

    @POST("/activity/add/product")
    fun addProduct(@HeaderMap headers: Map<String, String>,
                   @Body data: JsonObject) : JsonObject

    @GET("/activity/get/products")
    fun searchProduct(@Query("search") body: String): JsonArray

    @GET("/activity/get/products/id/{id}")
    fun getProductInfo(@Path("id")id: String): JsonObject

    @GET("/activity/get/all")
    fun searchSport(@Query("search") body: String): JsonArray

    @GET("/activity/get/id/{id}")
    fun getSportInfo(@Path("id")id: String): JsonObject

    @GET("/activity/get/housework")
    fun searchHousework(@Query("search") body: String): JsonArray

    @GET("/activity/get/housework/id/{id}")
    fun getHouseworkInfo(@Path("id")id: String): JsonObject

    @POST("/activity/update/weight")
    fun updateWeight(@HeaderMap headers: Map<String, String>, @Body body: JsonObject): JsonObject

    @POST("/activity/update/height")
    fun updateHeight(@HeaderMap headers: Map<String, String>, @Body body: JsonObject): JsonObject

    // /posts

    @GET("/posts/")
    fun getAllArticles(): JsonArray

    @GET("/posts/{id}")
    fun getArticleByID(@Path("id")id:String): JsonObject
}