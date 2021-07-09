/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.model

import java.io.Serializable

data class Article(
        var _id: String = "",
        var title: String = "",
        var body: String = "",
        var preview: String = ""
) : Serializable