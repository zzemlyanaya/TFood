/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.01.2021, 19:45
 */

package ru.zzemlyanaya.tfood.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import kotlinx.coroutines.flow.collect

class LocalRepository(
    private val dao: ILocalDbDao,
    private val dataStore: DataStore<Preferences>
    ) {
    companion object {

        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null

        fun getInstance(dao: ILocalDbDao, dataStore: DataStore<Preferences>) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: LocalRepository(
                            dao,
                            dataStore
                        )
                            .also { instance = it }
                }

        val FIELD_USER_AUTH = preferencesKey<String>("user_auth")
        val FIELD_IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
        val FIELD_IS_NOTIFY_EAT = preferencesKey<Boolean>("notify_eat")
        val FIELD_IS_NOTIFY_WATER = preferencesKey<Boolean>("notify_water")
        val FIELD_NOTIFY_WATER_NUMBER = preferencesKey<Int>("notify_water_number")
    }

    suspend fun<T> setPref(tag: Preferences.Key<T>, value: T) = dataStore.edit { preference ->
            preference[tag] = value
    }

    suspend fun<T> getPref(tag: Preferences.Key<T>) = dataStore.data
        .collect { preference -> preference[tag] }

    // getSmth() = dao.getSmth()

}