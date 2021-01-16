/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.01.2021, 14:08
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonObject
import ru.zzemlyanaya.tfood.App
import ru.zzemlyanaya.tfood.model.BasicQuizResult
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.SleepQuizResult
import ru.zzemlyanaya.tfood.model.User

class RemoteRepository {
    private var service = App.api

    fun createAccount(email: String, password: String): Result<String>{
        val data = JsonObject()
        data.addProperty("email", email)
        data.addProperty("password", password)
        val res = service.createAccount(data)
        val id = res.get("_id")
        val error = res.get("error")
        return if (id == null) {
            if (error != null)
                Result(data = null, error = error.asString)
            else
                Result(data = null, error = "Unsuccessful sign up")
        }
        else
            Result(data = id.asString, error = null)
    }

    fun login(email: String, password: String) : Result<String> {
        val data = JsonObject()
        data.addProperty("email", email)
        data.addProperty("password", password)
        val res = service.login(data).get("token")
        return if (res == null)
            Result(data = null, error = "Unsuccessful login")
        else
            Result(data = res.asString, error = null)
    }

    fun logout(headers: Map<String, String>): Result<String> {
        val res = service.logout(headers)
        val error = res.get("error")
        return if (error != null)
            Result(data = null, error = error.asString)
        else
            Result(data = res.get("message").asString, error = null)
    }

    fun addUserData(user: User, sleep: Double) : Result<BasicQuizResult> {
        val data = JsonObject().apply {
            addProperty("_id", user._id)
            addProperty("birthdate", user.birthdate)
            addProperty("weight", user.weight)
            addProperty("height", user.height)
            addProperty("chest", user.chest)
            addProperty("gender", user.gender)
            addProperty("sleep", sleep)
        }
        return service.addUserData(data)
    }

    fun addSleepData(headers: Map<String, String>, sleep: Double): Result<SleepQuizResult> {
        val data = JsonObject().apply {
            addProperty("sleep", sleep)
        }
        return service.addSleepData(headers, data)
    }
}