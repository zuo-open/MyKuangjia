package com.zxl.mykuangjia.ui.main.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pansoft.basecode.binding.BindingAction
import com.pansoft.basecode.binding.BindingCommand
import com.zxl.basecommon.base.BaseViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    val btnClick1 = MutableLiveData<Boolean>()
    val btnClick2 = MutableLiveData<Boolean>()
    val btnClick3 = MutableLiveData<Boolean>()
    val btnClick4 = MutableLiveData<Boolean>()
    val btnClick5 = MutableLiveData<Boolean>()
    val btnClick6 = MutableLiveData<Boolean>()
    val btnClick7 = MutableLiveData<Boolean>()
    val btnClick8 = MutableLiveData<Boolean>()
    val btnClick9 = MutableLiveData<Boolean>()
    var btnClick10 = MutableLiveData<Boolean>()
    var btnClick11 = MutableLiveData<Boolean>()
    var btnClick12 = MutableLiveData<Boolean>()
    var btnClick13 = MutableLiveData<Boolean>()
    var btnClick14 = MutableLiveData<Boolean>()
    var btnClick15 = MutableLiveData<Boolean>()
    var btnClick16 = MutableLiveData<Boolean>()
    var btnClick17 = MutableLiveData<Boolean>()

    var a = MutableStateFlow(1)
    var b = MutableStateFlow(2)
    var c = a.combine(b) { a, b -> a + b }

    //权限请求
    var btn1Click = BindingCommand<Void>(object : BindingAction {
        override fun call() {
            btnClick1.value = true
        }
    })

    //点赞效果
    fun btnClick2() {
        btnClick2.value = true
    }

    //骨架屏
    fun btnClick3() {
        btnClick3.value = true
    }

    //gloading
    fun btnClick4() {
        btnClick4.value = true
    }

    //新手引导
    fun btnClick5() {
        btnClick5.value = true
    }

    //ImageWatcher
    fun btnClick6() {
        btnClick6.value = true
    }

    //ticker
    fun btnClick7() {
        btnClick7.value = true
    }

    //expanableTextView
    fun btnClick8() {
        btnClick8.value = true
    }

    //updateUtils
    fun btnClick9() {
        btnClick9.value = true
    }

    //网络请求
    fun btnClick10() {
        btnClick10.value = true
    }

    //网络请求
    fun btnClick11() {
        btnClick11.value = true
    }

    //手势识别
    fun btnClick12() {
        btnClick12.value = true
    }

    //二维码扫描
    fun btnClick13() {
        btnClick13.value = true
    }

    //AgentWeb
    fun btnClick14() {
        btnClick14.value = true
    }

    //指纹识别
    fun btnClick15() {
        btnClick15.value = true
    }

    //kotlin协程-flow
    fun btnClick16() {
        btnClick16.value = true
    }

    //mvidemo
    fun btnClick17() {
        btnClick17.value = true
    }

    fun testFlow() {
        viewModelScope.launch {
            val reduce = flowOf(1, 2, 3).onStart {
                Log.e("flow", "onStart1")
            }.onStart {
                Log.e("flow", "onStart2")
            }.onCompletion {
                Log.e("flow", "onCompletion1")
            }.onCompletion {
                Log.e("flow", "onCompletion2")
            }.reduce { accumulator, value ->
                Log.e("flow", "$accumulator---$value")
                accumulator + value
            }
            Log.e("flow", "result:$reduce")


        }
    }


}


interface interfaceA {
    fun a(a: interfaceB)
}


interface interfaceB {
    fun b()
}


fun test123(param: interfaceB.() -> Unit) {

}