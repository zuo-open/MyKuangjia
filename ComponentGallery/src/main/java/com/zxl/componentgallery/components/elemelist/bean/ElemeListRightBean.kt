package com.zxl.componentgallery.components.elemelist.bean

class ElemeListRightBean {
    var isBigTitle: Boolean = false
    var title: String = ""

    constructor(title: String, isBigTitle: Boolean = false) {
        this.isBigTitle = isBigTitle ?: false
        this.title = title
    }
}