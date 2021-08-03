/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 21:07
 */

package ru.zzemlyanaya.tfood.model

data class UserUpdateResponse(
    var user: User,
    var weightResult: WeightResult
)

data class WeightResult(
    var recommendedWeight: Float,
    var weightValue: WeightGrade
)

enum class WeightGrade{
    PERFECT, TOO_LITTLE, TOO_MUCH
}
