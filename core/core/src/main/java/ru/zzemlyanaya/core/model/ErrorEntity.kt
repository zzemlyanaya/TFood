/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 12:20
 */

package ru.zzemlyanaya.core.model

data class ErrorEntity(
    var statusCode: Int,
    var message: String
)