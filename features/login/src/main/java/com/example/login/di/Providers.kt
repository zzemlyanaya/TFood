/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 15:50
 */

package com.example.login.di

import com.example.login.data.api.AuthAPI
import com.example.login.data.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class AuthApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<AuthAPI> {
    override fun get(): AuthAPI = retrofit.create(AuthAPI::class.java)
}

class AuthRepositoryProvider @Inject constructor(
    private val api: AuthAPI
) : Provider<AuthRepository> {
    override fun get(): AuthRepository = AuthRepository(api)
}