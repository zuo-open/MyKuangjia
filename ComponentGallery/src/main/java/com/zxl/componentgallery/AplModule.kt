package com.zxl.componentgallery


import com.zxl.componentgallery.components.fingergesture.ui.GestureLoginViewModel
import com.zxl.componentgallery.components.fingergesture.ui.LoginTypeViewModel
import com.zxl.componentgallery.components.morefunctions.MoreFunctionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModules = module {
    viewModel { MoreFunctionViewModel() }
    viewModel { GestureLoginViewModel() }
    viewModel { LoginTypeViewModel() }
}


val aplModule = listOf(viewModules)