package com.zxl.mykuangjia.ui.main.demo.kotlinDemo.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.zxl.mykuangjia.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class FlowDemo1Activity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var et1: EditText
    private lateinit var btn1: AppCompatButton;
    private var mainScope = MainScope();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_demo1)
        et1 = findViewById(R.id.et1)
        btn1 = findViewById(R.id.btn1)
        GlobalScope.launch {

            et1.searchKey().filter {
                it.isNotBlank()
            }.debounce(300).flatMapLatest {
                requestSearchKeyFlow(it)
            }.flowOn(Dispatchers.IO).collect {
                Log.e("flow", it.joinToString(","))
            }
        }

        btn1.onClickListener(this, object : OnClickCallBack {
            override fun onClick(view: View) {
                Log.e("flow", "点击了")
            }
        })


    }
}

fun EditText.searchKey() = callbackFlow<String> {
    val textWatch = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.also {
                trySend(it.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    addTextChangedListener(textWatch)
    awaitClose { removeTextChangedListener(textWatch) }
}

fun requestSearchKeyFlow(str: String) = flow {
    Log.e("flow", "请求参数:$str")
    delay(300)
    emit(requestSearchKey(str))
}


fun requestSearchKey(str: String): List<String> {
    return arrayListOf(str, str, str)
}


//防止重复点击
fun View.onClickListener(mainScope: CoroutineScope, callBack: OnClickCallBack) {
    callbackFlow<View> {
        setOnClickListener {
            trySend(it)
        }
        awaitClose { setOnClickListener(null) }
    }.clickThrottleFirst().onEach {
        callBack.onClick(this)
    }.launchIn(mainScope)
}


fun <T> Flow<T>.clickThrottleFirst() = flow<T> {
    var lastTime = 0L // 上次发射数据的时间
    // 收集数据
    collect { upstream ->
        // 当前时间
        val currentTime = System.currentTimeMillis()
        // 时间差超过阈值则发送数据并记录时间
        if (currentTime - lastTime > 1000) {
            lastTime = currentTime
            emit(upstream)
        }
    }
}


interface OnClickCallBack {
    fun onClick(view: View)
}

