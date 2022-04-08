package com.zxl.basecommon.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.billy.android.loading.Gloading.*
import com.bumptech.glide.Glide
import com.zxl.basecommon.R
import com.zxl.basecommon.databinding.LayoutGlobalLoadingBinding

class GlobalLoadingStatusView(context: Context, val retryTask: Runnable) : RelativeLayout(context),
    View.OnClickListener {

    private var mDataBinding = DataBindingUtil.inflate<LayoutGlobalLoadingBinding>(
        LayoutInflater.from(context),
        R.layout.layout_global_loading,
        this,
        true
    )

    init {
        Glide.with(context).load(R.drawable.global_loading).into(mDataBinding.globalLoading)
        mDataBinding.clLoadEmpty.setOnClickListener(this)
        mDataBinding.clLoadError.setOnClickListener(this)
    }


    fun setStatus(status: Int) {
        var visibility = true
        when (status) {
            //加载成功
            STATUS_LOAD_SUCCESS -> visibility = false
            //正在加载
            STATUS_LOADING -> {
                mDataBinding.llLoading.visibility = View.VISIBLE
                mDataBinding.clLoadError.visibility = View.GONE
                mDataBinding.clLoadEmpty.visibility = View.GONE

            }
            //加载失败
            STATUS_LOAD_FAILED -> {
                mDataBinding.clLoadError.visibility = View.VISIBLE
                mDataBinding.llLoading.visibility = View.GONE
                mDataBinding.clLoadEmpty.visibility = View.GONE
            }

            //数据为空
            STATUS_EMPTY_DATA -> {
                mDataBinding.clLoadEmpty.visibility = View.VISIBLE
                mDataBinding.clLoadError.visibility = View.GONE
                mDataBinding.llLoading.visibility = View.GONE
            }
        }
        //设置是否显示
        setVisibility(if (visibility) View.VISIBLE else View.GONE)
    }

    override fun onClick(v: View) {
        if (retryTask != null) {
            retryTask.run()
        }
    }

}