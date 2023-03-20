package com.zxl.mykuangjia.ui.main.home.splash

import com.zxl.basecommon.http.HttpResult
import com.zxl.mykuangjia.ui.main.home.http.WeatherListBean
import retrofit2.http.GET
import retrofit2.http.Url

interface SplashApi {

    @GET
    suspend fun getSplashData(
        @Url url: String,
    ): HttpResult<SplashDataBean>
}