package com.zxl.mykuangjia.ui.main.demo.mviDemo.main

import com.google.gson.Gson
import com.zxl.mvidemo3.model.main.Article
import com.zxl.mvidemo3.model.main.ArticleWrapper
import com.zxl.mykuangjia.ui.main.demo.mviDemo.model.main.getResult
import com.zxl.mykuangjia.ui.main.home.http.Api
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class MVIRepository(val api: Api) {

    fun getArticleFromRemote(page: Int) = suspend {
        val result = api.getArticle(page).getResult()
        ArticleWrapper(result, false)
    }.asFlow()

    fun getArticalFromLocal() = flow<ArticleWrapper> {
        val str = "{\n" +
                "    \"curPage\": 2,\n" +
                "    \"datas\": [\n" +
                "        {\n" +
                "            \"adminAdd\": false,\n" +
                "            \"apkLink\": \"\",\n" +
                "            \"audit\": 1,\n" +
                "            \"author\": \"鸿洋\",\n" +
                "            \"canEdit\": false,\n" +
                "            \"chapterId\": 605,\n" +
                "            \"chapterName\": \"字节跳动\",\n" +
                "            \"collect\": false,\n" +
                "            \"courseId\": 13,\n" +
                "            \"desc\": \"\",\n" +
                "            \"descMd\": \"\",\n" +
                "            \"envelopePic\": \"\",\n" +
                "            \"fresh\": false,\n" +
                "            \"host\": \"\",\n" +
                "            \"id\": 25592,\n" +
                "            \"isAdminAdd\": false,\n" +
                "            \"link\": \"https://juejin.cn/post/7096059314233671694\",\n" +
                "            \"niceDate\": \"2天前\",\n" +
                "            \"niceShareDate\": \"2天前\",\n" +
                "            \"origin\": \"\",\n" +
                "            \"prefix\": \"\",\n" +
                "            \"projectLink\": \"\",\n" +
                "            \"publishTime\": 1674992015000,\n" +
                "            \"realSuperChapterId\": 604,\n" +
                "            \"selfVisible\": 0,\n" +
                "            \"shareDate\": 1674992015000,\n" +
                "            \"shareUser\": \"\",\n" +
                "            \"superChapterId\": 605,\n" +
                "            \"superChapterName\": \"大厂对外分享 - 学习路径\",\n" +
                "            \"tags\": [],\n" +
                "            \"title\": \"我是数据库数据\",\n" +
                "            \"type\": 0,\n" +
                "            \"userId\": 2,\n" +
                "            \"visible\": 1,\n" +
                "            \"zan\": 0\n" +
                "        }\n" +
                "    ],\n" +
                "    \"offset\": 20,\n" +
                "    \"over\": false,\n" +
                "    \"pageCount\": 687,\n" +
                "    \"size\": 20,\n" +
                "    \"total\": 13728\n" +
                "}"
        val fromJson = Gson().fromJson(str, Article::class.java)
        delay(3000)//模拟延时
        emit(ArticleWrapper(fromJson, true))
    }

    fun deleteItem(id: String): String {
        return takeIf { id.isNotBlank() }?.let {
            id
        } ?: kotlin.run {
            throw RuntimeException("删除失败，请检查id")
        }
    }
}