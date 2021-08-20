/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.core.network.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.zzemlyanaya.core.network.provider.BaseOkHttpClientProvider
import ru.zzemlyanaya.core.network.provider.RetrofitProvider
import ru.zzemlyanaya.core.network.provider.UrlProvider
import toothpick.config.Module

class NetworkModule : Module() {
    init {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .create()

        bind(UrlProvider::class.java).toInstance(UrlProvider())
        bind(Gson::class.java).toInstance(gson)

        bind(HttpLoggingInterceptor::class.java).toInstance(logger)
        bind(OkHttpClient::class.java).toProvider(BaseOkHttpClientProvider::class.java)
            .providesSingleton()
        bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).providesSingleton()
    }
}