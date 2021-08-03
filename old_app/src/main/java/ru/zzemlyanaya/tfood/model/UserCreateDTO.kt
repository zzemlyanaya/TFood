/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 21:02
 */

package ru.zzemlyanaya.tfood.model

import com.google.gson.annotations.SerializedName

data class UserCreateDTO(
    private var email: String,
    @SerializedName("gmttimezone")
    private var gmtTimeZone: Int,
    private var password: String
)
