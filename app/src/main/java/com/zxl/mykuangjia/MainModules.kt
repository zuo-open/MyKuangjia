package com.zxl.mykuangjia

import com.zxl.basecommon.interfaces.IModules
import com.zxl.componentgallery.components.morefunctions.MoreFunctionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module




class MainModules : IModules {

    override fun getLocalModules(): List<Module> =
        listOf(viewModules)
}