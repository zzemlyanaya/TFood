/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 20:02
 */

package ru.zzemlyanaya.features.auth.data.repository

import ru.zzemlyanaya.core.extentions.log
import ru.zzemlyanaya.features.auth.data.api.AuthAPI
import ru.zzemlyanaya.features.auth.data.model.LoginDTO
import ru.zzemlyanaya.features.auth.data.model.LogoutDTO
import ru.zzemlyanaya.features.auth.data.model.RefreshDTO
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private var api: AuthAPI
){
    suspend fun auth(loginDTO: LoginDTO) = api.auth(loginDTO)

    suspend fun logout(logoutDTO: LogoutDTO) = api.logout(logoutDTO)

    suspend fun refresh(refreshDTO: RefreshDTO) = api.refreshToken(refreshDTO)
}