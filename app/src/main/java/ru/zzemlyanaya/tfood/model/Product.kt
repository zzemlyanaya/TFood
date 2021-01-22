/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 14:24
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class Product(
        val id: Int,
        var name: String,
        var kkal: Int,
        var prots: Float,
        var fats: Float,
        var carbs: Float,
        var vitamins: Vitamins,
        var minerals: Minerals
) : Serializable