package com.zxl.mykuangjia.ui.main.home

import android.content.Intent
import byc.imagewatcher.ui.ImageWatcherActivity
import com.dds.fingerprintidentify.demo.FingerPrintActivity
import com.dds.gestureunlock.demo.GestureLockActivity
import com.example.teprinciple.updateappdemo.UpdateUtilsMainActivity
import com.just.agentweb.sample.activity.AgentWebMainActivity
import com.king.zxing.demo.ZxingLiteMainActivity
import com.robinhood.ticker.sample.TickerMainActivity
import com.zxl.basecommon.base.BaseFragment
import com.zxl.mykuangjia.ui.main.home.http.HttpRequestActivity
import com.zxl.basecommon.utils.showError
import com.zxl.basecommon.utils.showSuccess
import com.zxl.componentgallery.components.expanableTextView.DemoActivity
import com.zxl.componentgallery.components.fingergesture.ui.GestureLoginActivity
import com.zxl.componentgallery.components.fingergesture.ui.GestureLoginActivity.Companion.startActivityForReset
import com.zxl.componentgallery.components.fingergesture.ui.LoginTypeActivity
import com.zxl.componentgallery.components.like.LikeButtonActivity
import com.zxl.componentgallery.components.loading.LoadingActivity
import com.zxl.componentgallery.components.skeleton.SkeletonActivity
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.FragmentHomeBinding
import com.zxl.mykuangjia.ui.main.home.multilanguage.MultilanguageActivity
import com.zxl.mykuangjia.ui.main.demo.kotlinDemo.flow.FlowDemo1Activity
import com.zxl.mykuangjia.ui.main.demo.mviDemo.main.MVIDemoActivity
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

        //应用更新
        mViewModel.btnClick9.observe(this) {
            startActivity(Intent(requireActivity(), UpdateUtilsMainActivity::class.java))
        }
        //网络请求
        mViewModel.btnClick10.observe(this) {
            startActivity(Intent(requireActivity(), HttpRequestActivity::class.java))
        }
        //多语言
        mViewModel.btnClick11.observe(this) {
            startActivity(Intent(requireActivity(), MultilanguageActivity::class.java))
        }
        //手势解锁
        mViewModel.btnClick12.observe(this) {
            startActivity(Intent(requireActivity(), GestureLockActivity::class.java))
        }
        //zxing扫一扫
        mViewModel.btnClick13.observe(this) {
            startActivity(Intent(requireActivity(), ZxingLiteMainActivity::class.java))
        }

        //AgentWeb
        mViewModel.btnClick14.observe(this) {
            startActivity(Intent(requireActivity(), AgentWebMainActivity::class.java))
        }

        //FingerPrint指纹识别
        mViewModel.btnClick15.observe(this) {
            startActivity(Intent(requireActivity(), FingerPrintActivity::class.java))
        }

        //FingerPrint指纹识别
        mViewModel.btnClick16.observe(this) {
            startActivity(Intent(requireActivity(), FlowDemo1Activity::class.java))
        }

        //mviDemo
        mViewModel.btnClick17.observe(this) {
            startActivity(Intent(requireActivity(), MVIDemoActivity::class.java))
        }


    }


    override fun initData() {
        super.initData()
        mViewModel.testFlow()
    }


    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initViewModelId(): Int = BR.viewModel

    override fun obtainViewModel(): HomeViewModel = get()
}