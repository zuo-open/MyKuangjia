package com.zxl.componentgallery.components.fingergesture.ui

import android.app.Activity
import com.zxl.basecommon.base.BaseActivity
import com.zxl.componentgallery.BR
import com.zxl.componentgallery.R
import com.zxl.componentgallery.databinding.ActivityLoginTypeBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginTypeActivity : BaseActivity<LoginTypeViewModel, ActivityLoginTypeBinding>() {


    override fun initView() {
        super.initView()
        mDataBinding.btnGestureRegister.setOnClickListener {
            GestureLoginActivity.startActivityForReset(this)
        }
        mDataBinding.btnGestureCheck.setOnClickListener {
            GestureLoginActivity.startActivityForCheck(this)
        }
        mDataBinding.btnGestureReset.setOnClickListener {
            GestureLoginActivity.startActivityForUpdate(this)
        }
    }


    override fun obtainViewModel(): LoginTypeViewModel = getViewModel()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_login_type
}