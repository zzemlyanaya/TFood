/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 12:23
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.*
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.SHOULD_SEND_ONLY_SLEEP
import ru.zzemlyanaya.tfood.TOKEN
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.databinding.FragmentSleepQuizBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.BasicQuizViewModel
import ru.zzemlyanaya.tfood.ui.CTPView


class SleepQuizFragment : Fragment() {

    lateinit var binding: FragmentSleepQuizBinding
    private val localRepository = LocalRepository.getInstance()
    private var bedTime = 0
    private var wakeTime = 0
    private var overall = 0

    private var shouldSendOnlySleep = false
    private lateinit var token: String
    private lateinit var viewModel: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shouldSendOnlySleep = it.getBoolean(SHOULD_SEND_ONLY_SLEEP) == true
            token = it.getString(TOKEN).orEmpty()
        }
        viewModel = if (shouldSendOnlySleep)
            ViewModelProviders.of(requireActivity()).get(SleepQuizViewModel::class.java)
        else
            ViewModelProviders.of(requireActivity()).get(BasicQuizViewModel::class.java)
    }

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
                overall = hours*60+minutes
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

        binding.butSetSleep.setOnClickListener {
            localRepository.updatePref("sleep", overall)
            if (shouldSendOnlySleep) {
//                (viewModel as SleepQuizViewModel).sendSleep(token, overall.toDouble())
//                    .observe(viewLifecycleOwner, {
//                        it?.let {
//                            when(it.status) {
//                                Status.LOADING -> {}
//                                Status.ERROR -> {}
//                                Status.SUCCESS -> {
//                                    (requireActivity() as MainActivity).showDashboard()
//                                }
//                            }
//                        }
//                    })
                Log.d("-----------HERE", "should send only sleep")
                (requireActivity() as MainActivity).showDashboard()
            }
            else {
                (viewModel as BasicQuizViewModel).update("sleep", overall)
//                (viewModel as BasicQuizViewModel).sendData().observe(viewLifecycleOwner, {
//                    it?.let {
//                        when (it.status) {
//                            Status.LOADING -> {
//                            }
//                            Status.ERROR -> {
//                            }
//                            Status.SUCCESS -> {
//                                (requireActivity() as MainActivity).showDashboard()
//                            }
//                        }
//                    }
//                })
                Log.d("-----------HERE", "should send all data")
                (requireActivity() as MainActivity).showDashboard()
            }
        }

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

    companion object {

        @JvmStatic
        fun newInstance(shouldSendOnlySleep: Boolean, token: String) =
            SleepQuizFragment().apply{
                arguments = Bundle().apply {
                    putBoolean(SHOULD_SEND_ONLY_SLEEP, shouldSendOnlySleep)
                    putString(TOKEN, token)
                }
            }
    }

}