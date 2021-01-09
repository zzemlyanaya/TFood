/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.01.2021, 19:45
 */

package ru.zzemlyanaya.tfood.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities = [],
//    version =  1,
//    exportSchema = false
//)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun dao(): ILocalDbDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LocalDatabase::class.java, "tfood_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
