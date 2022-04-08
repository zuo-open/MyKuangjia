package com.zxl.basecommon.adapter

import android.view.View
import com.billy.android.loading.Gloading
import com.zxl.basecommon.widgets.GlobalLoadingStatusView

class GlobalAdapter : Gloading.Adapter {

    override fun getView(holder: Gloading.Holder?, convertView: View?, status: Int): View {
        var globalLoadingStatusView: GlobalLoadingStatusView? = null
        if (convertView != null && convertView is GlobalLoadingStatusView) {
            globalLoadingStatusView = convertView
        }
        if (globalLoadingStatusView == null) {
            globalLoadingStatusView = GlobalLoadingStatusView(holder?.context!!,holder?.retryTask)
        }
        globalLoadingStatusView.setStatus(status)
        return globalLoadingStatusView
    }

}