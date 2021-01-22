/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 11:49
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
        var border: Double
)
data class PFC(
        var prots: Double,
        var fats: Double,
        var carbs: Double
)

data class SleepQuizResult(
        var energyNeed: Double,
        var water: Double,
        var pfc: PFC
)