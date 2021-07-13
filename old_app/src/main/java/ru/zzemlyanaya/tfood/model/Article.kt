/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 13:06
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class Article(
    var _id: String = "",
    var title: String = "",
    var body: String = "",
    var preview: String = ""
) : Serializable