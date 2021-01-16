/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 10:36
 */

package ru.zzemlyanaya.tfood.data.local

import com.kryptoprefs.context.KryptoContext
import com.kryptoprefs.preferences.KryptoPrefs

object PrefsConst {
    const val PREFS_NAME = "tfood_prefs"
    const val FIELD_USER_TOKEN = "user_auth"
    const val FIELD_USER_ID = "user_id"
    const val FIELD_IS_NOTIFY_EAT = "notify_eat"
    const val FIELD_IS_NOTIFY_WATER = "notify_water"
    const val FIELD_NOTIFY_WATER_NUMBER = "notify_water_number"
    const val FIELD_IS_FIRST_LAUNCH = "is_first_launch"
    const val FIELD_SLEEP_TODAY = "today's_sleep"
    const val FIELD_LAST_SLEEP_DATE = "sleep_date"
}

class Prefs(prefs: KryptoPrefs): KryptoContext(prefs) {
    private val userToken = string(PrefsConst.FIELD_USER_TOKEN, "")
    private val userID = string(PrefsConst.FIELD_USER_ID, "")
    private val isNotifyEat = boolean(PrefsConst.FIELD_IS_NOTIFY_EAT, true)
    private val isNotifyWater = boolean(PrefsConst.FIELD_IS_NOTIFY_WATER, false)
    private val notifyWaterNumber = int(PrefsConst.FIELD_NOTIFY_WATER_NUMBER, 3)
    private val isFirstLaunch = boolean(PrefsConst.FIELD_IS_FIRST_LAUNCH, true)
    private val sleepToday = string(PrefsConst.FIELD_SLEEP_TODAY, "0")
    private val lastSleepDate = string(PrefsConst.FIELD_LAST_SLEEP_DATE, "")

    fun setPref(key: String, value: Any){
        when(key){
            PrefsConst.FIELD_USER_ID -> prefs.putString(PrefsConst.FIELD_USER_ID, value as String)
            PrefsConst.FIELD_USER_TOKEN -> prefs.putString(PrefsConst.FIELD_USER_TOKEN, value as String)
            PrefsConst.FIELD_IS_NOTIFY_EAT -> prefs.putBoolean(PrefsConst.FIELD_IS_NOTIFY_EAT, value as Boolean)
            PrefsConst.FIELD_IS_NOTIFY_WATER -> prefs.putBoolean(PrefsConst.FIELD_IS_NOTIFY_WATER, value as Boolean)
            PrefsConst.FIELD_NOTIFY_WATER_NUMBER -> prefs.putInt(PrefsConst.FIELD_NOTIFY_WATER_NUMBER, value as Int)
            PrefsConst.FIELD_SLEEP_TODAY -> prefs.putString(PrefsConst.FIELD_SLEEP_TODAY, value as String)
            PrefsConst.FIELD_LAST_SLEEP_DATE -> prefs.putString(PrefsConst.FIELD_LAST_SLEEP_DATE, value as String)
            PrefsConst.FIELD_IS_FIRST_LAUNCH -> prefs.putBoolean(PrefsConst.FIELD_IS_FIRST_LAUNCH, value as Boolean)
            else -> throw Exception("Unknown key!")
        }
    }

    fun getPref(key: String): Any = when(key) {
        PrefsConst.FIELD_USER_ID -> userID.get()
        PrefsConst.FIELD_USER_TOKEN -> userToken.get()
        PrefsConst.FIELD_IS_NOTIFY_EAT -> isNotifyEat.get()
        PrefsConst.FIELD_IS_NOTIFY_WATER -> isNotifyWater.get()
        PrefsConst.FIELD_NOTIFY_WATER_NUMBER -> notifyWaterNumber.get()
        PrefsConst.FIELD_SLEEP_TODAY -> sleepToday.get()
        PrefsConst.FIELD_LAST_SLEEP_DATE -> lastSleepDate.get()
        PrefsConst.FIELD_IS_FIRST_LAUNCH -> isFirstLaunch.get()
        else -> throw Exception("Unknown key!")
    }
}