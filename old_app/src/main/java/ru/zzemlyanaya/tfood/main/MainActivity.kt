/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.*
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.ActivityMainBinding
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.di.Scopes.SESSION_SCOPE
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.main.achievements.AchievsFragment
import ru.zzemlyanaya.tfood.main.basicquiz.BasicFragment
import ru.zzemlyanaya.tfood.main.basicquiz.BasicResultFragment
import ru.zzemlyanaya.tfood.main.dairy.DairyFragment
import ru.zzemlyanaya.tfood.main.dashboard.ArticleFragment
import ru.zzemlyanaya.tfood.main.dashboard.ArticleListFragment
import ru.zzemlyanaya.tfood.main.dashboard.DashboardFragment
import ru.zzemlyanaya.tfood.main.dashboard.GameListFragment
import ru.zzemlyanaya.tfood.main.info.InfoFragment
import ru.zzemlyanaya.tfood.main.profile.ProfileFragment
import ru.zzemlyanaya.tfood.main.search.SearchFragment
import ru.zzemlyanaya.tfood.main.settings.AccountSettingsFragment
import ru.zzemlyanaya.tfood.main.settings.BaseSettingsFragment
import ru.zzemlyanaya.tfood.main.sleepquiz.SleepQuizFragment
import ru.zzemlyanaya.tfood.main.statistics.StatisticsFragment
import ru.zzemlyanaya.tfood.model.Article
import toothpick.ktp.KTP
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var localRepository: LocalRepository

    @Inject
    @field:Named("token")
    lateinit var token: String

    @Inject
    @field:Named("userID")
    lateinit var id: String
