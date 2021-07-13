/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:16
 */

package ru.zzemlyanaya.tfood.main.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.model.BarChart

class ChartsRecyclerAdapter(
    private var values: List<BarChart>
) : RecyclerView.Adapter<ChartsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barchart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textDates.text = item.dates
        holder.textTitle.text = item.title
        holder.chart.apply {
            setDrawValueAboveBar(false)
            isScaleXEnabled = false
            isScaleYEnabled = false
            axisRight.isEnabled = false
            axisLeft.setDrawAxisLine(false)
            setDrawBorders(false)
            setFitBars(true)
            description = Description().apply { text = "" }
            data = item.dataSet
            animateY(3000, Easing.Linear)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textChartTitle)
        val textDates: TextView = view.findViewById(R.id.textDates)
        val chart: com.github.mikephil.charting.charts.BarChart = view.findViewById(R.id.chart)
    }
}