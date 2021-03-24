/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.03.2021, 12:02
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

    val weight = LocalRepository.getInstance().getPref(PrefsConst.FIELD_USER_DATA).toString()
        .split(";") [3].toInt()

    private val viewModel by lazy { ViewModelProviders.of(this).get(DairyViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.simpleRatingBar.setOnRatingChangeListener { _, rating, isFromUser ->
            if (isFromUser) viewModel.setRating(rating)
        }

        return binding.root
    }


    private fun getDay(date: String) {
        viewModel.getOrCreateRecord(date).observe(viewLifecycleOwner, {
            it?.let {
                when (it.status) {
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        val day = it.data!!
                        if (day != Day()) {
                            binding.emptyDairyGroup.visibility = View.INVISIBLE
                            binding.dairyContent.visibility = View.VISIBLE
                            update(day)
                        } else {
                            binding.dairyContent.visibility = View.INVISIBLE
                            binding.emptyDairyGroup.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun update(day: Day) {
        binding.simpleRatingBar.rating = day.rating

        binding.textDayKcal.text = day.kkal.toString()
        binding.textDayWater.text = day.water.toString()
        binding.textRecordProts.text = "%.1f".format(day.prots)
        binding.textRecordFats.text = "%.1f".format(day.fats)
        binding.textRecordCarbs.text = "%.1f".format(day.carbs)
        val kcalIdeal = localRepository.getPref(PrefsConst.FIELD_MACRO_NORM).toString()
            .split(";")
            .first().toFloat().toInt()

        val list = ArrayList<Record>()
        list.add(
            Record(
                getString(R.string.breakfast), day.breakfastKkal,
                getString(R.string.recommended_kcal) + " " + (kcalIdeal / 4).toString()
            )
        )
        list.add(
            Record(
                getString(R.string.lunch), day.lunchKkal,
                getString(R.string.recommended_kcal) + " " + (kcalIdeal * 7 / 20).toString()
            )
        )
        list.add(
            Record(
                getString(R.string.dinner), day.dinnerKkal,
                getString(R.string.recommended_kcal) + " " + (kcalIdeal / 4).toString()
            )
        )
        list.add(
            Record(
                getString(R.string.snack), day.snackKkal,
                getString(R.string.recommended_kcal) + " " + (kcalIdeal * 3 / 20).toString()
            )
        )

        for (i in day.activities)
            list.add(Record(i.name, i.ecost.toInt() * weight, ""))

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
                holder.itemView.findViewById<TextView>(R.id.textItemDay).text = DateUtils.getDayNumber(
                    date
                )
                holder.itemView.findViewById<TextView>(R.id.textItemWeekDay).text = DateUtils.getDay3LettersName(
                    date
                )
            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                return true
            }
        }

        val myCalendarChangesObserver = object : CalendarChangesObserver {
            var lastSelectedDate = ""

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                super.whenSelectionChanged(isSelected, position, date)
                if (lastSelectedDate != date.toString()) {
                    lastSelectedDate = date.toString()
                    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                    val month = SimpleDateFormat("M", Locale.getDefault()).format(date)
                    val day = SimpleDateFormat("d", Locale.getDefault()).format(date)
                    val dateString = "$year-$month-$day"
                    getDay(dateString)
                }
            }

            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                binding.textDairyMonth.text = SimpleDateFormat("LLLL", Locale.getDefault()).format(
                    date
                ).capitalize(Locale.getDefault())
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