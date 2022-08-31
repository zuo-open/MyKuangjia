package com.zxl.basecommon.base

import android.app.Application
import android.content.Context
import com.zxl.basecommon.utils.MultiLanguageUtils
import kotlin.properties.Delegates

open class BaseContext : Application() {

    companion object {
        private var context: Context by Delegates.notNull()
        fun getApplication(): Context {
            return context
        }

        fun setApplication(context: Context) {
            this.context = context
            this.context = MultiLanguageUtils.attachBaseContext(context)
        }

        fun updateApplication() {
            this.context = MultiLanguageUtils.attachBaseContext(context)
        }

    }
}