/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.tfood.navigation

import com.example.login.presentation.view.LoginFlowFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.zzemlyanaya.dashboard.DashboardFragment


object GlobalFlow {

     fun flowLoginFragment() = FragmentScreen { LoginFlowFragment() }
     fun dashboardFragment() = FragmentScreen { DashboardFragment() }

//    // login flow
//    fun loginActivity() = ActivityScreen { Intent(it, LoginActivity::class.java) }
//    fun signIn() = FragmentScreen { SignInFragment() }
//    fun signUp() = FragmentScreen { SignUpFragment() }
//    fun resetPassword() = FragmentScreen { PasswordResetFragment() }
//
//    //main flow
//    fun mainActivity() = ActivityScreen { Intent(it, MainActivity::class.java) }
//    fun achievements() = FragmentScreen { AchievsFragment() }
//    fun basicQuiz() = FragmentScreen { BasicFragment() }
//    fun basicResult() = FragmentScreen { BasicResultFragment() }
//    fun dashboard() = FragmentScreen { DashboardFragment() }
//    fun sleepQuiz(shouldSendOnlySleepData: Boolean) =
//        FragmentScreen { SleepQuizFragment() }
//
//    fun dairy() = FragmentScreen { DairyFragment() }
//    fun statistics() = FragmentScreen { StatisticsFragment() }
//    fun search() = FragmentScreen { SearchFragment() }
//    fun genericInfo() = FragmentScreen { InfoFragment() }
//    fun profile() = FragmentScreen { ProfileFragment() }
//    fun articles() = FragmentScreen { ArticleListFragment() }
//    fun article() = FragmentScreen { ArticleFragment() }
//    fun games() = FragmentScreen { GameListFragment() }
//    fun baseSettings() = FragmentScreen { BaseSettingsFragment() }
//    fun accountSettings() = FragmentScreen { AccountSettingsFragment() }
}