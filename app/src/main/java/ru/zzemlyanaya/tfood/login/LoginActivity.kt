/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:14
 */

package ru.zzemlyanaya.tfood.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ru.zzemlyanaya.tfood.*
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

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {

        KTP.openScope(APP_SCOPE).inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLogout = intent.getBooleanExtra(LOGOUT, false)
        if(!isLogout) {
            val token = repository.getPref(PrefsConst.FIELD_USER_TOKEN) as String
            val id = repository.getPref(PrefsConst.FIELD_USER_ID) as String
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
            onBackPressedDouble()
    }

    fun onBackPressedDouble() {
        if (backPressedOnce) {
            KTP.closeScope(APP_SCOPE)
            super.onBackPressed()
        }
        else {
            backPressedOnce = true
            Toast.makeText(
                this@LoginActivity,
                "Нажмите ещё раз для выхода",
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed({ backPressedOnce = false }, 2000)
        }
    }


    private fun showSignInFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, SignInFragment(), "sign_in")
            .commitAllowingStateLoss()
    }

    fun showSignUpFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, SignUpFragment(), "sign_up")
            .commitAllowingStateLoss()
    }

    fun goOnMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showPassResetFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, PasswordResetFragment.newInstance(), "pass_reset")
            .commitAllowingStateLoss()
    }

    override fun onLogin() {
        goOnMain()
    }
}