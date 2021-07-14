/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 14.07.2021, 14:40
 */

package ru.zzemlyanaya.core.api.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HttpErrorLoggingInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }

    private fun buildThrowable(request: Request, response: Response): Throwable {
        val url = formatUrl(request.url)
        return RuntimeException(
            "HTTP error: httpCode = ${response.code}; url = ${url}; message = ${
                response.message
            }"
        )
    }

    private fun formatUrl(httpUrl: HttpUrl): String {
        val url = httpUrl.toUrl()
        return "${url.protocol}://${url.host}${url.path}"
    }
}