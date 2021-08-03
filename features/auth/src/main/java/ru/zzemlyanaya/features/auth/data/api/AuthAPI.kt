/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 19:55
 */

package ru.zzemlyanaya.features.auth.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.zzemlyanaya.core.api.model.State
import ru.zzemlyanaya.features.auth.data.model.LoginDTO
import ru.zzemlyanaya.features.auth.data.model.LogoutDTO
import ru.zzemlyanaya.features.auth.data.model.RefreshDTO
import ru.zzemlyanaya.features.auth.data.model.TokenPair

interface AuthAPI {
    @POST("/auth")
    suspend fun auth(@Body loginDTO: LoginDTO): State<TokenPair>

    @POST("/auth/logout")
    suspend fun logout(@Body logoutDTO: LogoutDTO): State<String>

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body refreshDTO: RefreshDTO): State<TokenPair>
}