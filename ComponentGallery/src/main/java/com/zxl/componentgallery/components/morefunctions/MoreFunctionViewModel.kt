package com.zxl.componentgallery.components.morefunctions

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.base.BaseViewModel

class MoreFunctionViewModel : BaseViewModel() {


    fun sayHello() {
        Toast.makeText(BaseContext.getApplication(), "这个事情成功了", Toast.LENGTH_SHORT).show()
    }
}