/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 15:20
 */

package ru.zzemlyanaya.core.api.provider

import retrofit2.http.HeaderMap

interface UrlProvider {
    fun provideServerUrl(): String
    fun provideAuthHeader(): HeaderMap
}