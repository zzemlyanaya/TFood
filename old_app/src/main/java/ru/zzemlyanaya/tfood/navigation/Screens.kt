/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.navigation

import android.content.Intent
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.login.passreset.PasswordResetFragment
import ru.zzemlyanaya.tfood.login.signin.SignInFragment
import ru.zzemlyanaya.tfood.login.signup.SignUpFragment
import ru.zzemlyanaya.tfood.main.MainActivity
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

object Screens {
    // login flow
    fun loginActivity() = ActivityScreen { Intent(it, LoginActivity::class.java) }
    fun signIn() = FragmentScreen { SignInFragment() }
    fun signUp() = FragmentScreen { SignUpFragment() }
    fun resetPassword() = FragmentScreen { PasswordResetFragment() }

    //main flow
    fun mainActivity() = ActivityScreen { Intent(it, MainActivity::class.java) }
    fun achievements() = FragmentScreen { AchievsFragment() }
    fun basicQuiz() = FragmentScreen { BasicFragment() }
    fun basicResult() = FragmentScreen { BasicResultFragment() }
    fun dashboard() = FragmentScreen { DashboardFragment() }
    fun sleepQuiz(shouldSendOnlySleepData: Boolean) =
        FragmentScreen { SleepQuizFragment() }

    fun dairy() = FragmentScreen { DairyFragment() }
    fun statistics() = FragmentScreen { StatisticsFragment() }
    fun search() = FragmentScreen { SearchFragment() }
    fun genericInfo() = FragmentScreen { InfoFragment() }
    fun profile() = FragmentScreen { ProfileFragment() }
    fun articles() = FragmentScreen { ArticleListFragment() }
    fun article() = FragmentScreen { ArticleFragment() }
    fun games() = FragmentScreen { GameListFragment() }
    fun baseSettings() = FragmentScreen { BaseSettingsFragment() }
    fun accountSettings() = FragmentScreen { AccountSettingsFragment() }
}