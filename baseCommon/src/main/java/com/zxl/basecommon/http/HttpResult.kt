package com.zxl.basecommon.http

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


const val SUCCESS = "0"

/**
 * 根据后台返回的接口范例可进行调整
 */
class HttpResult<T> {
    //请求是否成功
    var code: String? = null

    //请求情况描述
    var message: String? = null

    var data: T? = null
}

class HttpException(val errorCode: String = "-1", override val message: String = "") : Exception()

/**
 * 普通带返回的数据
 */
suspend fun <T> HttpResult<T>?.callResult() = suspendCancellableCoroutine<T> {
    this?.apply {
        if (SUCCESS != code) {
            it.resumeWithException(HttpException(message = message ?: "网络请求失败"))
        }
        data?.apply {
            it.resume(this);
        } ?: kotlin.run {
            it.resumeWithException(HttpException(message = message ?: "请求网络数据为空"))
        }
    } ?: kotlin.run {
        it.resumeWithException(HttpException(message = "请求网络数据为空"))
    }

}

/**
 *不带返回的数据
 */
suspend fun <Unit> HttpResult<Unit>.callRequestWithoutResult() =
    suspendCancellableCoroutine<kotlin.Unit> {
        this?.apply {
            if (SUCCESS != code) {
                it.resumeWithException(HttpException(message = message ?: "网络请求失败"))
            }
            data?.apply {
                it.resume(Unit);
            } ?: kotlin.run {
                it.resumeWithException(HttpException(message = message ?: "请求网络数据为空"))
            }
        } ?: kotlin.run {
            it.resumeWithException(HttpException(message = "请求网络数据为空"))
        }
    }