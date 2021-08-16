/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 16.08.2021, 9:11
 */

package ru.zzemlyanaya.tfood.presentation

import android.os.Build
import android.os.Bundle
import androidx.navigation.Navigation
import ru.zzemlyanaya.core.activity.CoreActivity
import ru.zzemlyanaya.core.di.AppModule
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.extentions.navigateSafe
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.PrefsConst.FINGERPRINT
import ru.zzemlyanaya.core.local.PrefsConst.IS_FIRST_LAUNCH
import ru.zzemlyanaya.core.local.di.PrefsModule
import ru.zzemlyanaya.core.navigation.NavManager
import ru.zzemlyanaya.core.navigation.NavigationModule
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.ActivityAppBinding
import toothpick.ktp.KTP
import javax.inject.Inject


class AppActivity : CoreActivity() {

    override val mProgress by lazy { LoadingDialog.view(supportFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

//    private var lastToast: Toast? = null
//    private var lastState: State<*>? = null

    private val navController get() = Navigation.findNavController(this, R.id.navHostFragment)

    @Inject
    lateinit var navManager: NavManager

    private lateinit var binding: ActivityAppBinding

    @Inject
    lateinit var localRepository: LocalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScopes(APP_SCOPE, this)
            .installModules(AppModule(this), NavigationModule(), PrefsModule(this))
            .inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFingerprint()

        binding.fab.visible = false
        binding.fabGroup.visible = false

        initBottomNavigation()
        initNavManager()
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

    private fun initNavManager() {
        navController.navigateSafe(R.id.featureLoginNavGraph)
    }

    private fun initBottomNavigation() {
        binding.bottomBarNav.apply {
            visible = false
            onItemSelectedListener = { _, menuItem ->
                when (menuItem.itemId) {
                    R.id.item_home -> navController.navigateSafe(R.id.featureDashboardNavGraph)
//                    R.id.item_dairy -> navController.navigate()
//                    R.id.item_profile -> navController.navigate()
//                    R.id.item_statistics -> navController.navigate()
                }
            }
            onItemReselectedListener = { _, _ -> }
        }
    }
}