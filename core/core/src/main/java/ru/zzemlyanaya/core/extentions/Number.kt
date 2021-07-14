/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.07.2021, 14:56
 */

package ru.zzemlyanaya.core.extentions

import java.math.BigDecimal

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L

fun Float?.orZero(): Float = this ?: 0.0f

fun Double?.orZero(): Double = this ?: 0.0

fun BigDecimal?.orZero(): BigDecimal = this ?: BigDecimal.ZERO
