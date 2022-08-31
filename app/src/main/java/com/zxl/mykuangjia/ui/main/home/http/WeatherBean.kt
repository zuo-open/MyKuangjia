package com.zxl.mykuangjia.ui.main.home.http

class WeatherListBean {
    var list: List<WeatherBean>? = null
    var logoUrl: String? = null
    var sourceName: String? = null
    var total: String? = null
}

class WeatherBean {
    var airData: String? = null
    var airQuality: String? = null
    var city: String? = null
    var date: String? = null
    var high: String? = null
    var low: String? = null
}