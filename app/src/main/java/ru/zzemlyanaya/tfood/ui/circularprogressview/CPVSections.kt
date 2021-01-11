/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 17:42
 */

package ru.zzemlyanaya.tfood.ui.circularprogressview

/**
 * Data class representing section with [Float] amount, name and color of progress line.
 */
data class CPVSection(
        val name: String,
        val color: Int,
        val amount: Float
)