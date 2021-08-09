/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.model

import com.google.gson.annotations.SerializedName

data class RefreshDTO(
    var fingerprint: String,
    @SerializedName("token")
    var refreshToken: String
)
