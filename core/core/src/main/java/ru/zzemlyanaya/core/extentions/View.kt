/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.07.2021, 14:57
 */

package ru.zzemlyanaya.core.extentions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun EditText.setOnTextChangedListener(listener: ((CharSequence) -> Unit)) {
    val textWatcher = createTextWatcher(
        onTextChanged = { text ->
            if (text != null) {
                listener.invoke(text)
            }
        }
    )
    return addTextChangedListener(
        textWatcher
    )
}

fun createTextWatcher(
    beforeTextChanged: ((CharSequence?) -> Unit)? = null,
    onTextChanged: ((CharSequence?) -> Unit)? = null,
    afterTextChanged: ((CharSequence?) -> Unit)? = null
) = object : TextWatcher {

    override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
        beforeTextChanged?.invoke(text)
    }

    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged?.invoke(text)
    }

    override fun afterTextChanged(text: Editable?) {
        afterTextChanged?.invoke(text)
    }
}

var View.visible: Boolean
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
    get() = visibility == View.VISIBLE

var View.invisible: Boolean
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }
    get() = visibility == View.INVISIBLE