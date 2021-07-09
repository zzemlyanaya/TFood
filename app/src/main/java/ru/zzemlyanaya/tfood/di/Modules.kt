/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.Prefs
import ru.zzemlyanaya.tfood.data.remote.IServerApi
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        // Remote data source
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .setLenient()
            .create()
        bind(IServerApi::class.java).toProviderInstance(ServerApiProvider(gson)).providesSingleton()
        bind(RemoteRepository::class.java).toProvider(RemoteRepositoryProvider::class.java).providesSingleton()
        bind(Gson::class.java).toInstance(gson)

        // Local data source
        bind(Prefs::class.java).toProviderInstance(PrefsProvider(context)).providesSingleton()
        bind(LocalRepository::class.java).toProvider(LocalRepositoryProvider::class.java).providesSingleton()

        // Navigation
        bind(Router::class.java).toInstance(Router())
        bind(Cicerone::class.java).toProvider(CiceroneProvider::class.java).providesSingleton()
        bind(NavigatorHolder::class.java).toProvider(NavigatorProvider::class.java).providesSingleton()
    }
}

class SessionModule(token: String, id: String) : Module() {
    init {
        bind(java.lang.String::class.java).withName("token").toInstance(token as java.lang.String)
        bind(java.lang.String::class.java).withName("userID").toInstance(id as java.lang.String)
    }
}