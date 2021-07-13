/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 11:15
 */

package ru.zzemlyanaya.tfood

import android.app.Application


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        KTP.openScope(APP_SCOPE).installModules(AppModule(this))
    }
}