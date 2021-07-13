/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 15:18
 */

package ru.zzemlyanaya.core.api.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.zzemlyanaya.core.system.network.provider.UrlProvider
import javax.inject.Inject
import javax.inject.Provider

class RetrofitProvider @Inject constructor(
    private val client: OkHttpClient,
    private val factory: CallAdapter.Factory,
    private val gson: Gson,
    private val urlProvider: UrlProvider,
) : Provider<Retrofit> {

    override fun get(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(urlProvider.provideServerUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(factory)
            .build()
    }
}