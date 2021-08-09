/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.local

import javax.inject.Inject

class LocalRepository @Inject constructor(val prefs: Prefs) {

    fun getPref(key: String) = prefs.getPref(key)

    fun updatePref(key: String, value: Any) {
        prefs.setPref(key, value)
    }

}