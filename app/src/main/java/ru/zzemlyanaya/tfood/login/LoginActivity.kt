/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:24
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
import ru.zzemlyanaya.tfood.login.passreset.PasswordResetFragment
import ru.zzemlyanaya.tfood.login.signin.IOnLogin
import ru.zzemlyanaya.tfood.login.signin.SignInFragment
import ru.zzemlyanaya.tfood.login.signup.SignUpFragment
import ru.zzemlyanaya.tfood.main.MainActivity

class LoginActivity : AppCompatActivity(), IOnLogin {
    private lateinit var binding: ActivityLoginBinding
    private val localRepository = LocalRepository.getInstance()

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isLogout = intent.getBooleanExtra(LOGOUT, false)
        if(!isLogout) {
            val token = localRepository.getPref(PrefsConst.FIELD_USER_TOKEN)
            if (token != "")
                goOnMain()
        }
        else
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
        if (backPressedOnce)
            finish()
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