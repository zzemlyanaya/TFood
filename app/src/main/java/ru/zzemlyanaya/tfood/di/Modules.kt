/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 14:02
 */

package ru.zzemlyanaya.tfood.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        // Navigation
        bind(Router::class.java).toInstance(Router())
        bind(Cicerone::class.java).toProvider(CiceroneProvider::class.java).providesSingleton()
        bind(NavigatorHolder::class.java).toProvider(NavigatorProvider::class.java)
            .providesSingleton()
    }
}