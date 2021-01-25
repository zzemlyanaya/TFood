/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 25.01.2021, 15:51
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String
        getDay(date)
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
                            binding.cardRecordKPFC.visibility = View.VISIBLE
                            binding.dairyRecyclerView.visibility = View.VISIBLE

                            update(day)
                        }
                    }
                }
            }
        })
    }

    private fun update(day: Day) {
        binding.textDayKcal.text = day.kkal.toString()
        binding.textRecordProts.text = day.prots.toString()
        binding.textRecordFats.text = day.fats.toString()
        binding.textRecordCarbs.text = day.carbs.toString()

        val list = ArrayList<Record>()
        var kcal = 0f
        for (i in day.breakfast)
            kcal += i.kkal
        list.add(Record(getString(R.string.breakfast), kcal.toInt()))
        kcal = 0f

        for (i in day.lunch)
            kcal += i.kkal
        list.add(Record(getString(R.string.lunch), kcal.toInt()))
        kcal = 0f

        for (i in day.dinner)
            kcal += i.kkal
        list.add(Record(getString(R.string.dinner), kcal.toInt()))
        kcal = 0f

        for (i in day.snack)
            kcal += i.kkal
        list.add(Record(getString(R.string.snack), kcal.toInt()))
        kcal = 0f

        for (i in day.activities)
            list.add(Record(i.name, i.ecost.toInt()))

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
                val dateString =
                        "${DateUtils.getYear(date)}-${DateUtils.getMonthNumber(date)}-${DateUtils.getDayNumber(date)}"
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