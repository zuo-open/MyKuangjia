package com.zxl.basecommon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.zxl.basecommon.utils.PermissionCallBack
import com.zxl.basecommon.utils.PermissionManager

abstract class BaseFragment<V : ViewModel, D : ViewDataBinding> : Fragment() {

    protected lateinit var mDataBinding: D
    protected lateinit var mViewModel: V
    protected var needPermission = false
    protected var permissionManagers: PermissionManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initDataBinding()
        initView()
        initData()
        initObserver()
        initPermission()
        return mDataBinding.root
    }

    //是否需要请求权限
    private fun initPermission() {
        if (needPermission) {
            permissionManagers = PermissionManager(fragment = this)
        }
    }

    //请求权限
    fun requestPermission(
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

    open fun initObserver() {

    }

    open fun initView() {

    }

    open fun initData() {

    }

    private fun initDataBinding() {
        mDataBinding.lifecycleOwner = this
        mViewModel = obtainViewModel()
        mDataBinding.setVariable(initViewModelId(), mViewModel)

    }


    abstract fun getLayoutId(): Int

    abstract fun initViewModelId(): Int

    abstract fun obtainViewModel(): V
}