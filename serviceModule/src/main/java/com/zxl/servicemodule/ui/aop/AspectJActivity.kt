package com.zxl.servicemodule.ui.aop

import android.util.Log
import android.view.View
import com.zxl.basecommon.base.BaseActivity
import com.zxl.servicemodule.BR
import com.zxl.servicemodule.R
import com.zxl.servicemodule.databinding.ActivityAspectjBinding
import org.koin.android.ext.android.get

class AspectJActivity : BaseActivity<AspectJViewModel, ActivityAspectjBinding>() {

    companion object {
        const val TAG = "AspectJActivity"
    }

    override fun initView() {
        super.initView()
        mDataBinding.btnAop.setOnClickListener(object : View.OnClickListener {
            @SingleClick(1000)
            override fun onClick(v: View) {
                Log.e(TAG, "点击了")
            }

        })
    }


    override fun obtainViewModel(): AspectJViewModel = get()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_aspectj

}