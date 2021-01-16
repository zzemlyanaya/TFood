/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 12:23
 */

package ru.zzemlyanaya.tfood.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.main.basicquiz.BasicFragment
import ru.zzemlyanaya.tfood.main.dashboard.DashboardFragment
import ru.zzemlyanaya.tfood.main.sleepquiz.SleepQuizFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val localRepository = LocalRepository.getInstance()
    private lateinit var token: String
    private lateinit var id: String

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_main)
        when(fragment!!.tag) {
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstOverall = localRepository.getPref(PrefsConst.FIELD_IS_FIRST_LAUNCH) as Boolean
        token = localRepository.getPref(PrefsConst.FIELD_USER_TOKEN) as String
        id = localRepository.getPref(PrefsConst.FIELD_USER_ID) as String

        val lastSleepDate = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String

        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        if (firstOverall) {
            showBasicQuiz(false)
            localRepository.updatePref(PrefsConst.FIELD_IS_FIRST_LAUNCH, false)
        }
        else if (lastSleepDate != today) {
            showSleepQuiz(true)
            localRepository.updatePref(PrefsConst.FIELD_LAST_SLEEP_DATE, today)
        }
        else
            showDashboard()

    }

    private fun showBasicQuiz(shouldSendData: Boolean){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, BasicFragment.newInstance(shouldSendData, id), "basic_quiz")
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
            .replace(R.id.frame_main, SleepQuizFragment.newInstance(shouldSendOnlySleep, token), "sleep_quiz")
            .commitAllowingStateLoss()
    }

    fun logout(){
        //TODO logout on server
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        killActivity()
    }

    private fun killActivity() {
        finish()
    }
}