package com.zxl.mykuangjia.factory

import androidx.fragment.app.Fragment
import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.bean.MenuBean
import com.zxl.mykuangjia.ui.main.component.ComponentFragment
import com.zxl.mykuangjia.ui.main.home.HomeFragment
import com.zxl.mykuangjia.ui.main.service.ServiceFragment

object MenuFragmentFactory {

    const val TAG_HOME = "com.zxl.mykuangjia.ui.main.home.HomeFragment"
    const val TAG_COMPONENT = "com.zxl.mykuangjia.ui.main.component.ComponentFragment"
    const val TAG_SERVICE = "com.zxl.mykuangjia.ui.main.service.ServiceFragment"

    fun getFragmentByUrl(menuBean: MenuBean): Fragment {

        return when (menuBean.url) {
            TAG_HOME -> {
                HomeFragment()
            }
            TAG_COMPONENT -> {
                ComponentFragment()
            }
            TAG_SERVICE -> {
                ServiceFragment()
            }
            else -> {
                HomeFragment()
            }
        }


    }
}