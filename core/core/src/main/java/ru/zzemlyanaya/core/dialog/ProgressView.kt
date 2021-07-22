/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.07.2021, 11:38
 */

package ru.zzemlyanaya.core.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import ru.zzemlyanaya.core.R

class ProgressView(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
    }
}