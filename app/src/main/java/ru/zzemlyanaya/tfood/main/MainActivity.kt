/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 17:29
 */

package ru.zzemlyanaya.tfood.main

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.zzemlyanaya.tfood.FIRST_LAUNCH
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.TOKEN
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.LocalRepository.Companion.PreferencesKeys
import ru.zzemlyanaya.tfood.main.basicquiz.BasicFragment
import ru.zzemlyanaya.tfood.main.dashboard.DashboardFragment
import ru.zzemlyanaya.tfood.main.sleepquiz.SleepQuizFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val localRepository = LocalRepository.getInstance()
    private var token: String? = null

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        token = intent.getStringExtra(TOKEN)
        val firstOverall = intent.getBooleanExtra(FIRST_LAUNCH, true)

        var lastSleepDate = ""
        GlobalScope.launch {
            localRepository
                .getPref(PreferencesKeys.FIELD_SLEEP_DATE)
                .collect { value -> lastSleepDate = value ?: ""}
        }

        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        if (firstOverall) {
            showBasicQuiz(false)
            GlobalScope.launch {
                localRepository.updatePref(PreferencesKeys.FIELD_IS_FIRST_START_OVERALL, false)
            }
        }
        else if (lastSleepDate != today) {
            showSleepQuiz(true)
            GlobalScope.launch {
                localRepository.updatePref(PreferencesKeys.FIELD_SLEEP_DATE, today)
            }
        }
        else
            showDashboard()

    }

    private fun showBasicQuiz(shouldSendData: Boolean){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, BasicFragment.newInstance(shouldSendData, token!!), "basic_quiz")
            .commitAllowingStateLoss()
    }

    fun showDashboard(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, DashboardFragment(), "dashboard")
            .commitAllowingStateLoss()
    }

    fun showSleepQuiz(shouldSendOnlySleep: Boolean){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, SleepQuizFragment.newInstance(shouldSendOnlySleep, token!!), "sleep_quiz")
            .commitAllowingStateLoss()
    }
}