/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.provider

import ru.zzemlyanaya.core.network.interceptor.AuthInterceptor
import ru.zzemlyanaya.core.network.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptorProvider @Inject constructor(
    private val repository: TokenRepository
) : Provider<AuthInterceptor> {
    override fun get(): AuthInterceptor = AuthInterceptor(repository)
}