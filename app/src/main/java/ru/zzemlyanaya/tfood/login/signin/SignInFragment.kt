/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 17:29
 */

package ru.zzemlyanaya.tfood.login.signin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentSignInBinding
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.model.Status


class SignInFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SignInViewModel::class.java)}
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var butSignIn: Button

    private var onLogin: IOnLogin? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSignInBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        loginProgressBar = binding.loginProgressBar
        butSignIn = binding.butSignIn

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val signInFormState = it ?: return@Observer

            binding.inputEmail.error = getString(signInFormState.emailError)
            binding.inputPass.error = getString(signInFormState.passwordError)
        })

        binding.butGoogle.setOnClickListener { (requireActivity() as LoginActivity).goOnMain() }
        binding.butSignIn.setOnClickListener {
            if(viewModel.loginFormState.value!!.isDataValid)
                login(
                        binding.textEmail.text.toString(),
                        binding.textPass.text.toString()
                )
        }
        binding.textForgotPass.setOnClickListener { (requireActivity() as LoginActivity).showPassResetFragment() }
        binding.textSignUp.setOnClickListener { (requireActivity() as LoginActivity).showSignUpFragment() }

        binding.textEmail.afterTextChanged {
            viewModel.loginDataChanged(
                    binding.textEmail.text.toString(),
                    binding.textPass.text.toString()
            )
        }

        binding.textPass.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                        binding.textEmail.text.toString(),
                        binding.textPass.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if(viewModel.loginFormState.value!!.isDataValid)
                            login(
                                    binding.textEmail.text.toString(),
                                    binding.textPass.text.toString()
                            )
                }
                false
            }
        }

        return binding.root
    }

    private fun login(email: String, password: String){
        viewModel.login(email, password).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Log.d("--------HERE", resource.data.toString())
                        resource.data?.let { token -> onLogin?.onLogin(token, false) }
                    }
                    Status.ERROR -> {
                        Log.d("--------HERE", it.message.toString())
                        loginProgressBar.visibility = View.GONE
                        butSignIn.visibility = View.VISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Log.d("--------HERE", "LOADING")
                        loginProgressBar.visibility = View.VISIBLE
                        butSignIn.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IOnLogin)
            onLogin = context
        else
            throw Exception("Must implement IOnLogin!")
    }

    override fun onDetach() {
        super.onDetach()
        onLogin = null
    }

    private fun getString(id: Int?): String? {
        return if (id == null) null else getString(id)
    }
}

interface IOnLogin {
    fun onLogin(token: String, firstLaunch: Boolean)
}