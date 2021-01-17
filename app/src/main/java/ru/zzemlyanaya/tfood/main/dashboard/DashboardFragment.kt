/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 17.01.2021, 20:20
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentDashboardBinding
import ru.zzemlyanaya.tfood.ui.circularprogressview.CPVSection


class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentDashboardBinding

    private val localRepository = LocalRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        with(binding.storiesRecyclerView){
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = StoriesRecyclerAdapter(
                { onStoryClick(it) },
                listOf(
                    "Почему важно питаться правильно",
                    "10 причин заняться йогой",
                    "Чем грозит нехватка кальция"
                )
            )
        }

        setUpCaloriesWidget()
        setUpWaterWidget()
        setUpSleepWidget()
        setUpLevelWidget()

        return binding.root
    }

    private fun setUpCaloriesWidget(){
        val prots = CPVSection(amount = 21f, color = resources.getColor(R.color.primaryColour))
        binding.progressProts.apply {
            cap = 42f
            submitData(listOf(prots))
        }
        binding.textProts.text = "21/42"
        val fats = CPVSection(amount = 12f, color = resources.getColor(R.color.primaryColour))
        binding.progressFats.apply {
            cap = 35f
            submitData(listOf(fats))
        }
        binding.textFats.text = "21/35"
        val carbs = CPVSection(amount = 32f, color = resources.getColor(R.color.primaryColour))
        binding.progressCarbs.apply {
            cap = 83f
            submitData(listOf(carbs))
        }
        binding.textCarbs.text = "32/83"

        val animation: ObjectAnimator =
                ObjectAnimator.ofInt(binding.progressKcal, "progress", 0, 50)
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }

    private fun setUpSleepWidget() {
        val overall = localRepository.getPref(PrefsConst.FIELD_SLEEP_TODAY) as Int
        if (overall > 7*60)
            binding.textSleepQuality.text = getString(R.string.good_sleep)
        else
            binding.textSleepQuality.text = getString(R.string.bad_sleep)
        binding.textSleepHours2.text = (overall/60).toString()
        binding.textSleepMinutes2.text = (overall%60).toString()
    }

    private fun setUpWaterWidget(){
        val water = CPVSection(amount = 750f, color = resources.getColor(R.color.blueDark))
        binding.progressWater.apply {
            cap = 1500f
            submitData(listOf(water))
        }
    }

    private fun setUpLevelWidget(){
        val exp = CPVSection(amount = 50f, color = resources.getColor(R.color.orangeLight))
        binding.progressLvl.apply {
            cap = 100f
            submitData(listOf(exp))
        }
    }

    private fun onStoryClick(title: String) {
        // TODO show fragment w/ full story
    }

}