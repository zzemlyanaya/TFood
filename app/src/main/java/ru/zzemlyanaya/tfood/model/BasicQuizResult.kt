/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:08
 */

package ru.zzemlyanaya.tfood.model

data class BasicQuizResult(
        var token: String,
        var weightHeight: String,
        var kkalNeed: Double,
        var perfectKkalNeed: Double,
        var water: Double
)