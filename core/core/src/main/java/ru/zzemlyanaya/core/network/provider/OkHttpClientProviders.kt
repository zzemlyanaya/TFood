/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.provider

import okhttp3.OkHttpClient
import ru.zzemlyanaya.core.network.interceptor.AuthInterceptor
import ru.zzemlyanaya.core.network.interceptor.CurlLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class AuthOkHttpClientProvider @Inject constructor(
    private val curlInterceptor: CurlLoggingInterceptor,
    private val authInterceptor: AuthInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(curlInterceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}

class BaseOkHttpClientProvider @Inject constructor(
    private val curlInterceptor: CurlLoggingInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(curlInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}