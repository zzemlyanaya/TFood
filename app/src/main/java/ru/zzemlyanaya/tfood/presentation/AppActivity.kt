/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.07.2021, 13:34
 */

package ru.zzemlyanaya.tfood.presentation

import android.os.Bundle
import ru.zzemlyanaya.core.activity.BaseActivity
import ru.zzemlyanaya.tfood.databinding.ActivityAppBinding

class AppActivity : BaseActivity() {

    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}