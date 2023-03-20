package com.zxl.basecommon.http

import com.zxl.libnetwork.BuildConfig
import com.zxl.libnetwork.utils.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseRetrofit {

    companion object {
        private val TIME_OUT: Long = 5 * 60
        private val mClient: OkHttpClient
            get() {
                val builder = OkHttpClient.Builder()
                builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
                builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                if (BuildConfig.DEBUG) {
                    LoggingInterceptor.bindLogging(builder)
                }
                return builder.build()
            }
    }


    inline fun <reified S> getService() = getService(S::class.java)

    //获取api实现类
    open fun <T> getService(clazz: Class<T>): T {
        return Retrofit.Builder().baseUrl("https://www.wanandroid.com")
            .client(mClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(clazz)

    }
}