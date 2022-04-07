package com.zxl.basecommon.interfaces

import org.koin.core.module.Module

interface IModules {
    fun getLocalModules(): List<Module>
}