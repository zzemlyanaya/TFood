/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.zzemlyanaya.core.network.repository.TokenRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val repository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return if (repository.isTokenExpired().not()) {
            chain.proceedDeletingTokenOnError(
                chain.request().newBuilder().addHeaders(repository.getToken()).build()
            )
        } else {
            val refreshTokenRequest =
                originalRequest
                    .newBuilder()
                    .post(repository.getRefreshRequestBody())
                    .url(repository.getRefreshUrl())
                    .build()
            val refreshResponse = chain.proceedDeletingTokenOnError(refreshTokenRequest)

            if (refreshResponse.isSuccessful) {
                val refreshedToken = repository.getTokenFromResponse(refreshResponse)
                repository.updateCurrentToken(refreshedToken)

                val newCall = originalRequest.newBuilder().addHeaders(refreshedToken).build()
                chain.proceedDeletingTokenOnError(newCall)
            } else
                chain.proceedDeletingTokenOnError(chain.request())
        }
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        if (response.code == 401) {
            repository.deleteToken()
        }
        return response
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply { header("Authorization", "Bearer $token") }
}