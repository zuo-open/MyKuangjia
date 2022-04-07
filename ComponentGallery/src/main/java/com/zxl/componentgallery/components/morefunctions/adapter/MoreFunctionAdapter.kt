package com.zxl.componentgallery.components.morefunctions.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.morefunctions.bean.FunctionBean
import com.zxl.componentgallery.components.morefunctions.bean.TAG_FUNCTION
import com.zxl.componentgallery.components.morefunctions.bean.TAG_SUBTITLE
import com.zxl.componentgallery.components.morefunctions.bean.TAG_TITLE

class MoreFunctionAdapter : BaseProviderMultiAdapter<FunctionBean>() {

    init {
        addItemProvider(FunctionAdapter(TAG_FUNCTION, R.layout.item_function))
        addItemProvider(TitleAdapter(TAG_TITLE, R.layout.item_title))
        addItemProvider(SubTitleAdapter(TAG_SUBTITLE, R.layout.item_subtitle))
    }

    override fun getItemType(data: List<FunctionBean>, position: Int): Int {
        val functionBean = data[position]
        return functionBean.type
    }

    fun isDragEnable(layoutPosition: Int): Boolean {
        var flag = false
        if (data.size >= layoutPosition) {
            data.indexOfFirst { it.type == TAG_TITLE }.takeIf {
                it != -1
            }?.also {
                flag = layoutPosition < it
            }

        }
        return flag
    }


}