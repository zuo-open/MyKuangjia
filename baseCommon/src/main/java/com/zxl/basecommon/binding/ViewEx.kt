package com.zxl.basecommon.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding4.view.clicks
import com.pansoft.basecode.binding.BindingCommand
import java.util.concurrent.TimeUnit


object ViewEx {
    const val CLICK_INTERVAL = 300L

    @JvmStatic
    @BindingAdapter(value = ["onClickCommand"], requireAll = false)
    fun onClickCommand(view: View, clickCommand: BindingCommand<*>?) {
        view.clicks()
            .throttleFirst(CLICK_INTERVAL, TimeUnit.MILLISECONDS)
            .subscribe { clickCommand?.execute() }

    }
}