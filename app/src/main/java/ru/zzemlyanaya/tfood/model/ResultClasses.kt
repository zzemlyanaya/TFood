/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 12:16
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