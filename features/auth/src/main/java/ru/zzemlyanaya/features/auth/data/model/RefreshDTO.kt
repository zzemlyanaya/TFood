/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 19:54
 */

package ru.zzemlyanaya.features.auth.data.model

data class RefreshDTO(
    private var fingerprint: String,
    private var token: String
)
