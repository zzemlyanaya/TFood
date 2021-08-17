/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.login.data.repository

import ru.zzemlyanaya.login.data.api.AuthAPI
import ru.zzemlyanaya.login.data.model.LoginDTO
import ru.zzemlyanaya.login.data.model.LogoutDTO
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthAPI
) {
    suspend fun auth(loginDTO: LoginDTO) = api.auth(loginDTO)

    suspend fun logout(logoutDTO: LogoutDTO) = api.logout(logoutDTO)
}