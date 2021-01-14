/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 0:18
 */

package ru.zzemlyanaya.tfood.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentSleepQuizBinding
import ru.zzemlyanaya.tfood.ui.CTPView


class SleepQuizFragment : Fragment() {

    lateinit var binding: FragmentSleepQuizBinding
    private var bedTime = 0
    private var wakeTime = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quiz, container, false)

        binding.circleTimerView.setOnTimeChangedListener(object : CTPView.OnTimeChangedListener {
            override fun onMoveEnded() {
                showTimeViews()
                var hours = 24-bedTime/60 - (if (bedTime%60 != 0) 1 else 0) + wakeTime/60
                var minutes = 60-bedTime%60 + wakeTime%60
                hours = (hours + minutes/60)%24
                minutes %= 60
                binding.textSleepHours.text = (hours.toString())
                binding.textSleepMinutes.text = minutes.toString()
            }
            override fun bedTime(starting: String?, bedTime: Int) {
                Log.d("CircleTimePickerView", "START")
                hideTimeViews()
                binding.textSleepTime.text = starting
                this@SleepQuizFragment.bedTime = bedTime
            }

            override fun wakeTime(ending: String?, wakeTime: Int) {
                hideTimeViews()
                binding.textSleepTime.text = ending
                this@SleepQuizFragment.wakeTime = wakeTime
            }
        })

        return binding.root
    }

    private fun hideTimeViews() {
        binding.textSleepHours.visibility = View.INVISIBLE
        binding.textSleepMinutes.visibility = View.INVISIBLE
        binding.textViewM.visibility = View.INVISIBLE
        binding.textViewH.visibility = View.INVISIBLE
        binding.textSleepTime.visibility = View.VISIBLE
    }

    private fun showTimeViews() {
        binding.textSleepHours.visibility = View.VISIBLE
        binding.textSleepMinutes.visibility = View.VISIBLE
        binding.textViewM.visibility = View.VISIBLE
        binding.textViewH.visibility = View.VISIBLE
        binding.textSleepTime.visibility = View.INVISIBLE
    }

}