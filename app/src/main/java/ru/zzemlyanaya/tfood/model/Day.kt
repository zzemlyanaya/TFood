/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 11:44
 */

package ru.zzemlyanaya.tfood.model

data class Day (
        val _id: Int = 0,
        var userId: Int = 0,
        val date: String = "",
        var kkal: Int = 0,
        var prots: Float = 0F,
        var fats: Float = 0F,
        var carbs: Float = 0F,
        var breakfast: Array<Product> = emptyArray(),
        var lunch: Array<Product> = emptyArray(),
        var dinner: Array<Product> = emptyArray(),
        var snack: Array<Product> = emptyArray(),
        var activities: Array<Product> = emptyArray(),
        var water: Int = 0,
        var vitamins: String = "",
        var minerals: Minerals
)

data class Minerals(
        var Ca: Double,
        var P: Double,
        var Mg: Double,
        var K: Double,
        var Na: Double,
        var Fe: Double,
        var Zn: Double,
        var I: Double,
        var Se: Double,
        var F: Double

)