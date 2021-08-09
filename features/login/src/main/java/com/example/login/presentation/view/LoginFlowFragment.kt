/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package com.example.login.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.login.presentation.viewmodel.LoginViewModel
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.fragment.CoreFragment
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView

class LoginFlowFragment : CoreFragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    override val mProgress by lazy { LoadingDialog.view(parentFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.authState.observe(viewLifecycleOwner, {
            handleDataState(it)
        })
    }
}