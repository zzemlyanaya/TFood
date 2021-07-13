/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 13.07.2021, 15:20
 */

package ru.zzemlyanaya.tfood.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Provider

class CiceroneProvider @Inject constructor(private val router: Router) :
    Provider<Cicerone<Router>> {
    override fun get(): Cicerone<Router> = Cicerone.create(router)
}

class NavigatorProvider @Inject constructor(private val cicerone: Cicerone<Router>) :
    Provider<NavigatorHolder> {
    override fun get(): NavigatorHolder = cicerone.getNavigatorHolder()
}

