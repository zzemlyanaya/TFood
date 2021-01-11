/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 16:42
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentAgeBinding
import java.text.SimpleDateFormat
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
        val day = binding.textDay.text.toString()
        val month = binding.textMonth.text.toString()
        val year = binding.textYear.text.toString()
        if (validate(day.toIntOrNull(), month.toIntOrNull(), year.toIntOrNull())) {
            val format = SimpleDateFormat("dd.MM.yyyy")
            val date: Date = format.parse("$day.$month.$year")!!
            viewModel.update("birthday", date)
            binding.textDateError.text = ""
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

}