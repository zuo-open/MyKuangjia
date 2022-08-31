package com.zxl.basecommon.sp

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.zxl.basecommon.base.BaseContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferencesExt<T>(
    val name: String? = null,
    private val default: T? = null,
    private val prefName: String = "Share_Data"
) : ReadWriteProperty<Any?, T> {

    private val prefs by lazy {
        BaseContext.getApplication().getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putValue(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    fun putValue(name: String? = null, value: T) = with(prefs!!.edit()) {
        var key = name
        if (key == null) {
            key = this@PreferencesExt.name
        }
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key, Gson().toJson(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun getValue(name: String? = null, default: T? = null): T = with(prefs!!) {
        var key = name
        var defaultValue = default
        if (key == null) {
            key = this@PreferencesExt.name
        }
        if (defaultValue == null) {
            defaultValue = this@PreferencesExt.default
        }
        val res: Any = when (defaultValue) {
            is Long -> getLong(key, defaultValue)
            is String -> {
                getString(key, defaultValue) ?: ""
            }
            is Int -> getInt(key, defaultValue)
            is Boolean -> getBoolean(key, defaultValue)
            is Float -> getFloat(key, defaultValue)
            else -> ""
        }
        return res as T
    }

}