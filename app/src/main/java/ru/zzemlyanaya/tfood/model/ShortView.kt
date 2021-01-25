/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 25.01.2021, 12:55
 */

package ru.zzemlyanaya.tfood.model

data class ShortView(
        var _id: String,
        var name: String
)

data class Record(
        var name: String,
        var kcal: Int
)