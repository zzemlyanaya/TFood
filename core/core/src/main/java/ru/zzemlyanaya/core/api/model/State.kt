/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 03.08.2021, 14:16
 */

package ru.zzemlyanaya.core.api.model

sealed class State

object Loading : State()

data class Error(
    var status: String,
    var message: String
) : State()

object Empty : State()

data class Success<T>(
    var data: T
) : State()