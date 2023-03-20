package com.zxl.mvidemo3.model.main

data class ArticalState(
    var artical: Article? = null,//数据
    var isLoading: Boolean = false,//是否正在加载(上拉加载，下拉刷新)
    var message: String? = null//错误信息
) {
    companion object {
        val initial = ArticalState(isLoading = true)
    }

    //stateFlow 会通过equal来判断两个值是否相等
    override fun equals(other: Any?): Boolean {
        return false
    }


}