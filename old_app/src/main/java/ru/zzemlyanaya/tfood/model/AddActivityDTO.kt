/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 21:38
 */

package ru.zzemlyanaya.tfood.model

data class AddActivityDTO(
    var activityId: String,
    var length: Int,
    var type: ActivityType
)
