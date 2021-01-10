/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 19:03
 */

package ru.zzemlyanaya.tfood.login.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.databinding.FragmentSignInBinding
import ru.zzemlyanaya.tfood.login.LoginActivity


class SignInFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SignInViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSignInBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        viewModel.signInFormState.observe(viewLifecycleOwner, Observer {
            val signInFormState = it ?: return@Observer

            binding.inputEmail.error = getString(signInFormState.emailError)
            binding.inputPass.error = getString(signInFormState.passwordError)
        })

        binding.butGoogle.setOnClickListener { (requireActivity() as LoginActivity).goOnMain() }
        binding.butSignIn.setOnClickListener {
            //viewModel.authorize()
            (requireActivity() as LoginActivity).goOnMain()
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
                        if(viewModel.signInFormState.value!!.isDataValid)
                            (requireActivity() as LoginActivity).goOnMain()
//                            authorize(
//                                    binding.textEmail.text.toString().hashCode(),
//                                    binding.textPass.text.toString()
//                            )
                }
                false
            }
        }

        return binding.root
    }

    private fun getString(id: Int?): String? {
        return if (id == null) null else getString(id)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SignInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}