/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 27.01.2021, 13:39
 */

package ru.zzemlyanaya.tfood.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import ru.zzemlyanaya.tfood.App
import ru.zzemlyanaya.tfood.DEBUG_TAG
import ru.zzemlyanaya.tfood.model.*


class RemoteRepository {
    private var service = App.api
    private var gson = GsonBuilder()
            .setPrettyPrinting()
            .create()


    // /accounts

    fun createAccount(email: String, password: String): Result<String>{
        val data = JsonObject()
        data.addProperty("email", email)
        data.addProperty("password", password)
        val res = service.createAccount(data)
        val id = res.get("_id")
        val error = res.get("error")
        return if (id == null)
            Result(data = null, error = error.asString)
        else
            Result(data = id.asString, error = null)
    }

    fun login(email: String, password: String) : Result<List<Any>> {
        val data = JsonObject()
        data.addProperty("email", email)
        data.addProperty("password", password)
        val res = service.login(data)
        val token = res.get("token")
        val user = gson.fromJson(res.get("userRecord"), User::class.java)
        return if (token == null)
            Result(data = null, error = res.get("error").asString)
        else
            Result(data = listOf(token.asString, user), error = null)
    }

    fun logout(headers: Map<String, String>): Result<String> {
        val res = service.logout(headers)
        val error = res.get("error")
        return if (error != null)
            Result(data = null, error = error.asString)
        else
            Result(data = "OK", error = null)
    }

    // /day

    fun getOrCreateDay(headers: Map<String, String>, date: String): Result<Day>{
        val data = JsonObject().apply { addProperty("date", date) }
        val res = service.getOrCreateDay(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(data = null, error = error.asString)
        else
            Result(data = gson.fromJson(res, Day::class.java), error = null)
    }

    fun setRating(headers: Map<String, String>, date: String, rating: Float) {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("rating", rating)
        }
        service.setRating(headers, data)
    }

    fun addWater(headers: Map<String, String>, date: String, water: Int) {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("water", water)
        }
        service.addWater(headers, data)
    }

    fun getWeek(headers: Map<String, String>, firstDayOfWeek: String): Result<List<Day>> {
        val data = JsonObject().apply { addProperty("date", firstDayOfWeek) }
        val res = service.getWeek(headers, data)
        return if (res.size() == 0)
            Result(data = null, error = "Cannot fetch data")
        else
            Result(data = res.map { item -> gson.fromJson(item, Day::class.java) }, error = null)
    }

    // /activity

    fun addUserData(user: User, sleep: Double) : Result<BasicQuizResult> {
        val data = JsonObject().apply {
            add("user", gson.toJsonTree(user))
            addProperty("sleep", sleep)
        }
        Log.d(DEBUG_TAG, data.toString())
        val res = service.addUserData(data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else {
            Log.d(DEBUG_TAG, res.toString())
            Result(data = gson.fromJson(res, BasicQuizResult::class.java), error = null)
        }
    }

    fun addSleepData(headers: Map<String, String>, sleep: Double): Result<SleepQuizResult> {
        val data = JsonObject().apply {
            addProperty("sleep", sleep)
        }
        val res = service.addSleepData(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else {
            Result(data = gson.fromJson(res, SleepQuizResult::class.java), error = null)
        }
    }

    fun addActivityData(headers: Map<String, String>, date: String, id: String, length: Float, type: String): Result<Day> {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("_id", id)
            addProperty("type", type)
            addProperty("length", length)
        }
        val res = service.addActivity(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, Day::class.java), error = null)
    }

    fun addEatenProduct(headers: Map<String, String>, id: String, eating: String, date: String, weight: Float): Result<Day> {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("productId", id)
            addProperty("eating", eating)
            addProperty("mass", weight)
        }
        val res = service.addProduct(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, Day::class.java), error = null)
    }

    fun searchProduct(search: String): Result<List<ShortView>> {
        val res = service.searchProduct(search)
        return if (res.size() == 0)
            Result(error = "No data", data = null)
        else
            Result(error = null, data = res.map { item -> gson.fromJson(item, ShortView::class.java) })
    }

    fun getProductInfo(id: String): Result<Product> {
        val res = service.getProductInfo(id)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, Product::class.java), error = null)
    }

    fun searchSport(search: String): Result<List<ShortView>> {
        val res = service.searchSport(search)
        return if (res.size() == 0)
            Result(error = "No data", data = null)
        else
            Result(error = null, data = res.map { item -> gson.fromJson(item, ShortView::class.java) })
    }

    fun getSportInfo(id: String): Result<Activities> {
        val res = service.getSportInfo(id)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, Activities::class.java), error = null)
    }

    fun searchHousework(search: String): Result<List<ShortView>> {
        val res = service.searchHousework(search)
        return if (res.size() == 0)
            Result(error = "No data", data = null)
        else
            Result(error = null, data = res.map { item -> gson.fromJson(item, ShortView::class.java) })
    }

    fun getHouseworkInfo(id: String): Result<Activities> {
        val res = service.getHouseworkInfo(id)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, Activities::class.java), error = null)
    }

    fun updateWeight(headers: Map<String, String>, date: String, weight: Int): Result<BasicQuizResult> {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("weight", weight)
        }
        val res = service.updateWeight(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, BasicQuizResult::class.java), error = null)
    }

    fun updateHeight(headers: Map<String, String>, date: String, height: Int): Result<BasicQuizResult> {
        val data = JsonObject().apply {
            addProperty("date", date)
            addProperty("height", height)
        }
        val res = service.updateHeight(headers, data)
        val error = res.get("error")
        return if (error != null)
            Result(error = error.asString, data = null)
        else
            Result(data = gson.fromJson(res, BasicQuizResult::class.java), error = null)
    }

    // /posts

    fun getAllArticles(): Result<List<Article>> {
        val res = service.getAllArticles()
        return if (res.size() == 0)
            Result(error = "No data", data = null)
        else
            Result(error = null, data = res.map { item -> gson.fromJson(item, Article::class.java) })
    }

    fun getArticleByID(id: String): Result<Article> {
        val res = service.getArticleByID(id)
        var error = res.get("error")
        return if (res.size() == 0)
            Result(error = "No data", data = null)
        else
            Result(error = null, data = gson.fromJson(res, Article::class.java))
    }
}