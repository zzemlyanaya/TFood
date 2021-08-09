/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.network.repository

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.zzemlyanaya.core.network.model.JWT
import ru.zzemlyanaya.core.network.model.RefreshDTO
import ru.zzemlyanaya.core.network.model.TokenPair
import ru.zzemlyanaya.core.network.provider.UrlProvider
import java.util.*
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val urlProvider: UrlProvider,
    private var currentToken: String,
    private var refreshDTO: RefreshDTO,
    private val gson: Gson
) {

    fun getToken() = currentToken

    fun getRefreshUrl() = "${urlProvider.provideServerUrl()}auth/refresh"

    fun updateCurrentToken(newToken: String) {
        currentToken = newToken
    }

    fun getRefreshRequestBody(): RequestBody =
        gson.toJson(refreshDTO).toString().toRequestBody()

    fun getTokenFromResponse(requestResponse: Response) =
        gson.fromJson(
            requestResponse.body?.string(),
            TokenPair::class.java
        ).accessToken

    fun deleteToken() {
        currentToken = ""
    }

    fun isTokenExpired(): Boolean {
        val payload = currentToken.split(".")[1]
        val decodedBytes = Base64.getDecoder().decode(payload)
        val decodedPayload = String(decodedBytes)
        val exp = (decodedPayload as JWT).exp
        return exp > Calendar.getInstance().time as Int
    }
}