package com.zxl.componentgallery.components.morefunctions.adapter

import android.widget.TextView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.morefunctions.bean.FunctionBean

class SubTitleAdapter(override val itemViewType: Int, override val layoutId: Int) :
    BaseItemProvider<FunctionBean>() {
    override fun convert(helper: BaseViewHolder, item: FunctionBean) {
        val tvTitle = helper.getView<TextView>(R.id.tv_title)
        tvTitle.text = item.name

    }
}