/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 23:41
 */

package ru.zzemlyanaya.tfood.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.*
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.TOKEN
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.LocalRepository.Companion.FIELD_USER_TOKEN
import ru.zzemlyanaya.tfood.main.basicquiz.BasicFragment
import ru.zzemlyanaya.tfood.main.dashboard.DashboardFragment

class MainActivity : AppCompatActivity() {
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        token = intent.getStringExtra(TOKEN)
        if(token != null)
            GlobalScope.launch {
                LocalRepository.setPref(FIELD_USER_TOKEN, token.orEmpty())
            }
        showBasicQuiz()
    }

    fun showBasicQuiz(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, BasicFragment.newInstance(), "basic_quiz")
            .commitAllowingStateLoss()
    }

    fun showDashboard(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, DashboardFragment(), "dashboard")
            .commitAllowingStateLoss()
    }

    fun showSleepQuiz(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, SleepQuizFragment(), "sleep_quiz")
            .commitAllowingStateLoss()
    }
}