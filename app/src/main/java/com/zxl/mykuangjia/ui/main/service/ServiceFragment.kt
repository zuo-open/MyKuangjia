package com.zxl.mykuangjia.ui.main.service

import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentHomeBinding
import com.zxl.mykuangjia.databinding.FragmentServiceBinding
import org.koin.android.ext.android.get

class ServiceFragment : BaseFragment<ServiceViewModel, FragmentServiceBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_service

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): ServiceViewModel = get()
}