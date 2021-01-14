/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 16:19
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class User(
        var _id: Int = 0,
        var email: String = "",
        var username: String? = null,
        var gender: Boolean? = null, //true = man, false = woman
        var birthdate: String? = null,
        var weight: Int? = null,
        var height: Int? = null,
        var chest: Int? = null
        ) : Serializable

