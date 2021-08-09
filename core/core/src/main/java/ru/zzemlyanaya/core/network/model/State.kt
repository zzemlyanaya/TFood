/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.model

sealed class State<out T>

object Loading : State<Any>()

data class Error<T>(
    var message: String
) : State<T>()

object Empty : State<Any>()

data class Success<T>(
    var data: T
) : State<T>()