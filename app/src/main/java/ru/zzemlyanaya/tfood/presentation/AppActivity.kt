/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:17
 */

package ru.zzemlyanaya.tfood.presentation

import android.os.Bundle
import android.widget.Toast
import ru.zzemlyanaya.core.activity.CoreActivity
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.tfood.databinding.ActivityAppBinding

class AppActivity : CoreActivity() {

    override val mProgress by lazy { LoadingDialog.view(supportFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

    private var lastToast: Toast? = null

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }



}