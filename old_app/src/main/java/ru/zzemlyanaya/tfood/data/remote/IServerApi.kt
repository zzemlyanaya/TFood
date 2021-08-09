/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 14:22
 */

package ru.zzemlyanaya.tfood.data.remote

import retrofit2.http.*
import ru.zzemlyanaya.tfood.model.*

interface IServerApi {
    companion object {
        const val BASE_URL = "https://tfood.ml/"
    }

    // /auth
    @POST("/auth")
    suspend fun auth(@Body loginDTO: LoginDTO): TokenPair

    @POST("/auth/logout")
    suspend fun logout(
        @HeaderMap headers: Map<String, String>,
        @Body logoutDTO: LogoutDTO
    ): String

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body refreshDTO: RefreshDTO): TokenPair

    // /user
    @POST("/api/v1/users")
    suspend fun createUser(@Body userCreateDTO: UserCreateDTO): User

    @POST("/api/v1/users/update")
    suspend fun updateUser(
        @HeaderMap headers: Map<String, String>,
        @Body userUpdateDTO: UserUpdateDTO
    ): UserUpdateResponse

    // /day
    @GET("/api/v1/day/rate")
    suspend fun rateDay(
        @HeaderMap headers: Map<String, String>,
        @Query("V")rate: Float
    ): String

    @GET("/api/v1/day/today")
    suspend fun getToday(@HeaderMap headers: Map<String, String>): Day

    @GET("/api/v1/day/week")
    suspend fun getLastWeek(@HeaderMap headers: Map<String, String>): List<Day>

    // /activity
    @POST("/api/v1/activity")
    suspend fun addActivity(
        @HeaderMap headers: Map<String, String>,
        @Body addActivityDTO: AddActivityDTO
    ): String

    @POST("/api/v1/activity/eating")
    suspend fun addProduct(
        @HeaderMap headers: Map<String, String>,
        @Body addProductDTO: AddProductDTO
    ): String

    @POST("/api/v1/activity/sleep")
    suspend fun addSleep(
        @HeaderMap headers: Map<String, String>,
        @Query("time") time: Int
    ): String

    @POST("/api/v1/activity/water")
    suspend fun addWater(
        @HeaderMap headers: Map<String, String>,
        @Query("amount") amount: Int
    ): String

    // /find
    @GET("/api/v1/find-activity")
    suspend fun searchActivity(
        @HeaderMap headers: Map<String, String>,
        @Query("q") query: String,
        @Query("type") type: ActivityType
    ): List<ActivityEntity>

    @GET("/api/v1/find-activity/products")
    suspend fun searchProduct(
        @HeaderMap headers: Map<String, String>,
        @Query("q") query: String
    ): ProductEntity
}