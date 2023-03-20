package com.zxl.mvidemo3.model.main

/**
 * abort 是否中断流(如果网络数据比数据库数据先来，则不需要发射后来的数据库数据)
 */
data class ArticleWrapper(val article: Article,val abort:Boolean)