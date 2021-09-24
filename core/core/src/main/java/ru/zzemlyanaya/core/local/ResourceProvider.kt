/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 24.09.2021, 15:53
 */

package ru.zzemlyanaya.core.local

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ru.zzemlyanaya.core.R
import java.lang.ref.WeakReference

/**
 * An interface meant to provide access to a few of Android's most commonly used [Context] methods
 * without the need to reference a [Context] object directly. E.g. you may want to access resolved
 * Android resources in a view model without any direct dependency on [Context].
 */
interface ResourceProvider {

    fun getString(@StringRes id: Int): String
    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String

    @ColorInt
    fun getColor(@ColorRes id: Int): Int

    fun getDrawable(@DrawableRes id: Int): Drawable?

    fun getErrorText(code: Int): String
}

/**
 * Spits out a [ResourceProvider] implementation given a [Context]. Holds a [WeakReference] to the
 * given [Context] to help with GC.
 */
fun createResourceProvider(rawContext: Context): ResourceProvider = object : ResourceProvider {

    private val weakContext = WeakReference(rawContext)
    private val context: Context
        get() = weakContext.get()!!

    override fun getString(@StringRes id: Int): String = context.getString(id)
    override fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return context.getString(id, *formatArgs)
    }

    @ColorInt
    override fun getColor(@ColorRes id: Int): Int = ContextCompat.getColor(context, id)

    override fun getDrawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(context, id)

    override fun getErrorText(code: Int): String {
        val res = when(code) {
            401 -> R.string.error_backend_401
            403 -> R.string.error_backend_403
            404 -> R.string.error_backend_404
            406 -> R.string.error_backend_406
            500 -> R.string.error_backend_500
            502 -> R.string.error_backend_502
            503 -> R.string.error_backend_503
            504 -> R.string.error_backend_504
            else -> R.string.error_unknown
        }

        return getString(res)
    }
}

/**
 * A computed property that returns the result of [createResourceProvider].
 *
 * @see createResourceProvider
 */
val Context.asResourceProvider: ResourceProvider
    get() = createResourceProvider(this)