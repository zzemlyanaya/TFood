/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package com.example.login.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProviders
import com.example.login.R
import com.example.login.presentation.viewmodel.LoginViewModel
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.core.local.LocalRepository
import javax.inject.Inject

class LoginFragment : CoreFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    private lateinit var loginProgressBar: ProgressBar
    private lateinit var butSignIn: Button

    @Inject
    lateinit var repository: LocalRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

}