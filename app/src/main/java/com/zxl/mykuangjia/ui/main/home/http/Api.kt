package com.zxl.mykuangjia.ui.main.home.http

import com.zxl.basecommon.http.HttpResult
import retrofit2.http.*

interface Api {

    //获取历史天气
    @GET
    suspend fun getHistoryWeather(
        @Url url: String,
    ): HttpResult<WeatherListBean>
}