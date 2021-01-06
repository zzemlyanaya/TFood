package ru.zzemlyanaya.tfood.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.login.signin.SignInFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        showSignInFragment()
    }

    private fun showSignInFragment(){
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.frame_login, SignInFragment(), "sign_in")
            .commitAllowingStateLoss()
    }
}