package com.zxl.mykuangjia

import com.zxl.basecommon.http.BaseRetrofit
import com.zxl.componentgallery.components.loading.LoadingViewModel
import com.zxl.mykuangjia.ui.main.component.ComponentViewModel
import com.zxl.mykuangjia.ui.main.home.HomeViewModel
import com.zxl.mykuangjia.ui.main.home.http.Api
import com.zxl.mykuangjia.ui.main.home.http.HttpRequestViewModel
import com.zxl.mykuangjia.ui.main.home.multilanguage.MultilanguageViewModel
import com.zxl.mykuangjia.ui.main.service.ServiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModules = module {
    viewModel { MainViewModel() }
    viewModel { HomeViewModel() }
    viewModel { ComponentViewModel() }
    viewModel { ServiceViewModel() }
    viewModel { LoadingViewModel() }
    viewModel { HttpRequestViewModel(get()) }
    viewModel { MultilanguageViewModel() }
}

val httpModules = module {
    single { BaseRetrofit() }
    single { get<BaseRetrofit>().getService(Api::class.java) }
}

val aplModule = listOf(viewModules, httpModules)