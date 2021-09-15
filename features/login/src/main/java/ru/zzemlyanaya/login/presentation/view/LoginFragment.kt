/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.login.presentation.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.example.login.R
import com.example.login.databinding.FmtLoginBinding
import ru.zzemlyanaya.core.di.Scopes.AUTH_FLOW_SCOPE
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.extentions.getNullableString
import ru.zzemlyanaya.core.extentions.setOnTextChangedListener
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.login.presentation.viewmodel.LoginViewModel
import toothpick.ktp.KTP
import javax.inject.Inject

class LoginFragment : CoreFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    private lateinit var binding: FmtLoginBinding

    override val mProgress by lazy { LoadingDialog.view(parentFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var repository: LocalRepository

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        KTP.openScopes(AUTH_FLOW_SCOPE, this)
            .inject(this)
            .also { KTP.closeScope(this) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fmt_login, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val signInFormState = it ?: return@Observer

            binding.inputEmail.error = getNullableString(signInFormState.emailError)
            binding.inputPass.error = getNullableString(signInFormState.passwordError)
        })
    }

    private fun setupListeners() {
        binding.butSignIn.setOnClickListener { login() }

        binding.textForgotPass.setOnClickListener {
            viewModel.navigatePassReset()
        }
        binding.textSignUp.setOnClickListener {
            viewModel.navigateSignUp()
        }

        binding.butGoogle.setOnClickListener {
            showToast(R.string.component_in_progress)
        }

        binding.textEmail.setOnTextChangedListener {
            viewModel.loginDataChanged(
                binding.textEmail.text.toString(),
                binding.textPass.text.toString()
            )
        }

        binding.textPass.apply {
            setOnTextChangedListener {
                viewModel.loginDataChanged(
                    binding.textEmail.text.toString(),
                    binding.textPass.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (viewModel.loginFormState.value!!.isDataValid) login()
                    else -> {
                    }
                }
                false
            }
        }

    }

    private fun login() {
        hideKeyboard()

        viewModel.authState.observe(viewLifecycleOwner, {
            handleDataState(it)
        })
        viewModel.login(binding.textEmail.text.toString(), binding.textPass.text.toString())
    }

}