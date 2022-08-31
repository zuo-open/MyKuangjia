package com.zxl.mykuangjia.ui.main.home.multilanguage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.base.BaseViewModel
import com.zxl.basecommon.utils.MultiLanguageUtils
import com.zxl.mykuangjia.Apl

class MultilanguageViewModel : BaseViewModel() {


    val multilanguageLiveData = MutableLiveData<Boolean>()


    fun setLanguage(language: String) {
        MultiLanguageUtils.saveSelectLanguage(BaseContext.getApplication(), language)
        multilanguageLiveData.value = true
    }
}