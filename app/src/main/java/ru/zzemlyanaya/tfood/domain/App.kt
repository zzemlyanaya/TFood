/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.tfood.domain

import android.app.Application
import ru.zzemlyanaya.tfood.BuildConfig
import toothpick.Toothpick
import toothpick.configuration.Configuration


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpToothpick()
    }

    private fun setUpToothpick() {
        val configuration = if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }

        Toothpick.setConfiguration(configuration)
    }
}