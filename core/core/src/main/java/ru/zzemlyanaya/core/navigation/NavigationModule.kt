/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 11:53
 */

package ru.zzemlyanaya.core.navigation

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import toothpick.config.Module

class NavigationModule(context: Context) : Module() {
    init {
        // Navigation
        bind(Router::class.java).toInstance(Router())
        bind(Cicerone::class.java).toProvider(CiceroneProvider::class.java).providesSingleton()
        bind(NavigatorHolder::class.java).toProvider(NavigatorProvider::class.java)
            .providesSingleton()
    }
}