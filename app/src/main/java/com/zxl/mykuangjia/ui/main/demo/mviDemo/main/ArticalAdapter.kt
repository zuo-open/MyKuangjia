package com.zxl.mykuangjia.ui.main.demo.mviDemo.main

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxl.mvidemo3.model.main.ArticleItem
import com.zxl.mykuangjia.R

class ArticalAdapter(data: MutableList<ArticleItem>) :
    BaseQuickAdapter<ArticleItem, BaseViewHolder>(R.layout.item_article, data) {
    override fun convert(holder: BaseViewHolder, item: ArticleItem) {
        val tv = holder.getView<TextView>(R.id.tv_content)
        tv.text = item.title
    }

}