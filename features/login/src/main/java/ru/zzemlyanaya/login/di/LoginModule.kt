/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.login.di

import ru.zzemlyanaya.login.data.api.AuthAPI
import ru.zzemlyanaya.login.data.repository.AuthRepository
import toothpick.config.Module

class LoginModule : Module() {
    init {
        bind(AuthAPI::class.java).toProvider(AuthApiProvider::class.java)
        bind(AuthRepository::class.java).toProvider(AuthRepositoryProvider::class.java)
            .providesSingleton()
    }
}