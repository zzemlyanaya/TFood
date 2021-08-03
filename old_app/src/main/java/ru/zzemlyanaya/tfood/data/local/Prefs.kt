/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.data.local

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object PrefsConst {
    const val PREFS_NAME = "tfood_prefs"
    const val FIELD_USER_FINGERPRINT = "user_fingrprint"
    const val FIELD_IS_NOTIFY_EAT = "notify_eat"
    const val FIELD_IS_NOTIFY_WATER = "notify_water"
    const val FIELD_NOTIFY_WATER_NUMBER = "notify_water_number"
    const val FIELD_IS_FIRST_LAUNCH = "is_first_launch"
    const val FIELD_LAST_SLEEP_DATE = "sleep_date"
    const val FIELD_LAST_CHECKUP_WN = "last_checkup_week_number"
    const val FIELD_USER_NOW = "user_now"
}

class Prefs(prefs: KryptoPrefs) : KryptoContext(prefs) {
    private val userID = string(PrefsConst.FIELD_USER_FINGERPRINT, "")
    private val isNotifyEat = boolean(PrefsConst.FIELD_IS_NOTIFY_EAT, true)
    private val isNotifyWater = boolean(PrefsConst.FIELD_IS_NOTIFY_WATER, false)
    private val notifyWaterNumber = int(PrefsConst.FIELD_NOTIFY_WATER_NUMBER, 3)
    private val isFirstLaunch = boolean(PrefsConst.FIELD_IS_FIRST_LAUNCH, true)
    private val lastSleepDate = string(PrefsConst.FIELD_LAST_SLEEP_DATE, "")
    private val lastCheckupWN = int(PrefsConst.FIELD_LAST_CHECKUP_WN, 0)
    private val userNow = string(PrefsConst.FIELD_USER_NOW, "0;0") //eaten;burnt

    fun setPref(key: String, value: Any) {
        when (key) {
            PrefsConst.FIELD_USER_FINGERPRINT -> prefs.putString(PrefsConst.FIELD_USER_FINGERPRINT, value as String)
            PrefsConst.FIELD_IS_NOTIFY_EAT -> prefs.putBoolean(
                PrefsConst.FIELD_IS_NOTIFY_EAT,
                value as Boolean
            )
            PrefsConst.FIELD_IS_NOTIFY_WATER -> prefs.putBoolean(
                PrefsConst.FIELD_IS_NOTIFY_WATER,
                value as Boolean
            )
            PrefsConst.FIELD_NOTIFY_WATER_NUMBER -> prefs.putInt(
                PrefsConst.FIELD_NOTIFY_WATER_NUMBER,
                value as Int
            )
            PrefsConst.FIELD_LAST_CHECKUP_WN -> prefs.putInt(
                PrefsConst.FIELD_LAST_CHECKUP_WN,
                value as Int
            )
            PrefsConst.FIELD_LAST_SLEEP_DATE -> prefs.putString(
                PrefsConst.FIELD_LAST_SLEEP_DATE,
                value as String
            )
            PrefsConst.FIELD_IS_FIRST_LAUNCH -> prefs.putBoolean(
                PrefsConst.FIELD_IS_FIRST_LAUNCH,
                value as Boolean
            )
            PrefsConst.FIELD_USER_NOW -> prefs.putString(PrefsConst.FIELD_USER_NOW, value as String)
            else -> throw Exception("Unknown key!")
        }
    }

    fun getPref(key: String): Any = when (key) {
        PrefsConst.FIELD_USER_FINGERPRINT -> userID.get()
        PrefsConst.FIELD_IS_NOTIFY_EAT -> isNotifyEat.get()
        PrefsConst.FIELD_IS_NOTIFY_WATER -> isNotifyWater.get()
        PrefsConst.FIELD_NOTIFY_WATER_NUMBER -> notifyWaterNumber.get()
        PrefsConst.FIELD_LAST_SLEEP_DATE -> lastSleepDate.get()
        PrefsConst.FIELD_IS_FIRST_LAUNCH -> isFirstLaunch.get()
        PrefsConst.FIELD_LAST_CHECKUP_WN -> lastCheckupWN.get()
        PrefsConst.FIELD_USER_NOW -> userNow.get()
        else -> throw Exception("Unknown key!")
    }
}