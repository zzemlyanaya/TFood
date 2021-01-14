/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 15:40
 */

package ru.zzemlyanaya.tfood.model

data class User(
        var id: Int = 0,
        var email: String = "",
        var username: String? = null,
        var gender: Boolean? = null, //true = man, false = woman
        var birthdate: String? = null,
        var weight: Int? = null,
        var height: Int? = null,
        var chest: Int? = null
        )

