/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.data.local

import javax.inject.Inject

class LocalRepository @Inject constructor(val prefs: Prefs) {

    fun getPref(key: String) = prefs.getPref(key)

    fun updatePref(key: String, value: Any) {
        prefs.setPref(key, value)
    }

}