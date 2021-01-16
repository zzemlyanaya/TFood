/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:24
 */

package ru.zzemlyanaya.tfood.data.local

import ru.zzemlyanaya.tfood.App

class LocalRepository {
    private var dao: ILocalDbDao? = null
    private val prefs = App.prefs

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: LocalRepository? = null
        fun getInstance() =
            instance
                ?: synchronized(this) {
                    instance ?: LocalRepository()
                        .also { instance = it }
                }

    }

    fun getPref(key: String) = prefs.getPref(key)

    fun updatePref(key: String, value: Any) {
        prefs.setPref(key, value)
    }

    fun setDAO(dao: ILocalDbDao){
        this.dao = dao
    }


    // getSmth() = dao.getSmth()

}