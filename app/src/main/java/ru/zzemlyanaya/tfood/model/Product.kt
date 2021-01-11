/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 12:02
 */

package ru.zzemlyanaya.tfood.model

data class Product(
        val id: Int,
        var name: String,
        var kkal: Int,
        var prots: Float,
        var fats: Float,
        var carbs: Float,
        var vitamins: String,
        var minerals: String
)