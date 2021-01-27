/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 13:11
 */

package ru.zzemlyanaya.tfood.model

data class Achievement(
    var _id: String,
    var icon_res: Int = 0,
    var name: String,
    var description: String,
    var progress: Float,
    var level: Int
)