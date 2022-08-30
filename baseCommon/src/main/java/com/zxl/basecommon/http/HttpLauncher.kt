package com.zxl.basecommon.http

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.zxl.basecommon.utils.showError
import com.zxl.basecommon.utils.showToast
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

/**
 * 协程网络请求体
 */

//成功回调
typealias SuccessCallBack = suspend () -> Unit
//失败回调
typealias FailCallBack = (suspend (Throwable) -> Unit)?
//请求结束后回调
typealias FinishCallBack = (suspend () -> Unit)?

//viewModel网络请求
fun ViewModel.httpLaunhcer(
    success: SuccessCallBack,
    fail: FailCallBack? = null,
    finish: FinishCallBack? = null
) = viewModelScope.httpLauncher(success, fail, finish)

//context网络请求
fun Context.httpLaunhcer(
    success: SuccessCallBack,
    fail: FailCallBack? = null,
    finish: FinishCallBack? = null
) = commonHttpLaunhcer(success, fail, finish).bindLifeObserver(this)

//普通网络请求
fun commonHttpLaunhcer(
    success: SuccessCallBack,
    fail: FailCallBack? = null,
    finish: FinishCallBack? = null
) = object : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main
}.httpLauncher(success, fail, finish)

//协程网络请求
private fun CoroutineScope.httpLauncher(
    success: SuccessCallBack,
    fail: FailCallBack? = null,
    finish: FinishCallBack? = null
): Job {
    return launch {
        try {
            success.invoke()
        } catch (e: Exception) {
            //不是协程主动取消网络请求
            if (e !is CancellationException && e.message != "Canceled") {
                var message = "未知错误"
                if (e is ConnectException || e is UnknownHostException) {
                    var message = "网络连接异常"
                }
                fail?.invoke(e) ?: kotlin.run {
                    showError(e.message ?: message)
                }
            }

        } finally {
            finish?.invoke()
        }
    }
}

fun Job.bindLifeObserver(context: Context) {
    if (context is AppCompatActivity) {
        (context as AppCompatActivity).lifecycle.addObserver(ContextJonFinishLifeObserver(this))
    }
}

//Context 取消协程方式
class ContextJonFinishLifeObserver(val job: Job) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        job.cancel()
    }
}


