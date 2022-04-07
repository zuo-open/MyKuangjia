package com.zxl.componentgallery.components.elemelist.adapter

import android.widget.TextView
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.elemelist.bean.ElemeBean

class ElemeList1LeftAdapter(@LayoutRes val layoutRes: Int) :
    BaseQuickAdapter<ElemeBean, BaseViewHolder>(layoutRes) {
    override fun convert(holder: BaseViewHolder, item: ElemeBean) {
        val tvTitle = holder.getView<TextView>(R.id.tv_title)
        tvTitle.text = item.title;
    }
}