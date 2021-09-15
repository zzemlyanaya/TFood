/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.login.presentation.view

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
import com.example.login.R
import com.example.login.databinding.FmtSignUpBinding
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.login.presentation.viewmodel.SignUpViewModel


class SignUpFragment : CoreFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SignUpViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FmtSignUpBinding =
            DataBindingUtil.inflate(inflater, R.layout.fmt_sign_up, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

//        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
//            val signUpFormState = it ?: return@Observer
//
//            binding.inputSignupEmail.error = getString(signUpFormState.emailError)
//            binding.inputSignupPass.error = getString(signUpFormState.passwordError)
//        })
//
//        binding.butCreateNewAccount.setOnClickListener {
//            if (viewModel.loginFormState.value!!.isDataValid)
//                registr(
//                    binding.textEmail.text.toString(),
//                    binding.textPass.text.toString()
//                )
//        }
//
//        binding.textEmail.afterTextChanged {
//            viewModel.loginDataChanged(
//                binding.textEmail.text.toString(),
//                binding.textPass.text.toString()
//            )
//        }
//
//        binding.textPass.apply {
//            afterTextChanged {
//                viewModel.loginDataChanged(
//                    binding.textEmail.text.toString(),
//                    binding.textPass.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        if (viewModel.loginFormState.value!!.isDataValid)
//                            registr(
//                                binding.textEmail.text.toString(),
//                                binding.textPass.text.toString()
//                            )
//                }
//                false
//            }
//        }

        return binding.root
    }

//    private fun registr(email: String, password: String) {
//        viewModel.registr(email, password).observe(viewLifecycleOwner, {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { id ->
//                            repository.apply {
//                                updatePref(PrefsConst.FIELD_USER_FINGERPRINT, id)
//                                updatePref(PrefsConst.FIELD_IS_FIRST_LAUNCH, true)
//                            }
//                            onLogin?.onLogin()
//                        }
//                    }
//                    Status.ERROR -> {
//                        Log.d("--------HERE", it.message.toString())
//                        signupProgressBar.visibility = View.GONE
//                        butSignUp.visibility = View.VISIBLE
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                    }
//                    Status.LOADING -> {
//                        Log.d("--------HERE", "LOADING")
//                        signupProgressBar.visibility = View.VISIBLE
//                        butSignUp.visibility = View.INVISIBLE
//                    }
//                }
//            }
//        })
//    }

}