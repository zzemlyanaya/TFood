/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

data class ActivityResponse(
    var activity: ActivityEntity,
    var duration: Int,
    var kcal: Double
)

data class ActivityEntity(
    var id: String,
    var name: String,
    var ecost: Float,
    var type: ActivityType
)

enum class ActivityType {
    HOUSEWORK, SPORT
}