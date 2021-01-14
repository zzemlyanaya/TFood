/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.01.2021, 14:42
 */

package ru.zzemlyanaya.tfood.data.remote

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
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
                return try {
                    call.execute().body()!!
                } catch (e: Exception) {
                    throw RuntimeException(e.localizedMessage)
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