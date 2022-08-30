package com.example.teprinciple.updateappdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.teprinciple.updateapputils.R
import constant.DownLoadBy
import constant.UiType
import demo.CheckMd5DemoActivity
import demo.JavaDemoActivity
import demo.SpanUtils
import kotlinx.android.synthetic.main.activity_updateutils_main.*
import listener.OnBtnClickListener
import listener.OnInitUiListener
import listener.UpdateDownloadListener
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils


class UpdateUtilsMainActivity : AppCompatActivity() {

    private val apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk"

    //    private val apkUrl = "https://github.com/AlexLiuSheng/CheckVersionLib/blob/master/library/src/main/java/com/allenliu/versionchecklib/utils/AppUtils.java"
    private val updateTitle = "发现新版本V2.0.0"
    private val updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updateutils_main)

        UpdateAppUtils.init(this)

        // 基本使用
        btn_basic_use.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateConfig(UpdateConfig(apkSaveName = "up_1.1"))
                .uiConfig(UiConfig(uiType = UiType.SIMPLE))
                .updateContent(updateContent)
                .update()
        }

        // 浏览器下载
        btn_download_by_browser.setOnClickListener {

            // 使用SpannableString
            val content = SpanUtils(this)
                .appendLine("1、Kotlin重构版")
                .appendLine("2、支持自定义UI").setForegroundColor(Color.RED)
                .appendLine("3、增加md5校验").setForegroundColor(Color.parseColor("#88e16531"))
                .setFontSize(20, true)
                .appendLine("4、更多功能等你探索").setBoldItalic()
                .appendLine().appendImage(R.mipmap.img).setBoldItalic()
                .create()

            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(content)
                .updateConfig(UpdateConfig().apply {
                    downloadBy = DownLoadBy.BROWSER
//                     alwaysShow = false
                    serverVersionName = "2.0.0"
                })
                .uiConfig(UiConfig(uiType = UiType.PLENTIFUL))

                // 设置 取消 按钮点击事件
                .setCancelBtnClickListener(object : OnBtnClickListener {
                    override fun onClick(): Boolean {
                        Toast.makeText(
                            this@UpdateUtilsMainActivity,
                            "cancel btn click",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return false // 事件是否消费，是否需要传递下去。false-会执行原有点击逻辑，true-只执行本次设置的点击逻辑
                    }
                })

                // 设置 立即更新 按钮点击事件
                .setUpdateBtnClickListener(object : OnBtnClickListener {
                    override fun onClick(): Boolean {
                        Toast.makeText(
                            this@UpdateUtilsMainActivity,
                            "update btn click",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        return false // 事件是否消费，是否需要传递下去。false-会执行原有点击逻辑，true-只执行本次设置的点击逻辑
                    }
                })

                .update()
        }

        // 自定义UI
        btn_custom_ui.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(UpdateConfig(alwaysShowDownLoadDialog = true))
                .uiConfig(
                    UiConfig(
                        uiType = UiType.CUSTOM,
                        customLayoutId = R.layout.view_update_dialog_custom
                    )
                )
                .setOnInitUiListener(object : OnInitUiListener {
                    override fun onInitUpdateUi(
                        view: View?,
                        updateConfig: UpdateConfig,
                        uiConfig: UiConfig
                    ) {
                        view?.findViewById<TextView>(R.id.tv_update_title)?.text = "版本更新啦"
                        view?.findViewById<TextView>(R.id.tv_version_name)?.text = "V2.0.0"
                        // do more...
                    }
                })
                .update()
        }

        // java使用示例
        btn_java_sample.setOnClickListener {
            startActivity(Intent(this, JavaDemoActivity::class.java))
        }

        // md5校验
        btn_check_md5.setOnClickListener {
            startActivity(Intent(this, CheckMd5DemoActivity::class.java))
        }

        // 高级使用
        btn_higher_level_use.setOnClickListener {
            // ui配置
            val uiConfig = UiConfig().apply {
                uiType = UiType.PLENTIFUL
                cancelBtnText = "下次再说"
                updateLogoImgRes = R.drawable.ic_update
                updateBtnBgRes = R.drawable.bg_btn
                titleTextColor = Color.BLACK
                titleTextSize = 18f
                contentTextColor = Color.parseColor("#88e16531")
            }

            // 更新配置
            val updateConfig = UpdateConfig().apply {
                force = true
                isDebug = true
                checkWifi = true
                isShowNotification = true
                notifyImgRes = R.drawable.ic_logo
                apkSavePath =
                    Environment.getExternalStorageDirectory().absolutePath + "/teprinciple"
                apkSaveName = "teprinciple"
            }

            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(updateConfig)
                .uiConfig(uiConfig)
                .setUpdateDownloadListener(object : UpdateDownloadListener {
                    override fun onStart() {
                    }

                    override fun onDownload(progress: Int) {
                    }

                    override fun onFinish() {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
                .update()
        }
    }
}