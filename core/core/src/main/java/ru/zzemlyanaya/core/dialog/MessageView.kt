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
import ru.zzemlyanaya.core.model.MessageEntity

class MessageView(context: Context) : Dialog(context) {
    private var message: MessageEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)
        setCancelable(false)
    }

    fun setMessage(message: MessageEntity) {
        this.message = message
    }
}