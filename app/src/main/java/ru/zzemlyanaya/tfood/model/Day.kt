/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 14:45
 */

package ru.zzemlyanaya.tfood.model

data class Day (
        val id: Int = 0,
        var userId: Int = 0,
        val date: String = "",
        var kkal: Int = 0,
        var prots: Float = 0F,
        var fats: Float = 0F,
        var carbs: Float = 0F,
        var breakfast: String = "",
        var lunch: String = "",
        var dinner: String = "",
        var snack: String = "",
        var water: Int = 0,
        var vitamins: String = "",
        var minerals: String = "",
        var phActLen: Int = 0
)