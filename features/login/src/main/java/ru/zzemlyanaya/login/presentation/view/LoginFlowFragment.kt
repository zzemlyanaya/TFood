/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 15:05
 */

package ru.zzemlyanaya.login.presentation.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.login.R
import ru.zzemlyanaya.core.di.Scopes.ACTIVITY_MAIN_SCOPE
import ru.zzemlyanaya.core.di.Scopes.AUTH_FLOW_SCOPE
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.login.di.LoginModule
import ru.zzemlyanaya.login.presentation.viewmodel.LoginViewModel
import toothpick.ktp.KTP

class LoginFlowFragment : CoreFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    override val mProgress by lazy { LoadingDialog.view(parentFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        KTP.openScopes(ACTIVITY_MAIN_SCOPE, AUTH_FLOW_SCOPE)
                .installModules(LoginModule())
                .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fmt_empty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authState.observe(viewLifecycleOwner, {
            handleDataState(it)
        })
        viewModel.checkIfAutoLogin()
    }

}