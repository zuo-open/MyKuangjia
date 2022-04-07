package com.zxl.mykuangjia

import android.app.Application
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.interfaces.IModules
import com.zxl.componentgallery.ComponentGalleryModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import java.lang.Exception


class Apl : Application() {

    val modules = listOf(ComponentGalleryModules::class.java,MainModules::class.java)


    override fun onCreate() {
        super.onCreate()
        BaseContext.setApplication(this)
        initKoin()
    }

    //koin配置
    private fun initKoin() {
        val initializeModules = initializeModules(modules)
        startKoin {
            printLogger()
            androidContext(this@Apl)
            modules(initializeModules)
        }
    }

    private fun initializeModules(list: List<Class<out IModules>>): List<Module> {
        val modules = mutableListOf<Module>()
        list.forEach {
            try {
                it.newInstance().getLocalModules().apply {
                    modules.addAll(this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return modules
    }
}