/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 14:13
 */

package ru.zzemlyanaya.tfood

import android.app.Application
import ru.zzemlyanaya.tfood.di.AppModule
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.ktp.KTP


class App : Application() {
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
        KTP.openScope(APP_SCOPE).installModules(AppModule(this))
    }
}