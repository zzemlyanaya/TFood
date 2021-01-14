/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 23:41
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.JsonObject
import ru.zzemlyanaya.tfood.App
import ru.zzemlyanaya.tfood.model.Result
import ru.zzemlyanaya.tfood.model.User

class RemoteRepository {
    private var service = App.api

    fun createAccount(email: String, password: String): Result<String>{
        val data = JsonObject()
        data.addProperty("email", email)
        data.addProperty("password", password)
        val res = service.createAccount(data)
        val token = res.get("token")
        return if (token == null)
            Result(data = null, error = "Unsuccessful sign up")
        else
            Result(data = token.asString, error = null)
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

    fun logout() = service.logout()

    fun addUserData(headers: Map<String, String>, user: User
    ) : Result<String> {
        val data = JsonObject()
        data.apply {
            addProperty("_id", user._id)
            addProperty("birthdate", user.birthdate)
            addProperty("weight", user.weight)
            addProperty("height", user.height)
            addProperty("chest", user.chest)
            addProperty("gender", user.gender)
        }
        val res = service.addUserData(headers, data)
        val mes = res.get("message")
        return if (mes == null)
            Result(data = null, error = res.get("error").asString)
        else
            Result(data = mes.asString, error = null)
    }
}