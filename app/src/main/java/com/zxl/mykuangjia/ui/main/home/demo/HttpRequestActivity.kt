package com.zxl.mykuangjia.ui.main.home.demo


import android.util.Log
import com.google.gson.Gson
import com.zxl.basecommon.base.BaseActivity
import com.zxl.basecommon.http.callResult
import com.zxl.basecommon.http.httpLaunhcer
import com.zxl.basecommon.utils.showToast
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.ActivityHttpRequestBinding
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HttpRequestActivity : BaseActivity<HttpRequestViewModel, ActivityHttpRequestBinding>() {
    companion object {
        private const val TAG = "HttpRequestActivity"
    }

    override fun initView() {
        super.initView()
        mDataBinding.btnContext.setOnClickListener {
            httpLaunhcer({
                val callResult =
                    get<Api>().getHistoryWeather("http://autodev.openspeech.cn/csp/api/v2.1/weather?openId=aiuicus&clientType=android&sign=android&city=%E4%B8%8A%E6%B5%B7&needMoreData=true&pageNo=1&pageSize=7%20%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%E2%80%94%20%E7%89%88%E6%9D%83%E5%A3%B0%E6%98%8E%EF%BC%9A%E6%9C%AC%E6%96%87%E4%B8%BACSDN%E5%8D%9A%E4%B8%BB%E3%80%8C%E6%BD%87%E6%BD%87%E7%8B%AC%E8%A1%8C%E4%BE%A0%E3%80%8D%E7%9A%84%E5%8E%9F%E5%88%9B%E6%96%87%E7%AB%A0%EF%BC%8C%E9%81%B5%E5%BE%AACC%204.0%20BY-SA%E7%89%88%E6%9D%83%E5%8D%8F%E8%AE%AE%EF%BC%8C%E8%BD%AC%E8%BD%BD%E8%AF%B7%E9%99%84%E4%B8%8A%E5%8E%9F%E6%96%87%E5%87%BA%E5%A4%84%E9%93%BE%E6%8E%A5%E5%8F%8A%E6%9C%AC%E5%A3%B0%E6%98%8E%E3%80%82%20%E5%8E%9F%E6%96%87%E9%93%BE%E6%8E%A5%EF%BC%9Ahttps://blog.csdn.net/qq_25269161/article/details/125188009")
                        .callResult()

                showToast(Gson().toJson(callResult))
            }, {
                Log.e(TAG, "出错了")
            }, {
                Log.e(TAG, "结束了")
            })
        }

    }

    override fun obtainViewModel(): HttpRequestViewModel = getViewModel()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_http_request
}