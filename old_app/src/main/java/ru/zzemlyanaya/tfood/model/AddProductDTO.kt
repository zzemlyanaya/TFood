/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 21:40
 */

package ru.zzemlyanaya.tfood.model

data class AddProductDTO(
    var eating: Eating,
    var mass: Int,
    var productId: String
    )

enum class Eating {
    BREAKFAST, DINNER, LUNCH, SNACK
}
