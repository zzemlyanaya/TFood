/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 17:29
 */

package ru.zzemlyanaya.tfood.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.zzemlyanaya.tfood.App
import java.io.IOException

class LocalRepository {
    private var dao: ILocalDbDao? = null
    private val dataStore = App.dataStore

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null
        fun getInstance() =
            instance
                ?: synchronized(this) {
                    instance ?: LocalRepository()
                        .also { instance = it }
                }

        object PreferencesKeys {
            val FIELD_USER_TOKEN = preferencesKey<String>("user_auth")
            val FIELD_IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
            val FIELD_IS_NOTIFY_EAT = preferencesKey<Boolean>("notify_eat")
            val FIELD_IS_NOTIFY_WATER = preferencesKey<Boolean>("notify_water")
            val FIELD_NOTIFY_WATER_NUMBER = preferencesKey<Int>("notify_water_number")
            val FIELD_IS_FIRST_START_OVERALL = preferencesKey<Boolean>("is_first_start_overall")
            val FIELD_IS_FIRST_START_TODAY = preferencesKey<Boolean>("is_first_start_today")
            val FIELD_SLEEP_TODAY = preferencesKey<Int>("today's_sleep")
            val FIELD_SLEEP_DATE = preferencesKey<String>("sleep_date")
        }

    }

    var userFirstUseOverallFlow: Flow<Boolean>
    var userFirstUseTodayFlow: Flow<Boolean>
    var userTokenFlow: Flow<String>
    var isDarkModeFlow: Flow<Boolean>
    var isNotifyEatFlow: Flow<Boolean>
    var isNotifyWaterFlow: Flow<Boolean>
    var sleepDateFlow: Flow<String>
    var sleepAmountFlow: Flow<Int>
    var notifyWaterNumberFlow: Flow<Int>

    init {
        userFirstUseOverallFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_FIRST_START_OVERALL] ?: true }
        userFirstUseTodayFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_FIRST_START_TODAY] ?: true }
        userTokenFlow = dataStore.data.map { it[PreferencesKeys.FIELD_USER_TOKEN] ?: "" }
        userFirstUseOverallFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_FIRST_START_OVERALL] ?: true }
        isDarkModeFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_DARK_MODE] ?: true }
        isNotifyEatFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_NOTIFY_EAT] ?: true }
        isNotifyWaterFlow = dataStore.data.map { it[PreferencesKeys.FIELD_IS_NOTIFY_WATER] ?: true }
        sleepDateFlow = dataStore.data.map { it[PreferencesKeys.FIELD_SLEEP_DATE] ?: "" }
        sleepAmountFlow = dataStore.data.map { it[PreferencesKeys.FIELD_SLEEP_TODAY] ?: 0 }
        notifyWaterNumberFlow = dataStore.data.map { it[PreferencesKeys.FIELD_NOTIFY_WATER_NUMBER] ?: 0 }
    }

    fun<T> getPref(key: Preferences.Key<T>) = dataStore.data.catch { exception ->
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }
        .map { it[key] }

    suspend fun<T> updatePref(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    fun setDAO(dao: ILocalDbDao){
        this.dao = dao
    }


    // getSmth() = dao.getSmth()

}