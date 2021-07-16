/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 12:25
 */

package ru.zzemlyanaya.uikit.circularprogressview

import android.graphics.Color

/**
 * Data class representing section with [Float] amount, name and color of dialog_progress.xml line.
 */
data class CPVSection(
    val name: String = "",
    val color: Int = Color.parseColor("#FA8925"),
    val amount: Float
)