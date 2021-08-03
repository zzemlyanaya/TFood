/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 19:45
 */

package ru.zzemlyanaya.tfood.model

data class TokenPair(
    var accessToken: String,
    var refreshToken: String
)