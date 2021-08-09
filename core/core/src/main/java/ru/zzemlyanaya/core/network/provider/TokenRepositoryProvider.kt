/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.core.network.provider

import com.google.gson.Gson
import ru.zzemlyanaya.core.network.model.RefreshDTO
import ru.zzemlyanaya.core.network.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class TokenRepositoryProvider @Inject constructor(
    private val urlProvider: UrlProvider,
    private val gson: Gson,
    private val refreshDTO: RefreshDTO,
    @field:Named("currentToken")
    private val currentToken: String
) : Provider<TokenRepository> {
    override fun get(): TokenRepository =
        TokenRepository(urlProvider, currentToken, refreshDTO, gson)
}