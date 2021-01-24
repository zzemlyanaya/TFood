/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.01.2021, 11:47
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class ProductShort(
        var _id: String,
        var name: String
)

data class Product(
        val id: Int,
        var name: String,
        var kkal: Int,
        var prots: Float,
        var fats: Float,
        var carbs: Float,
        var alimentaryFiber: Float,
        var category: String,
        var vitamins: Vitamins,
        var minerals: Minerals
) : Serializable