/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 19:45
 */

package ru.zzemlyanaya.features.auth.data.model

data class TokenPair(
    private var accessToken: String,
    private var refreshToken: String
)