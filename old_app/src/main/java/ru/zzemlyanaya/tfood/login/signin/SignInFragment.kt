/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.login.signin

import android.content.Context
import android.os.Bundle
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
import ru.zzemlyanaya.core.api.model.*
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.afterTextChanged
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentSignInBinding
import ru.zzemlyanaya.tfood.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.tfood.di.Scopes.SESSION_SCOPE
import ru.zzemlyanaya.tfood.di.SessionModule
import ru.zzemlyanaya.tfood.login.LoginActivity
import ru.zzemlyanaya.tfood.model.Status
import ru.zzemlyanaya.tfood.model.TokenPair
import ru.zzemlyanaya.tfood.model.User
import toothpick.ktp.KTP
import javax.inject.Inject


class SignInFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SignInViewModel::class.java) }
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var butSignIn: Button

    private var onLogin: IOnLogin? = null

    @Inject
    lateinit var repository: LocalRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        KTP.openScope(APP_SCOPE).inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSignInBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        loginProgressBar = binding.loginProgressBar
        butSignIn = binding.butSignIn

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val signInFormState = it ?: return@Observer

            binding.inputEmail.error = getString(signInFormState.emailError)
            binding.inputPass.error = getString(signInFormState.passwordError)
        })

        binding.butGoogle.setOnClickListener {
            Toast
                .makeText(
                    requireContext(),
                    getString(R.string.component_in_progress),
                    Toast.LENGTH_SHORT
                )
                .show()
        }
        binding.butSignIn.setOnClickListener {
            if (viewModel.loginFormState.value!!.isDataValid)
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
                        if (viewModel.loginFormState.value!!.isDataValid)
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

    private fun login(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner, {
            when (it) {
                is Success<*> -> {
                    val data = it.data as TokenPair
                    KTP.openScopes(APP_SCOPE, SESSION_SCOPE)
                        .installModules(SessionModule(data.accessToken, data.refreshToken))
                    repository.apply {
                        updatePref(PrefsConst.FIELD_IS_FIRST_LAUNCH, false)
                        updatePref(PrefsConst.FIELD_USER_FINGERPRINT, "fixme")
                    }
                    onLogin?.onLogin()
                }
                is Error<*> -> {
                    loginProgressBar.visibility = View.GONE
                    butSignIn.visibility = View.VISIBLE
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                is Loading -> {
                    loginProgressBar.visibility = View.VISIBLE
                    butSignIn.visibility = View.INVISIBLE
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
    fun onLogin()
}