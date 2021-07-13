/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

data class BasicQuizResult(
    var weight: Weight,
    var pfc: PFC,
    var energyNeed: Int,
    var water: Int,
    var token: String
)

data class Weight(
    var weightVal: Int,
    var border: Float
)

data class PFC(
    var prots: Float,
    var fats: Float,
    var carbs: Float
)

data class SleepQuizResult(
    var energyNeed: Double,
    var water: Double,
    var pfc: PFC
)