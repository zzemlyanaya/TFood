/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.01.2021, 19:45
 */

package ru.zzemlyanaya.tfood.login.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.FragmentSignInBinding
import ru.zzemlyanaya.tfood.login.LoginActivity


class SignInFragment : Fragment() {


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

        binding.butGoogle.setOnClickListener { (requireActivity() as LoginActivity).goOnMain() }
        binding.butSignIn.setOnClickListener { (requireActivity() as LoginActivity).goOnMain() }
        binding.textForgotPass.setOnClickListener { (requireActivity() as LoginActivity).showPassResetFragment() }
        binding.textSignUp.setOnClickListener { (requireActivity() as LoginActivity).showSignUpFragment() }

        return binding.root
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