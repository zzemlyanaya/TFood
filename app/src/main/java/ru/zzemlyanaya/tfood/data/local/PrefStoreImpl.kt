/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 20:36
 */

package ru.zzemlyanaya.tfood.data.local

import android.content.Context
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.zzemlyanaya.tfood.App
import java.io.IOException

class PrefStoreImpl constructor(context: Context) : IPrefStore {
    private val dataStore = App.dataStore

    override fun userToken() = dataStore.data.catch { exception ->
        if (exception is IOException)
            emit(emptyPreferences())
        else
            throw exception
    }.map { it[PreferencesKey.FIELD_USER_TOKEN] ?: "" }

    override suspend fun updateToken() {
        TODO("Not yet implemented")
    }

    private object PreferencesKey {
        val FIELD_USER_TOKEN = preferencesKey<String>("user_auth")
        val FIELD_IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
        val FIELD_IS_NOTIFY_EAT = preferencesKey<Boolean>("notify_eat")
        val FIELD_IS_NOTIFY_WATER = preferencesKey<Boolean>("notify_water")
        val FIELD_NOTIFY_WATER_NUMBER = preferencesKey<Int>("notify_water_number")
        val FIELD_IS_FIRST_START_OVERALL = preferencesKey<Boolean>("is_first_start_overall")
        val FIELD_IS_FIRST_START_TODAY = preferencesKey<Boolean>("is_first_start_today")
    }
}