/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 23:41
 */

package ru.zzemlyanaya.tfood.data.local

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.coroutineScope

class LocalRepository(private val dao: ILocalDbDao) {
    companion object {

        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null

        fun getInstance(dao: ILocalDbDao) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: LocalRepository(
                            dao
                        )
                            .also { instance = it }
                }





        suspend fun<T> setPref(tag: Preferences.Key<T>, value: T) {
            coroutineScope {
                dataStore.edit { preference ->
                    preference[tag] = value
                }
            }
        }

        suspend fun<T> getPref(tag: Preferences.Key<T>) {
            coroutineScope {  dataStore.data
                    .collect { preference -> preference[tag] }
            }
        }
    }


    // getSmth() = dao.getSmth()

}