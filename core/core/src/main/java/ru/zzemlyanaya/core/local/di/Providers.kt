/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:28
 */

package ru.zzemlyanaya.core.local.di

import android.content.Context
import com.kryptoprefs.preferences.KryptoBuilder
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.Prefs
import ru.zzemlyanaya.core.local.PrefsConst
import javax.inject.Inject
import javax.inject.Provider


class PrefsProvider @Inject constructor(private val context: Context) : Provider<Prefs> {
    override fun get(): Prefs =
        Prefs(
            KryptoBuilder.hybrid(
                context,
                PrefsConst.PREFS_NAME
            )
        )
}

class LocalRepositoryProvider @Inject constructor(private val prefs: Prefs) :
    Provider<LocalRepository> {
    override fun get(): LocalRepository = LocalRepository(prefs)
}