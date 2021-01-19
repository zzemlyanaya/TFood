/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 19.01.2021, 18:34
 */

package ru.zzemlyanaya.tfood.main.dairy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentDairyBinding
import java.text.SimpleDateFormat
import java.util.*


class DairyFragment : Fragment() {
    private lateinit var binding: FragmentDairyBinding
    private lateinit var singleRowCalendar: SingleRowCalendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dairy, container, false)

        setUpCalendar()

        binding.butToday.setOnClickListener {
            binding.calendar.apply {
                select(30)
                smoothScrollToPosition(30)
            }
        }

        return binding.root
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