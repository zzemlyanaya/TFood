/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 10.01.2021, 20:03
 */

package ru.zzemlyanaya.tfood.main.basicquiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.zzemlyanaya.tfood.model.User

class BasicQuizViewModel : ViewModel() {
    private val _user = MutableLiveData<User>(User(0, "sd", null, null, null, null, null, null))
    val user: LiveData<User> = _user

    fun update(key: String, value: Any) {
        when(key){
            "name" -> _user.value!!.name = value as String
            "age" -> _user.value!!.age = value as String
            "height" -> _user.value!!.height = value as Int
            "weight" -> _user.value!!.weight = value as Int
            "gk" -> _user.value!!.gk = value as Int
            "gender" -> _user.value!!.gender = value as Int
        }
    }

    fun isDataValid() = ( _user.value?.name == null || _user.value?.age == null || _user.value?.gender == null ||
            _user.value?.height == null || _user.value?.weight == null || _user.value?.gk == null)
}