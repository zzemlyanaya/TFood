/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.network.module

import okhttp3.OkHttpClient
import ru.zzemlyanaya.core.network.interceptor.AuthInterceptor
import ru.zzemlyanaya.core.network.model.RefreshDTO
import ru.zzemlyanaya.core.network.provider.AuthInterceptorProvider
import ru.zzemlyanaya.core.network.provider.AuthOkHttpClientProvider
import ru.zzemlyanaya.core.network.provider.TokenRepositoryProvider
import ru.zzemlyanaya.core.network.repository.TokenRepository
import toothpick.config.Module

class SessionModule(
    currentToken: String,
    refreshDTO: RefreshDTO
) : Module() {
    init {
        bind(RefreshDTO::class.java).toInstance(refreshDTO)
        bind(String::class.java).withName("currentToken").toInstance(currentToken)

        bind(TokenRepository::class.java).toProvider(TokenRepositoryProvider::class.java)
            .providesSingleton()
        bind(AuthInterceptor::class.java).toProvider(AuthInterceptorProvider::class.java)
            .providesSingleton()

        bind(OkHttpClient::class.java).toProvider(AuthOkHttpClientProvider::class.java)
            .providesSingleton()
    }
}