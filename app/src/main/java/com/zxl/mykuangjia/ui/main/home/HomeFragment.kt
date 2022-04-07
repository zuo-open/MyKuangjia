package com.zxl.mykuangjia.ui.main.home

import android.database.Observable
import android.widget.Toast
import androidx.lifecycle.Observer
import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentHomeBinding
import org.koin.android.ext.android.get
import java.util.jar.Manifest

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    init {
        needPermission = true
    }

    override fun initObserver() {
        mViewModel.btnClick1.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                requestPermission(
                    true,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                    ),
                    {
                        Toast.makeText(this@HomeFragment.context, "权限请求成功", Toast.LENGTH_SHORT)
                            .show()
                    },
                    {
                        Toast.makeText(this@HomeFragment.context, "权限请求失败", Toast.LENGTH_SHORT)
                            .show()
                    })
            }

        })
    }


    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): HomeViewModel = get()
}