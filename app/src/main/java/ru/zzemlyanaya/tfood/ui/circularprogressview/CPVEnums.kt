/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 17:39
 */

package ru.zzemlyanaya.tfood.ui.circularprogressview

import android.graphics.Paint

/**
 * Enum to specify a direction at which progress line will be animated
 */
enum class CPVDirection {
    CLOCKWISE,
    ANTICLOCKWISE
}

/**
 * Enum to specify a stroke cap of the progress line
 */
enum class CPVStrokeCap(val index: Int, val cap: Paint.Cap) {
    ROUND(index = 0, cap = Paint.Cap.ROUND),
    BUTT(index = 1, cap = Paint.Cap.BUTT)
}