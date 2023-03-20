package com.zxl.mykuangjia.ui.main.home.http

import com.zxl.basecommon.http.HttpResult
import com.zxl.mvidemo3.model.main.Article
import com.zxl.mvidemo3.model.main.Banner
import com.zxl.mykuangjia.ui.main.demo.mviDemo.model.main.BaseData
import retrofit2.http.*

interface Api {

    //获取历史天气
    @GET
    suspend fun getHistoryWeather(
        @Url url: String,
    ): HttpResult<WeatherListBean>


    @GET("banner/json")
    suspend fun getBanner(): BaseData<List<Banner>>

    //页码从0开始
    @GET("article/list/{page}/json")
    suspend fun getArticle(@Path("page") page: Int): BaseData<Article>
}