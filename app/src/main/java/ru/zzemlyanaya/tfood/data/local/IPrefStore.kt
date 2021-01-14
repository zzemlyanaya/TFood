/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 20:22
 */

package ru.zzemlyanaya.tfood.data.local

import java.util.concurrent.Flow

interface IPrefStore {
    fun userToken(): Flow<String>

    suspend fun updateToken()
}