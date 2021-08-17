/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 04.08.2021, 16:56
 */

package ru.zzemlyanaya.login.data.model

data class LoginDTO(
    private var email: String,
    private var fingerprint: String,
    private var password: String
)