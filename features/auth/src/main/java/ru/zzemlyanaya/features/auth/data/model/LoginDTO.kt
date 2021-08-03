/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 19:42
 */

package ru.zzemlyanaya.features.auth.data.model

data class LoginDTO(
    private var email: String,
    private var fingerprint: String,
    private var password: String
)