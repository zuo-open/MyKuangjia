package com.zxl.libnetwork

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseRetrofit {
    private val TIME_OUT: Long = 5 * 60
    private val mClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
            builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            return builder.build()
        }

    //获取api实现类
    fun <T> getService(clazz: Class<T>): T {
        return Retrofit.Builder().baseUrl("http://www.baidu.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(clazz)

    }
}