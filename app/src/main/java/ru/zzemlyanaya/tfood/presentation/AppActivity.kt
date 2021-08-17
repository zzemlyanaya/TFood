/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.tfood.presentation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.zzemlyanaya.core.activity.CoreActivity
import ru.zzemlyanaya.core.di.Scopes.ACTIVITY_MAIN_SCOPE
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.di.Scopes.NETWORK_SCOPE
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.extentions.visible
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.PrefsConst.FINGERPRINT
import ru.zzemlyanaya.core.local.PrefsConst.IS_FIRST_LAUNCH
import ru.zzemlyanaya.core.local.di.PrefsModule
import ru.zzemlyanaya.core.navigation.NavigationModule
import ru.zzemlyanaya.core.network.model.State
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.core.utils.KeyboardUtils
import ru.zzemlyanaya.core.utils.UtilsModule
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.ActivityAppBinding
import toothpick.Toothpick
import toothpick.ktp.KTP
import javax.inject.Inject


class AppActivity : CoreActivity() {

    @Inject
    lateinit var keyboardUtils: KeyboardUtils

    override val mProgress by lazy { LoadingDialog.view(supportFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var binding: ActivityAppBinding

    @Inject
    lateinit var localRepository: LocalRepository

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        showToast(R.string.double_tap_exit)

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        KTP.openScopes(NETWORK_SCOPE, ACTIVITY_MAIN_SCOPE)
            .installModules(
                UtilsModule(this),
                NavigationModule(navController)
            )
            .inject(this)

        handleFingerprint()

        initBottomNavigation()
        startNavigation()
    }



    //showing progress so that user knows sth is happening
    private fun handleFingerprint() {
        if (localRepository.getPref(IS_FIRST_LAUNCH) as Boolean) {
            mProgress.showProgress()
            val fingerprint = buildFingerprint()
            localRepository.updatePref(FINGERPRINT, fingerprint)
            mProgress.hideProgress()
        }
    }

    private fun buildFingerprint() = "35" + //we make this look like a valid IMEI
            Build.BOARD.hashCode() % 10 +
            Build.BRAND.hashCode() % 10 +
            Build.CPU_ABI.hashCode() % 10 +
            Build.DEVICE.hashCode() % 10 +
            Build.DISPLAY.hashCode() % 10 +
            Build.HOST.hashCode() % 10 +
            Build.ID.hashCode() % 10 +
            Build.MANUFACTURER.hashCode() % 10 +
            Build.MODEL.hashCode() % 10 +
            Build.PRODUCT.hashCode() % 10 +
            Build.TAGS.hashCode() % 10 +
            Build.TYPE.hashCode() % 10 +
            Build.USER.hashCode() % 10 //13 digits

    private fun startNavigation() {
        navController.navigate(R.id.action_appFlowFragment_to_feature_login_nav_graph)
    }

    private fun initBottomNavigation() {
        binding.bottomBarNav.apply {
            visible = false
            onItemSelectedListener = { _, menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> {
                        val uri = Uri.parse("myApp://dashboardFragment")
                        navController.navigate(uri)
                    }
//                    R.id.item_dairy -> navController.navigate()
//                    R.id.item_profile -> navController.navigate()
//                    R.id.item_statistics -> navController.navigate()
                }
            }
            onItemReselectedListener = { _, _ -> }
        }
    }

    override fun hideKeyboard() {
        window.decorView.postDelayed(
            { keyboardUtils.hideKeyboard(window.decorView) },
            KEYBOARD_HIDING_DELAY
        )
    }

    companion object {
        private const val KEYBOARD_HIDING_DELAY = 300L
    }

    override fun onDestroy() {
        KTP.closeScope(APP_SCOPE)
        super.onDestroy()
    }
}