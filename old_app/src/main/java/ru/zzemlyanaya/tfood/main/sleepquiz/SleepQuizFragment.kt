/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.*
import ru.zzemlyanaya.tfood.*
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentSleepQuizBinding
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.basicquiz.BasicQuizViewModel
import ru.zzemlyanaya.tfood.model.BasicQuizResult
import ru.zzemlyanaya.tfood.model.SleepQuizResult
import ru.zzemlyanaya.tfood.model.Status
import ru.zzemlyanaya.tfood.uikit.CTPView
import toothpick.ktp.KTP
import javax.inject.Inject
import javax.inject.Named


class SleepQuizFragment : Fragment() {

    lateinit var binding: FragmentSleepQuizBinding
    private var bedTime = 0
    private var wakeTime = 0
    private var overall = 0

    private var shouldSendOnlySleep = false
    private lateinit var viewModel: Any

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    @field:Named("token")
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScopes(APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
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
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quiz, container, false)

        binding.circleTimerView.setOnTimeChangedListener(object : CTPView.OnTimeChangedListener {
            override fun onMoveEnded() {
                showTimeViews()
                var hours = 24 - bedTime / 60 - (if (bedTime % 60 != 0) 1 else 0) + wakeTime / 60
                var minutes = 60 - bedTime % 60 + wakeTime % 60
                hours = (hours + minutes / 60) % 24
                minutes %= 60
                overall = hours * 60 + minutes
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
            localRepository.updatePref(PrefsConst.FIELD_SLEEP_TODAY, overall)
            if (shouldSendOnlySleep) {
                (viewModel as SleepQuizViewModel).sendSleep(
                    token,
                    overall.toDouble() / 60 + overall.toDouble() % 60 / 60
                )
                    .observe(viewLifecycleOwner, {
                        it?.let {
                            when (it.status) {
                                Status.LOADING -> {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.computing),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Status.ERROR -> {
                                    Log.d(DEBUG_TAG, it.message.orEmpty())
                                }
                                Status.SUCCESS -> {
                                    saveData(it.data!!)
                                    (requireActivity() as MainActivity).showDashboard(CongratsTypes.NONE)
                                }
                            }
                        }
                    })
            } else {
                (viewModel as BasicQuizViewModel).update(
                    "sleep",
                    overall.toDouble() / 60 + overall.toDouble() % 60 / 60
                )
                (viewModel as BasicQuizViewModel).saveData()
                (viewModel as BasicQuizViewModel).sendData().observe(viewLifecycleOwner, {
                    it?.let {
                        when (it.status) {
                            Status.LOADING -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.computing),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Status.ERROR -> {
                                Log.d(DEBUG_TAG, it.message.orEmpty())
                            }
                            Status.SUCCESS -> {
                                saveData(it.data!!)
                                (requireActivity() as MainActivity).showBasicResult(
                                    it.data.weight.weightVal,
                                    it.data.weight.border,
                                    it.data.energyNeed,
                                    it.data.water
                                )
                            }
                        }
                    }
                })
            }
        }

        return binding.root
    }

    private fun saveData(result: BasicQuizResult) {
        val norm =
            "${result.energyNeed};${result.pfc.prots};${result.pfc.fats};${result.pfc.carbs};${result.water}"
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NORM, norm)
        localRepository.updatePref(PrefsConst.FIELD_USER_TOKEN, result.token)
    }

    private fun saveData(result: SleepQuizResult) {
        val norm =
            "${result.energyNeed};${result.pfc.prots};${result.pfc.fats};${result.pfc.carbs};${result.water}"
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NORM, norm)
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
            SleepQuizFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(SHOULD_SEND_ONLY_SLEEP, shouldSendOnlySleep)
                    putString(TOKEN, token)
                }
            }
    }

}