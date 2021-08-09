/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 17:28
 */

package ru.zzemlyanaya.core.navigation

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