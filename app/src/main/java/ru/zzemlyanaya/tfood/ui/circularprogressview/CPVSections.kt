/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.01.2021, 20:03
 */

package ru.zzemlyanaya.tfood.ui.circularprogressview

import android.graphics.Color

/**
 * Data class representing section with [Float] amount, name and color of progress line.
 */
data class CPVSection(
        val name: String = "",
        val color: Int = Color.parseColor("#FA8925"),
        val amount: Float
)