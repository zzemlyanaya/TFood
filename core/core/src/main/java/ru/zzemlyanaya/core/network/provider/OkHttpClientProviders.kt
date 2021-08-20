/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.core.network.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.zzemlyanaya.core.network.interceptor.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class AuthOkHttpClientProvider @Inject constructor(
    private val interceptor: HttpLoggingInterceptor,
    private val authInterceptor: AuthInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}

class BaseOkHttpClientProvider @Inject constructor(
    private val interceptor: HttpLoggingInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}