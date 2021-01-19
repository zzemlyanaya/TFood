/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 19.01.2021, 18:34
 */

package ru.zzemlyanaya.tfood.model

import com.github.mikephil.charting.data.BarData


data class BarChart(
    var dates: String,
    var title: String,
    var color_res: Int,
    var dataSet: BarData?
)