/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 17.01.2021, 12:41
 */

package ru.zzemlyanaya.tfood.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import ru.zzemlyanaya.tfood.LOGOUT
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.ActivityMainBinding
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.main.basicquiz.BasicFragment
import ru.zzemlyanaya.tfood.main.dashboard.DashboardFragment
import ru.zzemlyanaya.tfood.main.sleepquiz.SleepQuizFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val localRepository = LocalRepository.getInstance()
    private lateinit var token: String
    private lateinit var id: String

    private var backPressedOnce = false

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_main)
        when(fragment!!.tag) {
            "dashboard" -> onBackPressedDouble()
            else -> {}
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomBar: ExpandableBottomBar = binding.bottomBarNav

        bottomBar.onItemSelectedListener = { _, menuItem ->
            when(menuItem.itemId) {
                R.id.item_home -> showDashboard()
                R.id.item_dairy -> showDairy()
                R.id.item_profile -> showProfile()
                R.id.item_statistics -> showStatistics()
            }
        }

        bottomBar.onItemReselectedListener = { _, _ -> }

        val firstOverall = localRepository.getPref(PrefsConst.FIELD_IS_FIRST_LAUNCH) as Boolean
        token = localRepository.getPref(PrefsConst.FIELD_USER_TOKEN) as String
        id = localRepository.getPref(PrefsConst.FIELD_USER_ID) as String

        val lastSleepDate = localRepository.getPref(PrefsConst.FIELD_LAST_SLEEP_DATE) as String

        val today = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        if (firstOverall || token == "") {
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
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, BasicFragment.newInstance(shouldSendData, id), "basic_quiz")
            .commitAllowingStateLoss()
    }

    fun showDashboard(){
        binding.bottomBarNav.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, DashboardFragment(), "dashboard")
            .commitAllowingStateLoss()
    }

    fun showSleepQuiz(shouldSendOnlySleep: Boolean){
        binding.bottomBarNav.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_main, SleepQuizFragment.newInstance(shouldSendOnlySleep, token), "sleep_quiz")
            .commitAllowingStateLoss()
    }

    fun showDairy() {

    }

    fun showStatistics() {

    }

    fun showProfile() {

    }

    fun logout(){
        //TODO logout on server
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra(LOGOUT, true)
        }
        startActivity(intent)
        finish()
    }
}