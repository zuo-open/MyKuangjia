package com.zxl.componentgallery

import com.zxl.componentgallery.components.morefunctions.MoreFunctionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModules = module {
    viewModel { MoreFunctionViewModel() }
}

val aplModule = listOf(viewModules)