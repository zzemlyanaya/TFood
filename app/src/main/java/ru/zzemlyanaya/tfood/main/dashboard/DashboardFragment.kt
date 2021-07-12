/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 12.07.2021, 12:03
 */

package ru.zzemlyanaya.tfood.main.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ru.zzemlyanaya.tfood.CONGRATS
import ru.zzemlyanaya.tfood.CongratsTypes
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentDashboardBinding
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.main.MainActivity
import ru.zzemlyanaya.tfood.main.MainViewModel
import ru.zzemlyanaya.tfood.model.Article
import ru.zzemlyanaya.tfood.uikit.circularprogressview.CPVSection
import toothpick.ktp.KTP
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    @Inject
    lateinit var localRepository : LocalRepository
    @Inject @field:Named("token")
    lateinit var token: String
    private var today =  ""

    private val mainViewModel by lazy { ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java) }
    private val articlesViewModel by lazy { ViewModelProviders.of(requireActivity()).get(ArticlesViewModel::class.java) }

    private var congrats = CongratsTypes.NONE

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        articlesViewModel.articleList.observe(viewLifecycleOwner, { list ->
            val lastIndex = if (list.size >= 4) 3 else list.size-1
            (binding.storiesRecyclerView.recycler.adapter as ArticleRecyclerAdapter).update(list.slice(0..lastIndex))
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScopes(APP_SCOPE, Scopes.SESSION_SCOPE).inject(this)
        super.onCreate(savedInstanceState)
        arguments?.let {
            congrats = it.getSerializable(CONGRATS) as CongratsTypes
        }
        today = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        with(binding.storiesRecyclerView.recycler){
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

        with(binding.gamesRecyclerView.recycler){
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

        binding.achievCard.achievCardLayout.setOnClickListener {
            (requireActivity() as MainActivity).showAchievements(
                "dashboard"
            )
        }

        if (congrats != CongratsTypes.NONE)
            showCongrats(congrats)

        binding.butMoreArticles.setOnClickListener {
            (requireActivity() as MainActivity).showArticlesFragment()
        }
        binding.textMoreArticles.setOnClickListener {
            (requireActivity() as MainActivity).showArticlesFragment()
        }

        binding.butMoreGames.setOnClickListener {
            (requireActivity() as MainActivity).showGamesFragment()
        }
        binding.textMoreGames.setOnClickListener {
            (requireActivity() as MainActivity).showGamesFragment()
        }

        return binding.root
    }

    private fun setUpCaloriesWidget(norm: List<String>, now: List<String>, addit: List<String>) {
        binding.nutrientsCard.textKcalGoal.text = norm[0].toDouble().toInt().toString()
        val left = norm[0].toDouble().toInt() - now[0].toDouble().toInt()
        binding.nutrientsCard.textKcalLeft.text = if (left >= 0) left.toString() else "0"
        binding.nutrientsCard.textKcalEaten.text = addit[0]
        binding.nutrientsCard.textKcalBurnt2.text = addit[1]
        binding.nutrientsCard.progressKcal.max = norm[0].toDouble().toInt()
        val animation: ObjectAnimator =
            ObjectAnimator.ofInt(
                binding.nutrientsCard.progressKcal,
                "progress",
                0,
                now[0].toFloat().toInt()
            )
        animation.duration = 2500
        animation.interpolator = DecelerateInterpolator()

        val prots =
            CPVSection(amount = now[1].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.nutrientsCard.progressProts.apply {
            cap = norm[1].toFloat()
            submitData(listOf(prots))
        }
        binding.nutrientsCard.textProts.text =
            "%.1f/%.1f".format(now[1].toFloat(), norm[1].toFloat())

        val fats = CPVSection(amount = now[2].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.nutrientsCard.progressFats.apply {
            cap = norm[2].toFloat()
            submitData(listOf(fats))
        }
        binding.nutrientsCard.textFats.text =
            "%.1f/%.1f".format(now[2].toFloat(), norm[2].toFloat())

        val carbs = CPVSection(amount = now[3].toFloat(), color = resources.getColor(R.color.primaryColour))
        binding.nutrientsCard.progressCarbs.apply {
            cap = norm[3].toFloat()
            submitData(listOf(carbs))
        }
        binding.nutrientsCard.textCarbs.text =
            "%.1f/%.1f".format(now[3].toFloat(), norm[3].toFloat())

        animation.start()
    }

    private fun setUpSleepWidget() {
        val overall = localRepository.getPref(PrefsConst.FIELD_SLEEP_TODAY) as Int
        if (overall > 7 * 60)
            binding.sleepCard.textSleepQuality.text = getString(R.string.good_sleep)
        else
            binding.sleepCard.textSleepQuality.text = getString(R.string.bad_sleep)
        binding.sleepCard.textSleepHours2.text = (overall / 60).toString()
        binding.sleepCard.textSleepMinutes2.text = (overall % 60).toString()

        binding.sleepCard.sleepCardLayout.setOnClickListener {
            (requireActivity() as MainActivity).showSleepQuiz(shouldSendOnlySleep = true)
        }
    }

    private fun setUpWaterWidget(waterNorm: Int, waterNow: Int){
        binding.waterCard.textWaterProgress.text = "$waterNow/$waterNorm"
        val water = CPVSection(name = "water", amount = waterNow.toFloat(), color = resources.getColor(R.color.waterBlue))
        binding.waterCard.progressWater.apply {
            cap = waterNorm.toFloat()
            submitData(listOf(water))
        }
        binding.waterCard.butGlass250.setOnClickListener {
            addWater(250, waterNorm)
        }
        binding.waterCard.butGlass350.setOnClickListener {
            addWater(350, waterNorm)
        }
        binding.waterCard.butGlass450.setOnClickListener {
            addWater(450, waterNorm)
        }
    }

    private fun addWater(amount: Int, norm: Int) {
        val nowList = (localRepository.getPref(PrefsConst.FIELD_MACRO_NOW) as String).split(';')
        val now = nowList[4].toFloat().toInt()
        val new = now + amount
        (nowList as ArrayList)[4] = new.toString()
        localRepository.updatePref(PrefsConst.FIELD_MACRO_NOW, nowList.joinToString(";"))
        binding.waterCard.textWaterProgress.text = "$new/$norm"
        binding.waterCard.progressWater.addAmount("water", amount.toFloat())
        mainViewModel.addWater(today, token, amount)
        if (now < norm && new >= norm)
            showCongrats(CongratsTypes.WATER)
    }

    private fun showCongrats(type: CongratsTypes){
        val num =  Random.nextInt(0, resources.getStringArray(R.array.congrats_array).size)
        binding.congratsTitle.text =  resources.getStringArray(R.array.congrats_array)[num]
        when(type) {
            CongratsTypes.WATER -> {
                binding.scrollView.smoothScrollTo(0, binding.scrollView.bottom)
                binding.waterCard.waterCardLayout.cardElevation = 4f
                setUpButCongrats(R.id.waterCard)
            }
            else -> {
                binding.scrollView.smoothScrollTo(0, 0)
                binding.nutrientsCard.nutrientsCardLayout.cardElevation = 4f
                setUpButCongrats(R.id.nutrientsCard)
            }
        }

        when(type) {
            CongratsTypes.WATER -> binding.congratsBase.text = getString(R.string.water_goal_reached)
            CongratsTypes.CARBS -> binding.congratsBase.text = String.format(
                    getString(R.string.macro_goal_one),
                    getString(R.string.carbs_goal)
                )
            CongratsTypes.FATS -> binding.congratsBase.text = String.format(
                getString(R.string.macro_goal_one),
                getString(R.string.fats_goal)
            )
            CongratsTypes.PROTS -> binding.congratsBase.text = String.format(
                getString(R.string.macro_goal_one),
                getString(R.string.prots_goal)
            )
            CongratsTypes.CF -> binding.congratsBase.text = String.format(
                getString(R.string.macro_goal_two),
                getString(R.string.carbs_goal),
                getString(R.string.fats_goal)
            )
            CongratsTypes.CP -> binding.congratsBase.text = String.format(
                getString(R.string.macro_goal_two),
                getString(R.string.carbs_goal),
                getString(R.string.prots_goal)
            )
            CongratsTypes.FP -> binding.congratsBase.text = String.format(
                getString(R.string.macro_goal_two),
                getString(R.string.fats_goal),
                getString(R.string.prots_goal)
            )
            CongratsTypes.DIET_ALL -> binding.congratsBase.text = getString(R.string.diet_goal_reached)
            CongratsTypes.NONE -> {}
        }

        binding.backShadow.apply {
            visibility = View.VISIBLE
            animate().alpha(0.8f)
            setOnClickListener { hideCongrats(type) }
        }
        binding.congratsGroup.visibility = View.VISIBLE
        binding.butCongrats.setOnClickListener { hideCongrats(type) }
    }

    fun setUpButCongrats(id: Int) {
        val constraintLayout: ConstraintLayout = binding.innerLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.butCongrats,
            ConstraintSet.TOP,
            id,
            ConstraintSet.BOTTOM,
            24
        )
        constraintSet.applyTo(constraintLayout)
    }


    private fun hideCongrats(type: CongratsTypes) {
        binding.backShadow.apply {
            animate().alpha(0f)
            visibility = View.GONE
        }
        binding.congratsGroup.visibility = View.GONE

        when (type) {
            CongratsTypes.WATER -> binding.waterCard.waterCardLayout.cardElevation = 0f
            else -> binding.nutrientsCard.nutrientsCardLayout.cardElevation = 0f
        }
    }

    private fun onArticleClick(id: Article) {
        articlesViewModel.getArticle(id._id)
        (requireActivity() as MainActivity).showArticle(id, "dashboard")
    }

    companion object {
        @JvmStatic
        fun newInstance(congratsType: CongratsTypes = CongratsTypes.NONE) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CONGRATS, congratsType)
                }
            }
    }

}