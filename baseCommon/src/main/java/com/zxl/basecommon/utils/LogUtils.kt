package com.zxl.basecommon.utils

import android.util.Log
import com.zxl.basecommon.BuildConfig
import java.lang.StringBuilder

/**
 * 作者：吕振鹏
 * E-mail:lvzhenpeng@pansoft.com
 * 创建时间：2020年03月13日
 * 时间：11:07
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
object LogUtils {
    private const val DEFULT_TAG = "FMISApp"
    private val IS_DEBUG: Boolean = BuildConfig.DEBUG

    /**
     * 截断输出日志
     *
     * @param msg
     */
    fun longD(msg: String) {
        val msgStr = StringBuilder(msg)
        if (IS_DEBUG) {
            val tag = DEFULT_TAG
            val segmentSize = 3 * 1024
            val length = msgStr.length.toLong()
            if (length <= segmentSize) { // 长度小于等于限制直接打印
                Log.d(tag, msgStr.toString())
            } else {
                while (msgStr.length > segmentSize) { // 循环分段打印日志
                    val logContent = msgStr.substring(0, segmentSize)
                    //msgStr = msgStr.replace(logContent, "")
                    msgStr.delete(0, segmentSize)
                    Log.d(tag, logContent)
                }
                Log.d(tag, msgStr.toString()) // 打印剩余日志
            }
        }
    }

    fun d(msg: String?, vararg obs: Any?) {
        d(DEFULT_TAG, msg, *obs)
    }

    fun d(tag: String?, msg: String?, vararg obs: Any?) {
        if (IS_DEBUG) {
            if (obs.isEmpty()) {
                Log.d(tag, msg!!)
            } else {
                Log.d(tag, String.format(msg!!, *obs))
            }
        }
    }

    fun e(msg: String?, vararg obs: Any?) {
        e(DEFULT_TAG, msg, *obs)
    }

    fun e(tag: String?, msg: String?, vararg obs: Any?) {
        if (IS_DEBUG) {
            if (obs.isEmpty()) {
                Log.e(tag, msg!!)
            } else {
                Log.e(tag, String.format(msg!!, *obs))
            }
        }
    }
}