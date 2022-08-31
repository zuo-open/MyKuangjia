package com.zxl.mykuangjia.ui.main.home

import android.content.Intent
import byc.imagewatcher.ui.ImageWatcherActivity
import com.example.teprinciple.updateappdemo.UpdateUtilsMainActivity
import com.robinhood.ticker.sample.TickerMainActivity
import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.ui.main.home.http.HttpRequestActivity
import com.zxl.basecommon.utils.showError
import com.zxl.basecommon.utils.showSuccess
import com.zxl.componentgallery.components.expanableTextView.DemoActivity
import com.zxl.componentgallery.components.like.LikeButtonActivity
import com.zxl.componentgallery.components.loading.LoadingActivity
import com.zxl.componentgallery.components.skeleton.SkeletonActivity
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentHomeBinding
import com.zxl.mykuangjia.ui.main.home.multilanguage.MultilanguageActivity
import org.koin.android.ext.android.get

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    init {
        needPermission = true
    }

    override fun initObserver() {
        //权限请求
        mViewModel.btnClick1.observe(this) {
            requestPermission(
                true,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ),
                {
                    showSuccess("权限请求成功")

                },
                {
                    showError("权限请求失败")
                })
        }
        //点赞效果
        mViewModel.btnClick2.observe(this) {
            startActivity(Intent(requireActivity(), LikeButtonActivity::class.java))
        }
        //骨架屏
        mViewModel.btnClick3.observe(this) {
            startActivity(Intent(requireActivity(), SkeletonActivity::class.java))
        }
        //gloading
        mViewModel.btnClick4.observe(this) {
            startActivity(Intent(requireActivity(), LoadingActivity::class.java))
        }
        //新手引导
        mViewModel.btnClick5.observe(this) {
            //startActivity(Intent(requireActivity(), FirstActivity::class.java))
        }
        //ImageWacther
        mViewModel.btnClick6.observe(this) {
            startActivity(Intent(requireActivity(), ImageWatcherActivity::class.java))
        }
        //ticker
        mViewModel.btnClick7.observe(this) {
            startActivity(Intent(requireActivity(), TickerMainActivity::class.java))
        }

        mViewModel.btnClick8.observe(this) {
            startActivity(Intent(requireActivity(), DemoActivity::class.java))
        }

        mViewModel.btnClick9.observe(this) {
            startActivity(Intent(requireActivity(), UpdateUtilsMainActivity::class.java))
        }

        mViewModel.btnClick10.observe(this) {
            startActivity(Intent(requireActivity(), HttpRequestActivity::class.java))
        }

        mViewModel.btnClick11.observe(this) {
            startActivity(Intent(requireActivity(), MultilanguageActivity::class.java))
        }


    }


    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): HomeViewModel = get()
}