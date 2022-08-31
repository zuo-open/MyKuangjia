package com.zxl.basecommon.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.zxl.basecommon.sp.PreferencesExt
import java.util.*

/**
 * 多语言设置
 */
object MultiLanguageUtils {

    //中文
    const val LANGUAGE_CN = "zh_CN"

    //英语
    const val LANGUAGE_EN = "en_US"

    //俄语
    const val LANGUAGE_RU = "ru_RU"

    private var mCurrentLanguage by PreferencesExt(
        "MultilingualLanguageNew", "", "EnvironmentVariable"
    )

    /**
     * 更新多语言设置
     */
    fun saveSelectLanguage(context: Context, select: String) {
        mCurrentLanguage = select
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            setConfiguration(context)
    }

    fun attachBaseContext(context: Context): Context {

        //设置默认
        if (mCurrentLanguage == "") {
            mCurrentLanguage = when (getSystemLocale().language) {
                Locale.CHINA.language -> LANGUAGE_CN
                Locale.ENGLISH.language -> LANGUAGE_EN
                "ru" -> LANGUAGE_RU
                else -> LANGUAGE_CN
            }
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createConfigurationResources(context)
        } else {
            setConfiguration(context)
            context
        }
    }

    /**
     * 获取选择的语言设置
     *
     * @return
     */
    private fun getSetLanguageLocale() = when (mCurrentLanguage) {
        LANGUAGE_CN -> Locale.CHINA
        LANGUAGE_EN -> Locale.ENGLISH
        LANGUAGE_RU -> Locale("RU")
        else -> getSystemLocale()
    }

    /**
     * 获取系统的locale
     *
     * @return Locale对象
     */
    private fun getSystemLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun createConfigurationResources(context: Context): Context {
        val resources = context.resources
        val configuration = resources.configuration
        val locale: Locale = getSetLanguageLocale()
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    /**
     * 设置语言
     */
    private fun setConfiguration(context: Context) {
        val targetLocale: Locale = getSetLanguageLocale()
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(targetLocale)
        val resources: Resources = context.resources
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}