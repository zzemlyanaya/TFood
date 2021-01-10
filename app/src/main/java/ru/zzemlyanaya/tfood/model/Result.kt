/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 17:18
 */

package ru.zzemlyanaya.tfood.model

data class Result<T>(
        val error: String?,
        val data: T?
)