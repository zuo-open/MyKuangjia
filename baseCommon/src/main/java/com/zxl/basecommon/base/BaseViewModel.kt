package com.zxl.basecommon.base

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    fun getColor(id: Int) = ContextCompat.getColor(BaseContext.getApplication(), id)
    fun getDrawable(id: Int) = ContextCompat.getDrawable(BaseContext.getApplication(), id)

    fun getString(id: Int) = BaseContext.getApplication().getString(id)

    fun getString(id: Int, vararg args: Any) = BaseContext.getApplication().getString(id, args)
}