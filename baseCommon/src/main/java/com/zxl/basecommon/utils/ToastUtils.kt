package com.zxl.basecommon.utils

import android.content.Context
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.utils.ToastUtils.TAG_ERROR
import com.zxl.basecommon.utils.ToastUtils.TAG_INF0
import com.zxl.basecommon.utils.ToastUtils.TAG_NORMAL
import com.zxl.basecommon.utils.ToastUtils.TAG_SUCCESS
import com.zxl.basecommon.utils.ToastUtils.TAG_WARNING
import es.dmoral.toasty.Toasty

fun showError(message: String) {
    ToastUtils.showToast(message, TAG_ERROR)
}

fun showSuccess(message: String) {
    ToastUtils.showToast(message, TAG_SUCCESS)
}

fun showToast(message: String) {
    ToastUtils.showToast(message, TAG_NORMAL)
}

fun showWaring(message: String) {
    ToastUtils.showToast(message, TAG_WARNING)
}

fun showInfo(message: String) {
    ToastUtils.showToast(message, TAG_INF0)
}


//吐司工具类
object ToastUtils {
    const val TAG_SUCCESS = "TAG_SUCCESS"
    const val TAG_ERROR = "TAG_ERROR"
    const val TAG_INF0 = "TAG_INF0"
    const val TAG_WARNING = "TAG_WARNING"
    const val TAG_NORMAL = "TAG_NORMAL"
    fun showToast(message: String, type: String, duration: Int = 1500) {
        when (type) {
            TAG_SUCCESS -> Toasty.success(BaseContext.getApplication(), message).show()
            TAG_ERROR -> Toasty.error(BaseContext.getApplication(), message).show()
            TAG_INF0 -> Toasty.info(BaseContext.getApplication(), message).show()
            TAG_WARNING -> Toasty.warning(BaseContext.getApplication(), message).show()
            TAG_NORMAL -> Toasty.normal(BaseContext.getApplication(), message).show()
        }
    }
}