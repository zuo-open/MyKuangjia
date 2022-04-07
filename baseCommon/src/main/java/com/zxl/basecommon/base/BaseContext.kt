package com.zxl.basecommon.base

import android.content.Context
import kotlin.properties.Delegates

class BaseContext {

    companion object {
        private var context: Context by Delegates.notNull()
        fun getApplication(): Context {
            return context
        }

        fun setApplication(context: Context) {
            this.context = context
        }

    }
}