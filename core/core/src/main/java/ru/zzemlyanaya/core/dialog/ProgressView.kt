/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 12:31
 */

package ru.zzemlyanaya.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import ru.zzemlyanaya.core.R

class ProgressView(context: Context) : Dialog(context, R.style.AppTheme_DialogProgress) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
    }
}