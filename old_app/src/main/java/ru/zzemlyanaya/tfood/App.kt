/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:16
 */

package ru.zzemlyanaya.tfood

import android.app.Application
import ru.zzemlyanaya.tfood.di.AppModule
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import toothpick.ktp.KTP


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KTP.openScope(APP_SCOPE).installModules(AppModule(this))
    }
}