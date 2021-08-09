/*
 * Created by Evgeniya Zemlyanaya (@zzemlyanaya)
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 09.08.2021, 18:16
 */

package ru.zzemlyanaya.tfood.presentation

import android.os.Build
import android.os.Bundle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.zzemlyanaya.core.activity.CoreActivity
import ru.zzemlyanaya.core.di.Scopes.APP_SCOPE
import ru.zzemlyanaya.core.dialog.LoadingDialog
import ru.zzemlyanaya.core.local.LocalRepository
import ru.zzemlyanaya.core.local.PrefsConst.IS_FIRST_LAUNCH
import ru.zzemlyanaya.core.local.di.PrefsModule
import ru.zzemlyanaya.core.presentation.ErrorView
import ru.zzemlyanaya.core.presentation.MessageView
import ru.zzemlyanaya.tfood.R
import ru.zzemlyanaya.tfood.databinding.ActivityAppBinding
import ru.zzemlyanaya.tfood.navigation.GlobalFlow.flowLoginFragment
import toothpick.ktp.KTP
import javax.inject.Inject


class AppActivity : CoreActivity() {

    override val mProgress by lazy { LoadingDialog.view(supportFragmentManager, loadingViewTag) }
    override var mError: ErrorView? = null
    override var mMessage: MessageView? = null

//    private var lastToast: Toast? = null
//    private var lastState: State<*>? = null

    private lateinit var binding: ActivityAppBinding

    override val navigator: Navigator = AppNavigator(this, R.id.container)

    @Inject
    lateinit var localRepository: LocalRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        KTP.openScope(APP_SCOPE)
            .installModules(PrefsModule(this))
            .inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFingerprint()

        router.navigateTo(flowLoginFragment())
    }

    //showing progress so that user knows sth is happening
    private fun handleFingerprint() {
        if (localRepository.getPref(IS_FIRST_LAUNCH) as Boolean) {
            mProgress.showProgress()
            val fingerprint = buildFingerprint()
            localRepository.updatePref(IS_FIRST_LAUNCH, fingerprint)
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

}