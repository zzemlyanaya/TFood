/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 20.08.2021, 12:43
 */

package ru.zzemlyanaya.core.extentions

import androidx.fragment.app.Fragment

fun Fragment.getNullableString(id: Int?): String? {
    return if (id == null) null else getString(id)
}