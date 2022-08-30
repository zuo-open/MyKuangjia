package com.zxl.mykuangjia.ui.main.service

import android.content.Intent
import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentServiceBinding
import com.zxl.servicemodule.ui.aop.AspectJActivity
import org.koin.android.ext.android.get

class ServiceFragment : BaseFragment<ServiceViewModel, FragmentServiceBinding>() {

    override fun initView() {
        mDataBinding.btn1.setOnClickListener {
            val intent = Intent(requireActivity(), AspectJActivity::class.java)
            startActivity(intent)
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_service

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): ServiceViewModel = get()
}