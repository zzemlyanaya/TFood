/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class User(
    var id: String,
    var username: String,
    var gender: Boolean, //true = man, false = woman
    var birthdate: String,
    var weight: Int,
    var height: Int,
    var chest: Int
) : Serializable

