/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 25.01.2021, 21:23
 */

package ru.zzemlyanaya.tfood.main.dairy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentDairyBinding
import ru.zzemlyanaya.tfood.model.Day
import ru.zzemlyanaya.tfood.model.Record
import ru.zzemlyanaya.tfood.model.Status
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DairyFragment : Fragment() {
    private lateinit var binding: FragmentDairyBinding
    private lateinit var singleRowCalendar: SingleRowCalendar
    private val localRepository = LocalRepository.getInstance()

    private val token by lazy { localRepository.getPref(PrefsConst.FIELD_USER_TOKEN) as String }
    private val viewModel by lazy { ViewModelProviders.of(this).get(DairyViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dairy, container, false)

        setUpCalendar()

        binding.butToday.setOnClickListener {
            binding.calendar.apply {
                select(30)
                smoothScrollToPosition(30)
            }
        }

        binding.dairyRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RecordRecyclerAdapter(ArrayList())
        }

        return binding.root
    }


    private fun getDay(date: String) {
        viewModel.getOrCreateRecord(token, date).observe(viewLifecycleOwner, {
            it?.let {
                when(it.status){
                    Status.LOADING -> {}
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        val day = it.data!!
                        if (day != Day()) {
                            binding.emptyDairyGroup.visibility = View.INVISIBLE
                            binding.dairyContent.visibility = View.VISIBLE
                            update(day)
                        }
                        else {
                            binding.dairyContent.visibility = View.INVISIBLE
                            binding.emptyDairyGroup.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun update(day: Day) {
        binding.textDayKcal.text = day.kkal.toString()
        binding.textRecordProts.text = "%.1f".format(day.prots)
        binding.textRecordFats.text = "%.1f".format(day.fats)
        binding.textRecordCarbs.text = "%.1f".format(day.carbs)

        val list = ArrayList<Record>()
        var kcal_eaten = 0

        list.add(Record(getString(R.string.breakfast), day.breakfastKkal))
        kcal_eaten += day.breakfastKkal

        list.add(Record(getString(R.string.lunch), day.lunchKkal))
        kcal_eaten += day.lunchKkal

        list.add(Record(getString(R.string.dinner), day.dinnerKkal))
        kcal_eaten += day.dinnerKkal

        list.add(Record(getString(R.string.snack), day.snackKkal))
        kcal_eaten += day.snackKkal

        for (i in day.activities)
            list.add(Record(i.name, i.ecost.toInt()))

        val kcal_burnt = kcal_eaten - day.kkal
        val usernow = localRepository.getPref(PrefsConst.FIELD_USER_NOW).toString()
            .split(';')
            .map { item -> item.toInt() } as java.util.ArrayList<Int>

        usernow[0] = kcal_eaten
        usernow[1] = kcal_burnt
        localRepository.updatePref(PrefsConst.FIELD_USER_NOW, usernow.joinToString(";"))

        (binding.dairyRecyclerView.adapter as RecordRecyclerAdapter).updateData(list)
    }

    private fun setUpCalendar(){
        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                return if (isSelected)
                    R.layout.item_calendar_day_selected
                else
                    R.layout.item_calendar_day
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                holder.itemView.findViewById<TextView>(R.id.textItemDay).text = DateUtils.getDayNumber(date)
                holder.itemView.findViewById<TextView>(R.id.textItemWeekDay).text = DateUtils.getDay3LettersName(date)
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                val month = SimpleDateFormat("M", Locale.getDefault()).format(date)
                val day = SimpleDateFormat("d", Locale.getDefault()).format(date)
                val dateString = "$year-$month-$day"
                getDay(dateString)
                super.whenSelectionChanged(isSelected, position, date)
            }

            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                binding.textDairyMonth.text = SimpleDateFormat("LLLL", Locale.getDefault()).format(date).capitalize()
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
            }
        }

        singleRowCalendar = binding.calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            futureDaysCount = 30
            pastDaysCount = 30
            includeCurrentDate = true
            initialPositionIndex = 30
            init()
            select(30)
        }
    }
}