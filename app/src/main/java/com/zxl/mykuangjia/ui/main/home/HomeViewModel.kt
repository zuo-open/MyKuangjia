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
    val btnClick7 = MutableLiveData<Boolean>()
    val btnClick8 = MutableLiveData<Boolean>()
    val btnClick9 = MutableLiveData<Boolean>()
    var btnClick10 = MutableLiveData<Boolean>()
    var btnClick11 = MutableLiveData<Boolean>()
    var btnClick12 = MutableLiveData<Boolean>()
    var btnClick13 = MutableLiveData<Boolean>()

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

}