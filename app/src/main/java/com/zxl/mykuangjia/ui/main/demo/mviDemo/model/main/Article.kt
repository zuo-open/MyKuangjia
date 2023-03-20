package com.zxl.mvidemo3.model.main

data class Article(
    var curPage: Int,
    var datas: List<ArticleItem>,
    val time: Long = System.currentTimeMillis()
)

data class ArticleItem(
    val title: String,
    val link: String,
    val userId: Int,
    val niceDate: String,
    val id: String
)