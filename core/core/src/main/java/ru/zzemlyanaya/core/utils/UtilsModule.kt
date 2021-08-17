/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 17.08.2021, 7:29
 */

package ru.zzemlyanaya.core.utils

import android.content.Context
import toothpick.config.Module

class UtilsModule(context: Context) : Module() {
    init {
        val keyboardUtils = KeyboardUtils(context)
        bind(KeyboardUtils::class.java).toInstance(keyboardUtils)
    }
}