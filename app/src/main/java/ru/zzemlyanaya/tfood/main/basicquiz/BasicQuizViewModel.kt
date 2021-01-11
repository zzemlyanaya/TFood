/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 11.01.2021, 16:42
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.lifecycle.ViewModel
import ru.zzemlyanaya.tfood.model.User
import java.util.*

class BasicQuizViewModel : ViewModel() {
    private val user = User()

    fun update(key: String, value: Any) {
        when(key){
            "name" -> user.username = value as String
            "birthday" -> user.birthdate = value as Date
            "height" -> user.height = value as Int
            "weight" -> user.weight = value as Int
            "chest" -> user.chest = value as Int
            "gender" -> user.gender = value as Boolean
        }
    }

    fun isDataValid() = !( user.username == null || user.birthdate == null || user.gender == null ||
            user.height == null || user.weight == null || user.chest == null)

    fun getData() = user.toString()
}