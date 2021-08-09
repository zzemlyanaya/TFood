/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:28
 */

package ru.zzemlyanaya.core.local.di

import android.content.Context
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.Prefs
import toothpick.config.Module

class PrefsModule(context: Context) : Module() {
    init {
        bind(Prefs::class.java).toProviderInstance(PrefsProvider(context)).providesSingleton()
        bind(LocalRepository::class.java).toProvider(LocalRepositoryProvider::class.java)
            .providesSingleton()
    }
}