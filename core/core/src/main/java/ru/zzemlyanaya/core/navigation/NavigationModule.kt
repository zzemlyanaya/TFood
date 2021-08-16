/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.core.navigation

import toothpick.config.Module

class NavigationModule : Module() {
    init {
        bind(NavManager::class.java).toInstance(NavManager())
    }
}