/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 15:40
 */

package ru.zzemlyanaya.tfood.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import ru.zzemlyanaya.tfood.model.CreateUserResponse
import ru.zzemlyanaya.tfood.model.Result

interface IServerService {
    companion object {
        const val BASE_URL = "https://tfood-backend.herokuapp.com/"
    }

    @POST("/accounts/create")
    fun createAccount(@Body email: String, @Body password: String): Result<CreateUserResponse>

    @POST("/accounts/login")
    fun login(@Body email: String, @Body password: String): Result<String>

    @DELETE("/accounts/logout")
    fun logout(): Result<String>

    @POST("/accounts/adddata")
    fun addUserData()
}