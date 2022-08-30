package com.zxl.servicemodule

import com.zxl.basecommon.interfaces.IModules
import org.koin.core.module.Module


class ServiceModules : IModules {

    override fun getLocalModules(): List<Module> =
        listOf(viewModules)
}