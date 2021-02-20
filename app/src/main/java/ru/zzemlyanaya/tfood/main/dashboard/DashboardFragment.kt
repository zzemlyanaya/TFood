/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.02.2021, 14:46
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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentDashboardBinding
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.MainViewModel
import ru.zzemlyanaya.tfood.model.Article
import ru.zzemlyanaya.tfood.ui.circularprogressview.CPVSection


class DashboardFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentDashboardBinding

    private val localRepository = LocalRepository.getInstance()
    private val token = localRepository.getPref(PrefsConst.FIELD_USER_TOKEN) as String
    private val today = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String

    private val mainViewModel by lazy { ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java) }
    private val articlesViewModel by lazy { ViewModelProviders.of(requireActivity()).get(ArticlesViewModel::class.java) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articlesViewModel.articleList.observe(viewLifecycleOwner, { list ->
            if (binding.storiesRecyclerView != null)
                (binding.storiesRecyclerView.adapter as ArticleRecyclerAdapter).update(list)
        })
    }

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
            adapter = ArticleRecyclerAdapter(
                { onArticleClick(it) },
                emptyList()
            )
        }

        with(binding.gamesRecyclerView){
            layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            adapter = GamesRecyclerAdapter(
                    {  },
                    listOf(
                            "Packman",
                            "Змейка"
                    )
            )
        }

        val norm = (localRepository.getPref(PrefsConst.FIELD_MACRO_NORM) as String).split(';')
        val now = (localRepository.getPref(PrefsConst.FIELD_MACRO_NOW) as String).split(';')
        val addit = (localRepository.getPref(PrefsConst.FIELD_USER_NOW) as String).split(';')

        setUpCaloriesWidget(norm, now, addit)
        setUpWaterWidget(norm[4].toFloat().toInt(), now[4].toFloat().toInt())
        setUpSleepWidget()

        binding.achievCard.setOnClickListener { (requireActivity() as MainActivity).showAchievements("dashboard") }

        return binding.root
    }

    private fun setUpCaloriesWidget(norm: List<String>, now: List<String>, addit: List<String>){
        binding.textKcalGoal.text = norm[0].toDouble().toInt().toString()
        val left = norm[0].toDouble().toInt() - now[0].toDouble().toInt()
        binding.textKcalLeft.text =  if (left >= 0) left.toString() else "0"
        binding.textKcalEaten.text = addit[0]
        binding.textKcalBurnt2.text = addit[1]
        binding.progressKcal.max = norm[0].toDouble().toInt()
        val animation: ObjectAnimator =
                ObjectAnimator.ofInt(binding.progressKcal, "progress", 0, now[0].toFloat().toInt())
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()

        val prots = CPVSection(amount = now[1].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressProts.apply {
            cap = norm[1].toFloat()
            submitData(listOf(prots))
        }
        binding.textProts.text = "%.1f/%.1f".format(now[1].toFloat(), norm[1].toFloat())

        val fats = CPVSection(amount = now[2].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressFats.apply {
            cap = norm[2].toFloat()
            submitData(listOf(fats))
        }
        binding.textFats.text = "%.1f/%.1f".format(now[2].toFloat(), norm[2].toFloat())

        val carbs = CPVSection(amount = now[3].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.progressCarbs.apply {
            cap = norm[3].toFloat()
            submitData(listOf(carbs))
        }
        binding.textCarbs.text = "%.1f/%.1f".format(now[3].toFloat(), norm[3].toFloat())

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

        binding.sleepCard.setOnClickListener {
            (requireActivity() as MainActivity).showSleepQuiz(shouldSendOnlySleep = true)
        }
    }

    private fun setUpWaterWidget(waterNorm: Int, waterNow: Int){
        binding.textWaterProgress.text = "$waterNow/$waterNorm"
        val water = CPVSection(name = "water", amount = waterNow.toFloat(), color = resources.getColor(R.color.waterBlue))
        binding.progressWater.apply {
            cap = waterNorm.toFloat()
            submitData(listOf(water))
        }
        binding.butGlass250.setOnClickListener {
            addWater(250, waterNorm)
        }
        binding.butGlass350.setOnClickListener {
            addWater(350, waterNorm)
        }
        binding.butGlass450.setOnClickListener {
            addWater(450, waterNorm)
        }
    }

    private fun addWater(amount: Int, norm: Int){
        val now = (localRepository.getPref(PrefsConst.FIELD_MACRO_NOW) as String).split(';')
        val new = now[4].toFloat().toInt() + amount
        (now as ArrayList)[4] = new.toString()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NOW, now.joinToString(";"))
        binding.textWaterProgress.text = "$new/$norm"
        binding.progressWater.addAmount("water", amount.toFloat())
        mainViewModel.addWater(today, token, amount)
    }


    private fun onArticleClick(id: Article) {
        articlesViewModel.getArticle(id._id)
        (requireActivity() as MainActivity).showArticle(id, "dashboard")
    }

}