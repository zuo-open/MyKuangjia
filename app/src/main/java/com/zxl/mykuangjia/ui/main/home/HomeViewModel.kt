package com.zxl.mykuangjia.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pansoft.basecode.binding.BindingAction
import com.pansoft.basecode.binding.BindingCommand
import com.zxl.basecommon.base.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val btnClick1 = MutableLiveData<Boolean>()
    val btnClick2 = MutableLiveData<Boolean>()
    val btnClick3 = MutableLiveData<Boolean>()
    val btnClick4 = MutableLiveData<Boolean>()
    val btnClick5 = MutableLiveData<Boolean>()
    val btnClick6 = MutableLiveData<Boolean>()

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
}