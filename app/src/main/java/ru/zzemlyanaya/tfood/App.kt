/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 14:42
 */

package ru.zzemlyanaya.tfood

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.zzemlyanaya.tfood.data.remote.IServerService
import ru.zzemlyanaya.tfood.data.remote.IServerService.Companion.BASE_URL
import ru.zzemlyanaya.tfood.data.remote.SynchronousCallAdapterFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
            .build()

        service = retrofit.create(IServerService::class.java)

        _dataStore = applicationContext.createDataStore("tfood_prefs")

    }

    companion object {
        private lateinit var service: IServerService
        val api: IServerService
            get() = service

        private lateinit var _dataStore : DataStore<Preferences>
        val dataStore: DataStore<Preferences>
            get() = _dataStore
    }
}