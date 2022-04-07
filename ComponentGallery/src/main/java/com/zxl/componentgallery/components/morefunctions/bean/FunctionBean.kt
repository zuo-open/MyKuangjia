package com.zxl.componentgallery.components.morefunctions.bean

data class FunctionBean(
    var name: String,
    var url: String,
    var type: Int,
    var show: Boolean = false,
    var org: Int? = null
)

//主标题
const val TAG_TITLE = 0

//副标题
const val TAG_SUBTITLE = 1

//应用
const val TAG_FUNCTION = 2

//我的业务
const val TAG_WDYW = 3

//影像管理
const val TAG_YXGL = 4

//移动办公
const val TAG_YDBG = 5

