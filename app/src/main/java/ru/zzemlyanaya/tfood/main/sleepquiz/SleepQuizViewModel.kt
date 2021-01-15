/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 15.01.2021, 12:46
 */

package ru.zzemlyanaya.tfood.main.sleepquiz

import androidx.lifecycle.ViewModel
import ru.zzemlyanaya.tfood.data.remote.RemoteRepository

class SleepQuizViewModel : ViewModel() {
    val repository = RemoteRepository()

    fun sendSleep(token: String, sleep: Int){
        //TODO send data to the server
    }
}