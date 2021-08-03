/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.zzemlyanaya.tfood.LOGOUT
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.ActivityLoginBinding
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.di.Scopes.SESSION_SCOPE
import ru.zzemlyanaya.tfood.di.SessionModule
import ru.zzemlyanaya.tfood.login.passreset.PasswordResetFragment
import ru.zzemlyanaya.tfood.login.signin.IOnLogin
import ru.zzemlyanaya.tfood.login.signin.SignInFragment
import ru.zzemlyanaya.tfood.login.signup.SignUpFragment
import ru.zzemlyanaya.tfood.main.MainActivity
import toothpick.ktp.KTP
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), IOnLogin {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var repository: LocalRepository

    private val navigator = AppNavigator(this, R.id.frame_login)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {

        KTP.openScope(APP_SCOPE).inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLogout = intent.getBooleanExtra(LOGOUT, false)
        if (!isLogout) {
            val token = repository.getPref(PrefsConst.FIELD_USER_TOKEN) as String
            val id = repository.getPref(PrefsConst.FIELD_USER_FINGERPRINT) as String
            if (token != "") {
                KTP.openScopes(APP_SCOPE, SESSION_SCOPE).installModules(SessionModule(token, id))
                goOnMain()
            }
        }
        showSignInFragment()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_login)
        if (!fragment!!.tag.equals("sign_in"))
            showSignInFragment()
        else
            super.onBackPressed()
    }


    private fun showSignInFragment() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, SignInFragment(), "sign_in")
            .commitAllowingStateLoss()
    }

    fun showSignUpFragment() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, SignUpFragment(), "sign_up")
            .commitAllowingStateLoss()
    }

    fun goOnMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showPassResetFragment() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, PasswordResetFragment.newInstance(), "pass_reset")
            .commitAllowingStateLoss()
    }

    override fun onLogin() {
        goOnMain()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}