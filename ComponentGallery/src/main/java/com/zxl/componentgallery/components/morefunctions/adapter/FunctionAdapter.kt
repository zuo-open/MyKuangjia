package com.zxl.componentgallery.components.morefunctions.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.morefunctions.bean.FunctionBean

class FunctionAdapter(override val itemViewType: Int, override val layoutId: Int) :
    BaseItemProvider<FunctionBean>() {
    override fun convert(helper: BaseViewHolder, item: FunctionBean) {
        val tvName = helper.getView<TextView>(R.id.tv_name)
        val ivShow = helper.getView<ImageView>(R.id.iv_show)
        tvName.text = item.name
        val showResId = when (item.show) {
            true -> R.drawable.function_delete
            else -> R.drawable.function_add
        }
        ivShow.setImageResource(showResId)
    }
}