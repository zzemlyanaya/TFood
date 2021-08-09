/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:45
 */

package ru.zzemlyanaya.core.local

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs


object PrefsConst {
    const val PREFS_NAME = "tfood_prefs"
    const val FINGERPRINT = "fingerprint"
    const val IS_FIRST_LAUNCH = "is_first_launch"
    const val LAST_SLEEP_DATE = "sleep_date"
    const val USER_CREDENTIALS = "credentials"
}

class Prefs(prefs: KryptoPrefs) : KryptoContext(prefs) {
    private val fingerprint = string(PrefsConst.FINGERPRINT, "")
    private val isFirstLaunch = boolean(PrefsConst.IS_FIRST_LAUNCH, true)
    private val lastSleepDate = string(PrefsConst.LAST_SLEEP_DATE, "")
    private val credentials = string(PrefsConst.USER_CREDENTIALS, ";") // email;password_hashcode

    fun setPref(key: String, value: Any) {
        when (key) {
            PrefsConst.FINGERPRINT -> prefs.putString(PrefsConst.FINGERPRINT, value as String)
            PrefsConst.LAST_SLEEP_DATE -> prefs.putString(
                PrefsConst.LAST_SLEEP_DATE,
                value as String
            )
            PrefsConst.IS_FIRST_LAUNCH -> prefs.putBoolean(
                PrefsConst.IS_FIRST_LAUNCH,
                value as Boolean
            )
            PrefsConst.USER_CREDENTIALS -> prefs.putString(
                PrefsConst.USER_CREDENTIALS,
                value as String
            )
            else -> throw Exception("Unknown key!")
        }
    }

    fun getPref(key: String): Any = when (key) {
        PrefsConst.FINGERPRINT -> fingerprint.get()
        PrefsConst.LAST_SLEEP_DATE -> lastSleepDate.get()
        PrefsConst.IS_FIRST_LAUNCH -> isFirstLaunch.get()
        PrefsConst.USER_CREDENTIALS -> credentials.get()
        else -> throw Exception("Unknown key!")
    }
}