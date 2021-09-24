/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.presentation

import ru.zzemlyanaya.core.network.model.Resource


interface BaseViewWithData {
    fun onLoading()
    fun onError(message: String)
    fun <T> onData(data: T)
    fun <T> handleDataState(resource: Resource<T>)

    fun hideKeyboard()
}