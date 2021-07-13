/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 15:18
 */

package ru.zzemlyanaya.core.extentions

import timber.log.Timber

fun Any.log(throwable: Throwable, message: String = "") {
    Timber.tag(this.javaClass.simpleName).w(throwable, message)
}

fun Any.log(message: String) {
    Timber.tag(this.javaClass.simpleName).d(message)
}