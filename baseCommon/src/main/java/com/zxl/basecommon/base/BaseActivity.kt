package com.zxl.basecommon.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.zxl.basecommon.R
import com.zxl.basecommon.databinding.ActivityBaseBinding
import com.zxl.basecommon.utils.PermissionCallBack
import com.zxl.basecommon.utils.PermissionManager

abstract class BaseActivity<V : ViewModel, D : ViewDataBinding> : AppCompatActivity() {

    private lateinit var rootDataBinding: ActivityBaseBinding

    protected lateinit var mDataBinding: D

    protected lateinit var mViewModel: V

    protected var needPermision: Boolean = false

    protected var permissionManagers: PermissionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initViewDataBinding()
        initData()
        initView()
        initPermission()
    }

    //是否需要请求权限
    private fun initPermission() {
        if (needPermision) {
            permissionManagers = PermissionManager(this)
        }
    }

    //请求权限
    private fun requestPermission(
        necessary: Boolean,
        permissionArray: Array<String>,
        permitCallback: PermissionCallBack? = null,
        denayCallBack: PermissionCallBack? = null
    ) {
        permissionManagers?.requestPermission(
            necessary,
            permissionArray,
            permitCallback,
            denayCallBack
        )
    }

    private fun initViewModel() {
        mViewModel = obtainViewModel()
    }

    abstract fun obtainViewModel(): V

    open fun initData() {

    }

    open fun initView() {

    }

    private fun initViewDataBinding() {
        rootDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        rootDataBinding.setVariable(initViewModelId(), mViewModel);
        mDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutId(),
            rootDataBinding.flContainer,
            true
        )
        mDataBinding.lifecycleOwner = this

    }

    abstract fun initViewModelId(): Int


    abstract fun getLayoutId(): Int
}