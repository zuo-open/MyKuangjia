package com.zxl.componentgallery.components.elemelist.adapter

import android.widget.TextView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.elemelist.bean.ElemeListRightBean
import com.zxl.componentgallery.components.morefunctions.bean.FunctionBean

class ElemeRightContentAdapter(override val itemViewType: Int, override val layoutId: Int) :
    BaseItemProvider<ElemeListRightBean>() {
    override fun convert(helper: BaseViewHolder, item: ElemeListRightBean) {
        val tvTitle = helper.getView<TextView>(R.id.tv_content)
        tvTitle.text = item.title
    }
}