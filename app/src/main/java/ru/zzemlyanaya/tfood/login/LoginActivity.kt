/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 23:41
 */

package ru.zzemlyanaya.tfood.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.TOKEN
import ru.zzemlyanaya.tfood.databinding.ActivityLoginBinding
import ru.zzemlyanaya.tfood.login.passreset.PasswordResetFragment
import ru.zzemlyanaya.tfood.login.signin.IOnLogin
import ru.zzemlyanaya.tfood.login.signin.SignInFragment
import ru.zzemlyanaya.tfood.login.signup.SignUpFragment
import ru.zzemlyanaya.tfood.main.MainActivity

class LoginActivity : AppCompatActivity(), IOnLogin {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showSignInFragment()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_login)
        if (!fragment!!.tag.equals("sign_in"))
            showSignInFragment()
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

    override fun onLogin(token: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(TOKEN, token)
        startActivity(intent)
        finish()
    }

}