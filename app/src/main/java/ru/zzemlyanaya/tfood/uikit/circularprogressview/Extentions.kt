/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.uikit.circularprogressview

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 */
internal inline fun <T> Iterable<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

internal inline fun <T, R> Iterable<T>.hasDuplicatesBy(selector: (T) -> R): Boolean {
    val registry = mutableListOf<R>()
    forEach {
        val sel = selector(it)
        if (registry.contains(sel)) {
            return true
        }
        registry.add(sel)
    }
    return false
}
fun Float.toRadians() = Math.toRadians(this.toDouble())
