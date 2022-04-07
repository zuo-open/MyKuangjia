package com.zxl.mykuangjia

import com.zxl.componentgallery.components.morefunctions.MoreFunctionViewModel
import com.zxl.mykuangjia.ui.main.component.ComponentViewModel
import com.zxl.mykuangjia.ui.main.home.HomeViewModel
import com.zxl.mykuangjia.ui.main.service.ServiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModules = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel() }
    viewModel { ComponentViewModel() }
    viewModel { ServiceViewModel() }
}

val aplModule = listOf(viewModules)