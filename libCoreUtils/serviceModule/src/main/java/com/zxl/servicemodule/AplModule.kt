package com.zxl.servicemodule

import com.zxl.servicemodule.ui.aop.AspectJViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModules = module {
    viewModel { AspectJViewModel() }
}

val aplModule = listOf(viewModules)