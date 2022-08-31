package com.zxl.componentgallery.components.fingergesture.ui

import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pansoft.basecode.binding.BindingAction
import com.pansoft.basecode.binding.BindingCommand
import com.zxl.basecommon.base.BaseViewModel
import com.zxl.basecommon.bus.SingleLiveEvent
import com.zxl.basecommon.sp.PreferencesExt

class GestureLoginViewModel : BaseViewModel() {
    var UserGesturePassword: String by PreferencesExt(
        "UserGesturePassword",
        "",
        "EnvironmentVariable"
    )
    var gesturePassword = ObservableField<String>(UserGesturePassword)

    val gestureResultShow = MutableLiveData(true)

    var uiChange = UIChangeObservable()

    //更多
    var showMoreCommand = BindingCommand<View.OnClickListener>(object : BindingAction {
        override fun call() {
            uiChange.showMoreEvent.value = true
        }

    })

    //关闭页面
    var closeActivityCommand = BindingCommand<View.OnClickListener>(object : BindingAction {
        override fun call() {
            uiChange.closeActivityEvent.value = true
        }
    })

//    fun startLogin() {
//        login(errorBlock = {
//            startActivity(LoginActivity::class.java)
//            finishActivity()
//        })
//    }

    class UIChangeObservable {
        //显示更多登陆方式
        var showMoreEvent = SingleLiveEvent<Boolean>()

        //关闭Activity
        var closeActivityEvent = SingleLiveEvent<Boolean>()
    }

}