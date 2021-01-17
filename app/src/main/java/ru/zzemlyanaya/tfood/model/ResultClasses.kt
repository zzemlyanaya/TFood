/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 17.01.2021, 20:11
 */

package ru.zzemlyanaya.tfood.model

data class BasicQuizResult(
        var weightVal: Int,
        var border: Double,
        var perfectKkalNeed: Int,
        var water: Int,
        var prots: Double,
        var fats: Double,
        var carbs: Double,
        var token: String
)

data class SleepQuizResult(
        var perfectKkalNeed: Double,
        var water: Double,
        var prots: Double,
        var fats: Double,
        var carbs: Double
)