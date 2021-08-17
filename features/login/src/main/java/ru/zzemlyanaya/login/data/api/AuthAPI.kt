/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.login.data.api

import ru.zzemlyanaya.login.data.model.LoginDTO
import ru.zzemlyanaya.login.data.model.LogoutDTO
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zzemlyanaya.core.network.model.TokenPair

interface AuthAPI {
    @POST("/auth")
    suspend fun auth(@Body loginDTO: LoginDTO): TokenPair

    @POST("/auth/logout")
    suspend fun logout(@Body logoutDTO: LogoutDTO): String

}