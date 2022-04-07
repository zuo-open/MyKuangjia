package com.zxl.mykuangjia.bean

class MenuBean {
    var id: String? = null
    var title: String? = null
    var img_press: String? = null
    var img_normal: String? = null
    var enable: Boolean = false
    var url: String? = null

    constructor(
        id: String,
        title: String,
        img_press: String,
        img_normal: String,
        enable: Boolean,
        url: String
    ) {
        this.id = id
        this.title = title
        this.img_press = img_press
        this.img_normal = img_normal
        this.enable = enable
        this.url = url
    }
}