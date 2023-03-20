package com.zxl.mykuangjia.ui.main.home.splash

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.http.BaseRetrofit
import com.zxl.basecommon.http.callResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class LoadSplashDataService : Service() {

    companion object {
        private const val TAG = "LoadSplashDataService"
        const val SPLASH_DATA = "SPLASH_DATA"//请求数据存储key
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            try {
                val splashData =
                    BaseRetrofit().getService(SplashApi::class.java)
                        .getSplashData("https://console-mock.apipost.cn/mock/a739dc37-c2fd-4d6b-ccc2-0685bcb6570b/mock/a739dc37-c2fd-4d6b-ccc2-0685bcb6570b/mock/a739dc37-c2fd-4d6b-ccc2-0685bcb6570b/splash?apipost_id=fba47d")
                        .callResult()
                Log.e(TAG, Gson().toJson(splashData))
                splashData?.let {
                    loadImageFile(splashData)
                }
            } catch (e: Exception) {
                Log.e(TAG, "获取闪屏页数据失败")
            }

        }
    }

    //保存欢迎页图片
    @SuppressLint("CheckResult")
    private fun loadImageFile(splashData: SplashDataBean) {
        splashData.fileName
        val SPLASH_IMAGE_SAVE_PATH =
            "${BaseContext.getApplication().getExternalFilesDir("")}/splash"
        val fileDir = File(SPLASH_IMAGE_SAVE_PATH)
        //创建文件夹
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val file = File(SPLASH_IMAGE_SAVE_PATH, splashData.fileName)
        //文件下载
//        val aa = splashData.imgUrl.download()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onNext = {
//
//                },
//                onComplete = {
//                    Log.e("211", "下载成功")
//
//                },
//                onError = {
//                    Log.e("211", it.message ?: "")
//                }
//            )
        Glide.with(BaseContext.getApplication()).downloadOnly().load(splashData.imgUrl).listener(
            object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "下载失败")
                    return false
                }

                override fun onResourceReady(
                    resource: File,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "下载成功")
                    val copyTo = resource.copyTo(file, true)
                    if (copyTo.exists()) {
                        SPUtils.getInstance().put(SPLASH_DATA, Gson().toJson(splashData))
                    }
                    return true
                }

            }).preload()


    }

}