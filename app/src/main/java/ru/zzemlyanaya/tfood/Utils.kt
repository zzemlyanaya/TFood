/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 22.01.2021, 18:50
 */

package ru.zzemlyanaya.tfood

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

const val USER = "user"
const val TOKEN = "token"
const val ID = "id"
const val SHOULD_SEND_ONLY_SLEEP = "should send only sleep"
const val SHOULD_SEND_DATA = "should send data"
const val FIRST_LAUNCH = "first launch"
const val LOGOUT = "logout"
const val DEBUG_TAG = "-----------HERE"
const val WEIGHT_VAL = "weight_value"
const val KCAL_NORM = "kcal_norm"
const val WATER_NORM = "water_norm"
const val BORDER_WEIGHT = "border_weight"
const val WEIGHT = "weight"
const val PRODUCT = "product"
const val MEAL = "meal"

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

fun getStandardHeader(token: String) = mapOf("Authorization" to "Bearer $token")