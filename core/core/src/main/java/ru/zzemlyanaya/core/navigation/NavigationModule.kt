/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.core.navigation

import androidx.navigation.NavController
import toothpick.config.Module

class NavigationModule(navController: NavController) : Module() {
    init {
        bind(NavController::class.java).toInstance(navController)
    }
}