/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.model

import com.github.mikephil.charting.data.BarData


data class BarChart(
    var dates: String,
    var title: String,
    var color_res: Int,
    var dataSet: BarData?
)