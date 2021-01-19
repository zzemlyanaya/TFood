/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 18.01.2021, 17:13
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

        val norm = (localRepository.getPref(PrefsConst.FIELD_MACRO_NORM) as String).split(';')
        val now = (localRepository.getPref(PrefsConst.FIELD_MACRO_NOW) as String).split(';')

        setUpCaloriesWidget(norm, now)
        setUpWaterWidget(norm[4].toFloat(), now[4].toFloat())
        setUpSleepWidget()
        setUpLevelWidget()

        return binding.root
    }

    private fun setUpCaloriesWidget(norm: List<String>, now: List<String>){
        binding.progressKcal.max = norm[0].toInt()
        val animation: ObjectAnimator =
                ObjectAnimator.ofInt(binding.progressKcal, "progress", 0, now[0].toInt())
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()

        val prots = CPVSection(amount = now[1].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressProts.apply {
            cap = norm[1].toFloat()
            submitData(listOf(prots))
        }
        binding.textProts.text = "${now[1].toFloat()}/${norm[1].toFloat()}"

        val fats = CPVSection(amount = now[2].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressFats.apply {
            cap = norm[2].toFloat()
            submitData(listOf(fats))
        }
        binding.textFats.text = "${now[2].toFloat()}/${norm[2].toFloat()}"

        val carbs = CPVSection(amount = now[3].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressCarbs.apply {
            cap = norm[3].toFloat()
            submitData(listOf(carbs))
        }
        binding.textCarbs.text = "${now[3].toFloat()}/${norm[3].toFloat()}"

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

    private fun setUpWaterWidget(waterNorm: Float, waterNow: Float){
        val water = CPVSection(amount = waterNow, color = resources.getColor(R.color.blueDark))
        binding.progressWater.apply {
            cap = waterNorm
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