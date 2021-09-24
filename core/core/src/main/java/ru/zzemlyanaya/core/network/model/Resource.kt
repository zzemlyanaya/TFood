/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 16:21
 */

package ru.zzemlyanaya.core.network.model

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T? = null, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T? = null): Resource<T> = Resource(status = Status.LOADING, data = data, message = null)
    }
}