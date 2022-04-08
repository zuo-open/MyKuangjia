package com.zxl.componentgallery.components.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.billy.android.loading.Gloading
import com.zxl.basecommon.base.BaseActivity
import com.zxl.componentgallery.BR
import com.zxl.componentgallery.R
import com.zxl.componentgallery.databinding.ActivityLoadingBinding
import org.koin.android.ext.android.get

class LoadingActivity : BaseActivity<LoadingViewModel, ActivityLoadingBinding>() {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun showLoadigView(): View? {
        return mDataBinding.clContainer
    }

    override fun showEmptyView(): View? {
        return mDataBinding.clContainer
    }

    override fun retryData() {
        mViewModel.showLoading()
        mHandler.postDelayed({
            mViewModel.showLoadingSuccess()
        }, 3000)
    }


    override fun obtainViewModel(): LoadingViewModel = get()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_loading

}