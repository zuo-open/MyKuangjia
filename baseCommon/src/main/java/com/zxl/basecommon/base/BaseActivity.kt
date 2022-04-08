package com.zxl.basecommon.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.billy.android.loading.Gloading
import com.zxl.basecommon.R
import com.zxl.basecommon.databinding.ActivityBaseBinding
import com.zxl.basecommon.utils.PermissionCallBack
import com.zxl.basecommon.utils.PermissionManager
import com.zxl.basecommon.widgets.GlobalLoadingStatusView

abstract class BaseActivity<V : BaseViewModel, D : ViewDataBinding> : AppCompatActivity() {

    private lateinit var rootDataBinding: ActivityBaseBinding

    protected lateinit var mDataBinding: D

    protected lateinit var mViewModel: V

    protected var needPermision: Boolean = false

    protected var permissionManagers: PermissionManager? = null

    //加载loading
    private var mLoadingHolder: Gloading.Holder? = null

    //空视图loading
    //private var mEmptyHolder: Gloading.Holder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initViewDataBinding()
        initData()
        initView()
        initPermission()
        initGloading()
        initDefaultObserver()
    }

    //基类设置的事件监听
    private fun initDefaultObserver() {

        mViewModel.mBaseObserver.showLoadingEvent.observe(this) {
            mLoadingHolder?.showLoading()
        }

        mViewModel.mBaseObserver.showLoadingSuccessEvent.observe(this) {
            mLoadingHolder?.showLoadSuccess()
        }

        mViewModel.mBaseObserver.showLoadingFailEvent.observe(this) {
            mLoadingHolder?.showLoadFailed()
        }

        mViewModel.mBaseObserver.showEmptyEvent.observe(this) {
            mLoadingHolder?.showEmpty()
        }
    }

    private fun initGloading() {
        showLoadigView()?.apply {
            mLoadingHolder = Gloading.getDefault().wrap(this).withRetry {
                retryData()
            }
        }
//        showEmptyView()?.apply {
//            mEmptyHolder = Gloading.getDefault().wrap(this)
//        }
    }

    //再次重试
    open fun retryData() {

    }

    open fun showLoadigView(): View? {
        return null
    }

    open fun showEmptyView(): View? {
        return null
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
        mDataBinding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutId(),
            rootDataBinding.flContainer,
            true
        )
        mDataBinding.setVariable(initViewModelId(), mViewModel);
        mDataBinding.lifecycleOwner = this

    }

    abstract fun initViewModelId(): Int


    abstract fun getLayoutId(): Int
}