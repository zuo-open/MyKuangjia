package com.zxl.componentgallery.components.elemelist.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.elemelist.bean.ElemeListRightBean

class ElemeList1RightAdapter : BaseProviderMultiAdapter<ElemeListRightBean>() {

    companion object {
        const val TAG_TITLE = 1000
        const val TAG_CONTENT = 1001
    }

    init {
        addItemProvider(ElemeRightTitleAdapter(TAG_TITLE, R.layout.item_elemelist1_right_title))
        addItemProvider(
            ElemeRightContentAdapter(
                TAG_CONTENT,
                R.layout.item_elemelist1_right_content
            )
        )
    }

    override fun getItemType(data: List<ElemeListRightBean>, position: Int): Int {
        val data = data.get(position)
        return data.takeIf { it.isBigTitle }?.let { TAG_TITLE } ?: kotlin.run {
            TAG_CONTENT
        }
    }
}