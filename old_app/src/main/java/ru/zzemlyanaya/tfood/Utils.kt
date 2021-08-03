/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood

import android.content.Context
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.widget.EditText
import androidx.annotation.StringRes
import java.util.*


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
const val PRODUCT_ID = "product"
const val MEAL = "meal"
const val WHAT_TO_SEARCH = "what_to_search"
const val VALUE = "value"
const val CONGRATS = "congrats"

enum class CongratsTypes { NONE, WATER, DIET_ALL, CARBS, FATS, PROTS, CF, CP, FP }

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

fun Context.getStringByLocale(
    @StringRes stringRes: Int,
    locale: Locale,
    vararg formatArgs: Any
): String {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration).resources.getString(stringRes, *formatArgs)
}

/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Int.dpToPixel(dp: Float, context: Context): Float {
    return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}