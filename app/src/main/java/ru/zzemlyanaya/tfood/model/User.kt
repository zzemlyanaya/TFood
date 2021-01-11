/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 14:45
 */

package ru.zzemlyanaya.tfood.model

import java.util.*

data class User(
        var id: Int = 0,
        var email: String = "",
        var username: String? = null,
        var gender: Boolean? = null, //true = man, false = woman
        var birthdate: Date? = null,
        var weight: Int? = null,
        var height: Int? = null,
        var chest: Int? = null
        )

