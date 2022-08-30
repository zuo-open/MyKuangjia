package com.zxl.libnetwork.utils

import com.zxl.basecommon.utils.LogUtils.longD
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 作者：吕振鹏
 * E-mail:lvzhenpeng@pansoft.com
 * 创建时间：2019年03月21日
 * 时间：9:31
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */
class LoggingInterceptor : HttpLoggingInterceptor.Logger {
    private val mRequestBuffer = StringBuilder()

    //private val mLogHandler: Handler
    override fun log(message: String) {
        longD(message)
        //val msg = Message.obtain()
        //msg.obj = message
        //mLogHandler.sendMessage(msg)
    }

    companion object {
        private const val NEXT = "[next]"
        fun bindLogging(okhttpBuilder: OkHttpClient.Builder) {
            try {
                val logger =
                    HttpLoggingInterceptor(LoggingInterceptor())
                logger.level = HttpLoggingInterceptor.Level.BODY
                okhttpBuilder.addInterceptor(logger)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    init {
//        val logThread = HandlerThread("LogThread")
//        logThread.start()
//        mLogHandler = object : Handler(logThread.looper) {
//            override fun handleMessage(msg: Message) {
//                try {
//                    val message = msg.obj.toString()
//                    mRequestBuffer.append(message)
//                        .append(NEXT)
//                    if (message.contains("<-- END HTTP")
//                        || message.contains("<-- HTTP FAILED")
//                    ) {
//                        // requestData = mRequestBuffer.toString().toLowerCase(Locale.ROOT)
//                        val requestData = mRequestBuffer.toString()
//                        val requestDataSplit =
//                            requestData.split(NEXT)
//                                .toTypedArray()
//                        val httpRequestData: MutableMap<String, String> =
//                            HashMap()
//                        //用来标识是否为请求体的数据
//                        var isRequest = true
//                        //因为是按照正常的执行流程拼接的，所以不用担心流程错乱
//                        for (requestItem in requestDataSplit) {
//                            if (isRequest) {
//                                if (requestItem.contains("-->") && !requestItem.contains("END")) {
//                                    val responseSplit =
//                                        requestItem.split(" ").toTypedArray()
//                                    val requestType = responseSplit[1]
//                                    httpRequestData["REQUEST_TYPE"] = requestType
//                                    val url = responseSplit[2]
//                                    httpRequestData["URL"] = url
//                                    if ("GET" == requestType) {
//                                        val urlSplit =
//                                            url.split("?").toTypedArray()
//                                        httpRequestData["URL"] = urlSplit[0]
//                                        if (urlSplit.size > 1) {
//                                            httpRequestData["PARAMS"] =
//                                                URLDecoder.decode(urlSplit[1], "utf-8")
//                                        }
//                                    }
//                                } else if (requestItem.contains("--> END")) {
//                                    isRequest = false
//                                } else if (httpRequestData["REQUEST_TYPE"] == "POST" && !requestItem.contains(
//                                        ": "
//                                    )
//                                ) { //如果数据中不包含“：”，可能就是POST请求的请求数据
//                                    httpRequestData["PARAMS"] = requestItem
//                                } else if (requestItem.contains("<-- HTTP FAILED")) {
//                                    httpRequestData["REQUEST_FAILED"] = requestItem
//                                }
//                            } else { //请求体结束后便是响应的数据
//                                if (requestItem.contains("<--")) {
//                                    if (!requestItem.contains("END")) {
//                                        val responseSplit =
//                                            requestItem.split(" ").toTypedArray()
//                                        val useTime =
//                                            responseSplit[responseSplit.size - 1]
//                                                .replace("(", "")
//                                                .replace(")", "")
//                                        httpRequestData["USE_TIME"] = useTime
//                                    }
//                                } else if (!requestItem.contains(": ") && !TextUtils.isEmpty(requestItem)) {
//                                    httpRequestData["RESPONSE"] = requestItem
//                                }
//                            }
//                        }
//                        val table = HttpRequestLogTable()
//                        table.error = httpRequestData["REQUEST_FAILED"]
//                        table.oid = UUID.randomUUID()
//                        table.url = httpRequestData["URL"]
//                        table.response = httpRequestData["RESPONSE"]
//                        table.params = httpRequestData["PARAMS"]
//                        LogUploadManager.getInstance().saveNetLogFile(table)
//                        HttpRequestLogDao.saveHttpLogAsyn(table)
//                        /*longD(
//                            "------需要存入数据库的数据-----" + JSON.toJSONString(
//                                httpRequestData
//                            )
//                        )*/
//                        //e("本次请求为异常信息")
//                        /*if (requestData.contains("\"errorcode\":0") || requestData.contains("\"result\":\"success\"")
//                            || requestData.contains("{\"errcode\":\"0\"}")
//                        ) {
//                            d("本次请求正常")
//                        } else {
//
//                        }
//                        d(
//                            "-----本次请求参数长度 = %s----",
//                            mRequestBuffer.length
//                        )*/
//                        mRequestBuffer.delete(0, mRequestBuffer.length)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
}