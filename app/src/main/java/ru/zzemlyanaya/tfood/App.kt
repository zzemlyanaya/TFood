/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 10:36
 */

package ru.zzemlyanaya.tfood

import android.app.Application
import com.google.gson.GsonBuilder
import com.kryptoprefs.preferences.KryptoBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.zzemlyanaya.tfood.data.local.Prefs
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.data.remote.IServerService
import ru.zzemlyanaya.tfood.data.remote.IServerService.Companion.BASE_URL
import ru.zzemlyanaya.tfood.data.remote.SynchronousCallAdapterFactory
import java.util.concurrent.TimeUnit


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build()

        val gson = GsonBuilder()
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .build()

        service = retrofit.create(IServerService::class.java)

        preferences = Prefs(
                KryptoBuilder.hybrid(
                        this,
                        PrefsConst.PREFS_NAME
                )
        )

    }

    companion object {
        private lateinit var service: IServerService
        val api: IServerService
            get() = service

        private lateinit var preferences : Prefs
        val prefs: Prefs
            get() = preferences
    }
}