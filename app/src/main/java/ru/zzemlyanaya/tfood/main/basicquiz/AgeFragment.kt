/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:14
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.joda.time.DateTime
import org.joda.time.Months
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentAgeBinding
import java.util.*

class AgeFragment : Fragment() {
    private lateinit var binding: FragmentAgeBinding
    private val viewModel by lazy { ViewModelProviders.of(requireActivity()).get(BasicQuizViewModel::class.java)}

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_age, container, false)

        binding.textDay.afterTextChanged {
            getDate()
        }
        binding.textMonth.afterTextChanged {
            getDate()
        }
        binding.textYear.afterTextChanged {
            getDate()
        }

        return binding.root
    }

    private fun getDate(){
        val day = binding.textDay.text.toString().toIntOrNull()
        val month = binding.textMonth.text.toString().toIntOrNull()
        val year = binding.textYear.text.toString().toIntOrNull()
        if (validate(day, month, year)) {
            if (isTeenager(day!!, month!!, year!!)) {
                viewModel.update("birthday", "$year-$month-$day")
                binding.textDateError.text = ""
            }
            else
                binding.textDateError.text = getString(R.string.age_error)
        }
        else
            binding.textDateError.text = getString(R.string.date_error)
    }

    private fun validate(day: Int?, month: Int?, year: Int?) =
            if (day == null || month == null || year == null )
                false
            else if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)
                    || year < 1950 || year > Calendar.getInstance().get(Calendar.YEAR)
                    || month > 12 || month < 1 || day < 1 || day > 31) {
                false // only 1,3,5,7,8,10,12 has 31 days
            } else if (month == 2) {
                //leap year
                if (year % 4 == 0) {
                    !(day == 30 || day == 31)
                } else {
                    !(day == 29 || day == 30 || day == 31)
                }
            } else
                true

    private fun isTeenager(day: Int, month: Int, year: Int): Boolean{
        val birthdate = DateTime(year, month, day, 0, 0, 0)
        val now = DateTime()
        var age: Months = Months.monthsBetween(birthdate, now)
        val years = age.dividedBy(12)
        age = age.minus(years.multipliedBy(12))
        return years.months in 13..17 || years.months == 17 && age.months < 5
    }
}