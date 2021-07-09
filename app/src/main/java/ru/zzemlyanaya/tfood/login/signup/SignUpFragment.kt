/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.login.signup

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
import ru.zzemlyanaya.tfood.data.local.LocalRepository
import ru.zzemlyanaya.tfood.data.local.PrefsConst
import ru.zzemlyanaya.tfood.databinding.FragmentSignUpBinding
import ru.zzemlyanaya.tfood.di.Scopes
import ru.zzemlyanaya.tfood.login.signin.IOnLogin
import ru.zzemlyanaya.tfood.model.Status
import toothpick.ktp.KTP
import javax.inject.Inject


class SignUpFragment : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SignUpViewModel::class.java)}
    private lateinit var signupProgressBar: ProgressBar
    private lateinit var butSignUp: Button

    private var onLogin: IOnLogin? = null

    @Inject
    lateinit var repository: LocalRepository

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        KTP.openScope(Scopes.APP_SCOPE).inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSignUpBinding
                = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        signupProgressBar = binding.signupProgressBar
        butSignUp = binding.butCreateNewAccount

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val signUpFormState = it ?: return@Observer

            binding.inputSignupEmail.error = getString(signUpFormState.emailError)
            binding.inputSignupPass.error = getString(signUpFormState.passwordError)
        })

        binding.butCreateNewAccount.setOnClickListener {
            if(viewModel.loginFormState.value!!.isDataValid)
                registr(
                        binding.textEmail.text.toString(),
                        binding.textPass.text.toString()
                )
        }

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
                            registr(
                                    binding.textEmail.text.toString(),
                                    binding.textPass.text.toString()
                            )
                }
                false
            }
        }

        return binding.root
    }

    private fun registr(email: String, password: String){
        viewModel.registr(email, password).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { id ->
                            repository.apply {
                                updatePref(PrefsConst.FIELD_USER_ID, id)
                                updatePref(PrefsConst.FIELD_IS_FIRST_LAUNCH, true)
                            }
                            onLogin?.onLogin()
                        }
                    }
                    Status.ERROR -> {
                        Log.d("--------HERE", it.message.toString())
                        signupProgressBar.visibility = View.GONE
                        butSignUp.visibility = View.VISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Log.d("--------HERE", "LOADING")
                        signupProgressBar.visibility = View.VISIBLE
                        butSignUp.visibility = View.INVISIBLE
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