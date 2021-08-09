/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class RetrofitProvider @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val urlProvider: UrlProvider,
) : Provider<Retrofit> {

    override fun get(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(urlProvider.provideServerUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}