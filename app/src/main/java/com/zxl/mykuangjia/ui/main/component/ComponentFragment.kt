package com.zxl.mykuangjia.ui.main.component

import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentComponentBinding
import com.zxl.mykuangjia.databinding.FragmentHomeBinding
import org.koin.android.ext.android.get

class ComponentFragment : BaseFragment<ComponentViewModel, FragmentComponentBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_component

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): ComponentViewModel = get()
}