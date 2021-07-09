/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:14
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
    fun Login() = ActivityScreen { Intent(it, LoginActivity::class.java) }
    fun SignIn() = FragmentScreen { SignInFragment() }
    fun SignUp() = FragmentScreen { SignUpFragment() }
    fun ResetPassword() = FragmentScreen { PasswordResetFragment() }

    //main flow
    fun Main() = ActivityScreen { Intent(it, MainActivity::class.java) }
    fun Achievements() = FragmentScreen { AchievsFragment() }
    fun BasicQuiz() = FragmentScreen { BasicFragment() }
    fun BasicResult() = FragmentScreen { BasicResultFragment() }
    fun Dashboard() = FragmentScreen { DashboardFragment() }
    fun SleepQuiz(shouldSendOnlySleepData: Boolean) =
        FragmentScreen { SleepQuizFragment() }
    fun Dairy() = FragmentScreen { DairyFragment() }
    fun Statistics() = FragmentScreen { StatisticsFragment() }
    fun Search() = FragmentScreen { SearchFragment() }
    fun GenericInfo() = FragmentScreen { InfoFragment() }
    fun Profile() = FragmentScreen { ProfileFragment() }
    fun Articles() = FragmentScreen { ArticleListFragment() }
    fun Article() = FragmentScreen { ArticleFragment() }
    fun Games() = FragmentScreen { GameListFragment() }
    fun BaseSettings() = FragmentScreen { BaseSettingsFragment() }
    fun AccountSettings() = FragmentScreen { AccountSettingsFragment() }
}