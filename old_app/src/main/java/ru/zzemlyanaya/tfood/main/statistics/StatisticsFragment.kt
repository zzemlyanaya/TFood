/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.statistics

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentStatisticsBinding
import ru.zzemlyanaya.tfood.model.BarChart
import ru.zzemlyanaya.tfood.model.Status
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var recyclerView: RecyclerView

    private lateinit var data: List<BarChart>
    private var dates = ArrayList<String>()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(StatisticsViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        val format: DateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        for (i in 0..6) {
            dates.add(format.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val datesString = "${dates[0]}-${dates[6]}"
        data = listOf(
            BarChart(datesString, getString(R.string.kcal_title), R.color.blueLight, null),
            BarChart(datesString, getString(R.string.water_with_emoji), R.color.blueDark, null),
            BarChart(datesString, getString(R.string.prots), R.color.googleBlue, null),
            BarChart(datesString, getString(R.string.fats), R.color.googleBlue, null),
            BarChart(datesString, getString(R.string.carbs), R.color.googleBlue, null),
            BarChart(datesString, getString(R.string.sleep_with_emoji), R.color.blueDark, null)
        )
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)

        recyclerView = binding.chartsRecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChartsRecyclerAdapter(data)
        }

        getStatistics()

        return binding.root
    }

    private fun getStatistics() {
        viewModel.getStatistics().observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.computing),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.ERROR -> {
                        Log.d(DEBUG_TAG, it.message.toString())
                    }
                    Status.SUCCESS -> {
                        val weekNorm = it.data!![0]
                        val weekActual = it.data[1]
                        convertData(weekNorm, weekActual)
                    }
                }
            }
        })
    }

    private fun convertData(weekNorm: List<Int>, weekActual: List<Int>) {
        val entriesGroupNorm: MutableList<BarEntry> = ArrayList()
        val entriesGroupActual: MutableList<BarEntry> = ArrayList()

        for (i in weekNorm.indices) {
            val j = dates[i].split('.')[0].toFloat()
            entriesGroupNorm.add(BarEntry(j, weekNorm[i].toFloat()))
            entriesGroupActual.add(BarEntry(j, weekActual[i].toFloat()))
        }
        val set1 = BarDataSet(entriesGroupNorm, getString(R.string.norm)).apply {
            color = resources.getColor(R.color.primaryColour)
            form = Legend.LegendForm.CIRCLE
        }
        val set2 = BarDataSet(entriesGroupActual, getString(R.string.completed)).apply {
            form = Legend.LegendForm.CIRCLE
        }

        val groupSpace = 0.06f
        val barSpace = 0.02f

        for (item in data) {
            set2.color = resources.getColor(item.color_res)
//            item.dataSet = BarData(set1, set2).apply {
//                groupBars(dates[0].split('.')[0].toFloat(), groupSpace, barSpace)
//                setDrawValues(false)
//                isHighlightEnabled = false
//            }
            item.dataSet = BarData(set2).apply {
                setDrawValues(false)
                isHighlightEnabled = false
            }
        }
        recyclerView.adapter = ChartsRecyclerAdapter(data)
        //(recyclerView.adapter as ChartsRecyclerAdapter).notifyDataSetChanged()
    }
}