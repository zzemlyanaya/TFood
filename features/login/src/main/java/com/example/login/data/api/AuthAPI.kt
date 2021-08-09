/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package com.example.login.data.api

import com.example.login.data.model.LoginDTO
import com.example.login.data.model.LogoutDTO
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zzemlyanaya.core.network.model.TokenPair

interface AuthAPI {
    @POST("/auth")
    suspend fun auth(@Body loginDTO: LoginDTO): TokenPair

    @POST("/auth/logout")
    suspend fun logout(@Body logoutDTO: LogoutDTO): String

}