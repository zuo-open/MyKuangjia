package com.zxl.mykuangjia.ui.main.home.multilanguage

import android.content.Intent
import com.zxl.basecommon.base.BaseActivity
import com.zxl.basecommon.base.BaseContext
import com.zxl.basecommon.utils.MultiLanguageUtils
import com.zxl.mykuangjia.BR
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.databinding.ActivityMultilanguageBinding
import org.koin.android.ext.android.get

class MultilanguageActivity : BaseActivity<MultilanguageViewModel, ActivityMultilanguageBinding>() {

    companion object {
        const val RESTART_APP = "RESTART_APP"
    }


    override fun obtainViewModel(): MultilanguageViewModel = get()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_multilanguage


    override fun initView() {
        super.initView()
        intent?.takeIf { it.getBooleanExtra(RESTART_APP, false) }?.apply {
            BaseContext.updateApplication()
        }

        mDataBinding.btnChina.setOnClickListener {
            changeLanguage(MultiLanguageUtils.LANGUAGE_CN)
        }
        mDataBinding.btnEnglish.setOnClickListener {
            changeLanguage(MultiLanguageUtils.LANGUAGE_EN)
        }
        mDataBinding.btnRu.setOnClickListener {
            changeLanguage(MultiLanguageUtils.LANGUAGE_RU)
        }


        mViewModel.multilanguageLiveData.observe(this) {
            startActivity(Intent(this, MultilanguageActivity::class.java).apply {
                putExtra(RESTART_APP, true)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }

    }


    fun changeLanguage(language: String) {
        MultiLanguageUtils.saveSelectLanguage(this, language)
        startActivity(Intent(this, MultilanguageActivity::class.java).apply {
            putExtra(RESTART_APP, true)
        })
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out)
        finish()
    }
}