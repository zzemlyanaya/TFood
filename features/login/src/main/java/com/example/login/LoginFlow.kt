/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 05.08.2021, 17:06
 */

package com.example.login

import com.example.login.presentation.view.LoginFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object LoginFlow {
    fun loginFragment() = FragmentScreen { LoginFragment() }
}