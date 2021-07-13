/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class Product(
    var _id: String = "",
    var name: String = "",
    var kkal: Float = 0f,
    var prots: Float = 0f,
    var fats: Float = 0f,
    var carbs: Float = 0f,
    var alimentaryFiber: Float = 0f,
    var category: String = "",
    var vitamins: Vitamins = Vitamins(),
    var minerals: Minerals = Minerals()
) : Serializable

data class ProductShort(
    var _id: String = "",
    var name: String = "",
    var kkal: Float = 0f,
    var prots: Float = 0f,
    var fats: Float = 0f,
    var carbs: Float = 0f,
    var alimentaryFiber: Float = 0f,
    var category: String = "",
    var vitamins: String = "",
    var minerals: String = ""
) : Serializable