/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:16
 */

package ru.zzemlyanaya.core.presentation

import ru.zzemlyanaya.core.api.model.State

interface BaseViewWithData {
    fun onLoading()
    fun onEmpty()
    fun onError(message: String)
    fun <T> onData(data: T)
    fun <T> handleDataState(state: State)

    fun hideKeyboard()
}