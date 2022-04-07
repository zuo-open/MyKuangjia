package com.zxl.mykuangjia.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pansoft.basecode.binding.BindingAction
import com.pansoft.basecode.binding.BindingCommand

class HomeViewModel : ViewModel() {

    val btnClick1 = MutableLiveData<Boolean>()
    val btnClick2 = MutableLiveData<Boolean>()
    val btnClick3 = MutableLiveData<Boolean>()

    var btn1Click = BindingCommand<Void>(object : BindingAction {
        override fun call() {
            btnClick1.value = true
        }
    })

    fun btnClick2() {
        btnClick2.value = true
    }

    fun btnClick3() {
        btnClick3.value = true
    }
}