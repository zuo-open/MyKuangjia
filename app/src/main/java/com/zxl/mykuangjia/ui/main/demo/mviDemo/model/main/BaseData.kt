package com.zxl.mykuangjia.ui.main.demo.mviDemo.model.main

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @author jianchong.hu
 * @create at 2022 12.12
 * @description:
 **/
class BaseData<T> {
    @SerializedName("errorCode")
    var code = -1

    @SerializedName("errorMsg")
    var msg: String? = null
    var data: T? = null
}

fun <T> BaseData<T>?.getResult(): T {
    try {
        this?.apply {
            if (code == 0) {
                data?.apply {
                    return this
                } ?: kotlin.run {
                    throw  RuntimeException("获取数据为空")
                }
            } else {
                throw  RuntimeException(msg ?: "获取数据为空")
            }
        }
    } catch (e: Exception) {
        if (e !is CancellationException && e.message != "Canceled") {
            var message = e.message ?: "未知错误"
            if (e is ConnectException || e is UnknownHostException) {
                message = "网络连接异常"
            }
            throw  Exception(message)
        } else {
            throw Exception(e.message ?: "未知错误")
        }
    }
    throw RuntimeException("未知错误")

}

