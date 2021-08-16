/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 20:01
 */

package ru.zzemlyanaya.core.di

import android.content.Context
import ru.zzemlyanaya.core.utils.KeyboardUtils
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        bind(KeyboardUtils::class.java).toInstance(KeyboardUtils(context))
    }
}