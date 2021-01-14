/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 14:54
 */

package ru.zzemlyanaya.tfood.model

data class CreateUserResponse(
        var user: User,
        var token: String
)