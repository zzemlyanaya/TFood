/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.tfood.domain

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import ru.zzemlyanaya.core.utils.UtilsModule
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.di.Scopes.NETWORK_SCOPE
import ru.zzemlyanaya.core.local.di.PrefsModule
import ru.zzemlyanaya.core.network.module.NetworkModule
import ru.zzemlyanaya.tfood.BuildConfig
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.ktp.KTP


class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        setUpToothpick()
        setUpAppScope()
    }

    private fun setUpToothpick() {
        val configuration = if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }

        Toothpick.setConfiguration(configuration)
    }

    private fun setUpAppScope() {
        KTP.openScopes(APP_SCOPE, NETWORK_SCOPE)
            .installModules(
                PrefsModule(this),
                NetworkModule()
            )
    }

}