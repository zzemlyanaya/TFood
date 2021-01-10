/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 20:03
 */

package ru.zzemlyanaya.tfood.model

data class User(
        val id: Int,
        var email: String,
        var name: String?,
        var gender: Int?,
        var age: String?,
        var weight: Int?,
        var height: Int?,
        var gk: Int?
        )

