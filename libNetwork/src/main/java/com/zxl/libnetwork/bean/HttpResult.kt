package com.zxl.libnetwork.bean

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 根据后台返回的接口范例可进行调整
 */
class HttpResult<T> {
    //请求是否成功
    var error_code: String? = null

    //请求情况描述
    var reason: String? = null

    var result: T? = null
}

class HttpException(val errorCode: String = "-1", override val message: String = "") : Throwable()


suspend fun <T> HttpResult<T>?.callResult() = suspendCancellableCoroutine<T> {
    this?.apply {
        if (error_code != "0") {
            it.resumeWithException(HttpException(message = reason ?: "网络请求失败"))
        }
        if (result != null) {
            it.resume(result!!)
        } else {
            it.resumeWithException(HttpException(message = reason ?: "请求网络数据为空"))
        }
    } ?: kotlin.run {
        it.resumeWithException(HttpException(message = "请求网络数据为空"))
    }

}