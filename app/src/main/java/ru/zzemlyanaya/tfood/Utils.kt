/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 17:23
 */

package ru.zzemlyanaya.tfood

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

const val USER = "user"
const val TOKEN = "token"
const val SHOULD_SEND_ONLY_SLEEP = "should send only sleep"
const val SHOULD_SEND_DATA = "should send data"
const val FIRST_LAUNCH = "first launch"

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}