//    val token: String by inject("token")
//    val id: String by inject("userID")


    private var backPressedOnce = false

    private lateinit var myFabSrc: Drawable

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onBackPressed() {
        if (binding.fabGroup.isVisible)
            closeFABMenu()
        else {
            val fragment = supportFragmentManager.findFragmentById(R.id.frame_main)
            when (fragment!!.tag) {
                "dashboard", "dairy", "statistics", "profile" -> onBackPressedDouble()
                "base_settings", "about_app", "shop", "achievs_profile" -> showProfile()
                "sleep_quiz_true", "article_dashboard", "achievs_dashboard", "article_list",
                "game_list" -> showDashboard(CongratsTypes.NONE)
                "add_sth" -> {
                    (fragment as SearchFragment).back()
                }
                "info" -> (fragment as InfoFragment).back()
                "account_settings" -> showBaseSettings()
                else -> {
                }
            }
        }
    }

    private fun onBackPressedDouble() {
        if (backPressedOnce)
            logout()
        else {
            backPressedOnce = true
            Toast.makeText(
                this@MainActivity,
                "Нажмите ещё раз для выхода",
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({ backPressedOnce = false }, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScopes(APP_SCOPE, SESSION_SCOPE).inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBarNav.apply {
            onItemSelectedListener = { _, menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> showDashboard(CongratsTypes.NONE)
                    R.id.item_dairy -> showDairy()
                    R.id.item_profile -> showProfile()
                    R.id.item_statistics -> showStatistics()
                }
            }
            onItemReselectedListener = { _, _ -> }
        }


        myFabSrc = getDrawable(R.drawable.ic_add_black)!!

        val firstOverall = localRepository.getPref(PrefsConst.FIELD_IS_FIRST_LAUNCH) as Boolean
        val lastSleepDate = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String
        val today = SimpleDateFormat("yyyy-M-dd", Locale.getDefault()).format(Date())

        if (token == "" || firstOverall) {
            showBasicQuiz(false)
            localRepository.updatePref(PrefsConst.FIELD_IS_FIRST_LAUNCH, false)
            localRepository.updatePref(PrefsConst.FIELD_LAST_SLEEP_DATE, today)
        } else if (lastSleepDate != today) {
            viewModel.getOrCreateDay(token, today)
            showSleepQuiz(true)
            localRepository.apply {
                updatePref(PrefsConst.FIELD_LAST_SLEEP_DATE, today)
                updatePref(PrefsConst.FIELD_MACRO_NOW, "0;0;0;0;0")
                updatePref(PrefsConst.FIELD_USER_NOW, "0;0")
            }
        } else
            showDashboard(CongratsTypes.NONE)

        setUpFabs()
        binding.fab.setOnClickListener {
            val tag = supportFragmentManager.findFragmentById(R.id.frame_main)?.tag
            if (tag == "dashboard" || tag == "dairy") {
                if (View.GONE == binding.fabGroup.visibility)
                    showFABMenu()
                else
                    closeFABMenu()
            }
        }

    }


    private fun showBasicQuiz(shouldSendData: Boolean) {
        binding.bottomBarNav.visibility = View.GONE
        binding.fab.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.frame_main, BasicFragment.newInstance(shouldSendData, id), "basic_quiz")
            .commitAllowingStateLoss()
    }

    fun showDashboard(congratsType: CongratsTypes) {
        binding.bottomBarNav.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, DashboardFragment.newInstance(congratsType), "dashboard")
            .commitAllowingStateLoss()
    }

    fun showSleepQuiz(shouldSendOnlySleep: Boolean) {
        binding.bottomBarNav.visibility = View.GONE
        binding.fab.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(
                R.id.frame_main,
                SleepQuizFragment.newInstance(shouldSendOnlySleep, token),
                "sleep_quiz_$shouldSendOnlySleep"
            )
            .commitAllowingStateLoss()
    }

    fun showBasicResult(weightVal: Int, border: Float, kcalNorm: Int, waterNorm: Int) {
        val weight =
            (localRepository.getPref(PrefsConst.FIELD_USER_DATA) as String).split(';')[3].toIntOrNull()
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(
                R.id.frame_main, BasicResultFragment.newInstance(
                    weightVal,
                    border,
                    weight ?: 0,
                    kcalNorm,
                    waterNorm
                ), "basic_result"
            )
            .commitAllowingStateLoss()
    }

    fun showDairy() {
        binding.fab.visibility = View.VISIBLE
        binding.bottomBarNav.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, DairyFragment(), "dairy")
            .commitAllowingStateLoss()
    }

    private fun setUpFabs() {
        binding.fabDinner.setOnClickListener { showAddSth(R.string.dinner, "product") }
        binding.fabLunch.setOnClickListener { showAddSth(R.string.lunch, "product") }
        binding.fabBreakfast.setOnClickListener { showAddSth(R.string.breakfast, "product") }
        binding.fabSnack.setOnClickListener { showAddSth(R.string.snack, "product") }
        binding.fabSport.setOnClickListener { showAddSth(R.string.sport, "sport") }
        binding.fabChores.setOnClickListener { showAddSth(R.string.chores, "housework") }
    }

    private fun showFABMenu() {
        binding.fabGroup.visibility = View.VISIBLE
        val willBeOrange = myFabSrc.constantState!!.newDrawable()
        willBeOrange.mutate()
            .setColorFilter(getColor(R.color.primaryColour), PorterDuff.Mode.SRC_ATOP)
        binding.fab.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            setImageDrawable(willBeOrange)
        }
        binding.backShadow.animate().alpha(0.45f)
        binding.fab.animate().rotationBy(45F)
        binding.fabDinnerLayout.animate().alpha(1F)
        binding.fabLunchLayout.animate().alpha(1F)
        binding.fabBreakfastLayout.animate().alpha(1F)
        binding.fabSnackLayout.animate().alpha(1F)
        binding.fabSportLayout.animate().alpha(1F)
        binding.fabChoresLayout.animate().alpha(1F)
    }

    private fun closeFABMenu() {
        binding.fab.animate().rotation(0f)
        val willBeWhite = myFabSrc.constantState!!.newDrawable()
        willBeWhite.mutate().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.fab.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.primaryColour))
            setImageDrawable(willBeWhite)
        }
        binding.backShadow.animate().alpha(0f)
        binding.fabDinnerLayout.animate().alpha(0f)
        binding.fabLunchLayout.animate().alpha(0f)
        binding.fabBreakfastLayout.animate().alpha(0f)
        binding.fabSnackLayout.animate().alpha(0f)
        binding.fabSportLayout.animate().alpha(0f)
        binding.fabChoresLayout.animate().alpha(0f)
        binding.fabGroup.visibility = View.GONE
    }

    fun showStatistics() {
        binding.fab.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, StatisticsFragment(), "statistics")
            .commitAllowingStateLoss()
    }

    fun showProfile() {
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, ProfileFragment(), "profile")
            .commitAllowingStateLoss()
    }

    fun showAddSth(title_res: Int, whatToAdd: String) {
        closeFABMenu()
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, SearchFragment.newInstance(title_res, whatToAdd), "add_sth")
            .commitAllowingStateLoss()
    }

    fun showAddNewProduct() {
//        supportFragmentManager.beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .add(R.id.frame_main, AddNewProductFragment(), "add_new_product")
//                .commitAllowingStateLoss()
    }

    fun showInfo(id: String, whatToShow: String, title_res: Int) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, InfoFragment.newInstance(id, whatToShow, title_res), "info")
            .commitAllowingStateLoss()
    }

    fun showBaseSettings() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, BaseSettingsFragment(), "base_settings")
            .commitAllowingStateLoss()
    }

    fun showAccountSettings() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, AccountSettingsFragment(), "account_settings")
            .commitAllowingStateLoss()
    }

    fun showArticle(article: Article, from: String) {
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, ArticleFragment.newInstance(article), "article_$from")
            .commitAllowingStateLoss()
    }

    fun showArticlesFragment() {
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, ArticleListFragment(), "article_list")
            .commitAllowingStateLoss()
    }

    fun showGamesFragment() {
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, GameListFragment(), "game_list")
            .commitAllowingStateLoss()
    }

    fun showAchievements(from: String) {
        binding.fab.visibility = View.GONE
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frame_main, AchievsFragment(), "achievs_$from")
            .commitAllowingStateLoss()
    }

    fun logout() {
        //remoteRepository.logout(getStandardHeader(token))
        localRepository.apply {
            updatePref(PrefsConst.FIELD_USER_DATA, "Username;2006-1-1;180;65;85")
            updatePref(PrefsConst.FIELD_MACRO_NOW, "0;0;0;0;0")
            updatePref(PrefsConst.FIELD_USER_NOW, "0;0")
            updatePref(PrefsConst.FIELD_USER_ID, "")
            updatePref(PrefsConst.FIELD_USER_TOKEN, "")
            updatePref(PrefsConst.FIELD_SLEEP_TODAY, 0)
            updatePref(PrefsConst.FIELD_LAST_SLEEP_DATE, "")
        }
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra(LOGOUT, true)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        KTP.closeScope(SESSION_SCOPE)
        super.onDestroy()
    }
}