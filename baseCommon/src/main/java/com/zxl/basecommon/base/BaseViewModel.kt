package com.zxl.basecommon.base

import androidx.core.content.ContextCompat
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    var mBaseObserver: BaseEventObserver = BaseEventObserver()


    fun getColor(id: Int) = ContextCompat.getColor(BaseContext.getApplication(), id)
    fun getDrawable(id: Int) = ContextCompat.getDrawable(BaseContext.getApplication(), id)

    fun getString(id: Int) = BaseContext.getApplication().getString(id)

    fun getString(id: Int, vararg args: Any) = BaseContext.getApplication().getString(id, args)

    //加载中
    fun showLoading() {
        mBaseObserver.showLoadingEvent.value = true
    }

    //加载成功
    fun showLoadingSuccess() {
        mBaseObserver.showLoadingSuccessEvent.value = true
    }

    //加载失败
    fun showLoadingFail() {
        mBaseObserver.showLoadingFailEvent.value = true
    }

    //加载空布局
    fun showEmpty() {
        mBaseObserver.showEmptyEvent.value = true
    }

}

class BaseEventObserver {
    //加载中
    var showLoadingEvent = MutableLiveData<Boolean>()

    //加载成功
    var showLoadingSuccessEvent = MutableLiveData<Boolean>()

    //加载失败
    var showLoadingFailEvent = MutableLiveData<Boolean>()

    //加载空数据
    var showEmptyEvent = MutableLiveData<Boolean>()
}