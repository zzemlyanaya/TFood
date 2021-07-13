/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:16
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