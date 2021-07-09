/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.07.2021, 15:11
 */

package ru.zzemlyanaya.tfood.data.remote

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import ru.zzemlyanaya.tfood.model.ErrorResponse
import java.lang.reflect.Type

class SynchronousCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
            returnType: Type,
            annotations: Array<Annotation?>?,
            retrofit: Retrofit?
    ): CallAdapter<Any?, Any?>? {
        return if (returnType.toString().contains("retrofit2.Call")) {
            null
        } else object : CallAdapter<Any?, Any?> {
            override fun responseType(): Type {
                return returnType
            }

            override fun adapt(call: Call<Any?>): Any {
                try {
                    val gson = GsonBuilder()
                            .setPrettyPrinting()
                            .create()
                    val res = call.execute()
                    return if (res.isSuccessful)
                        if (res.body() == null) JsonObject().apply { addProperty("message", "OK") }
                        else res.body()!!
                    else {
                        val mes = gson.fromJson(res.errorBody()!!.string(), ErrorResponse::class.java).message
                        JsonObject().apply {
                            addProperty("error", mes)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return JsonObject().apply {
                        addProperty("message", "")
                        addProperty("error", e.message)
                    }
                }
            }
        }
    }

    companion object {
        fun create(): CallAdapter.Factory {
            return SynchronousCallAdapterFactory()
        }
    }
}