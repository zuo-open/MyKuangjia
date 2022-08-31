package com.zxl.componentgallery.components.fingergesture.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.animation.LinearInterpolator
import com.zxl.basecommon.base.BaseActivity
import com.zxl.basecommon.sp.PreferencesExt
import com.zxl.basecommon.utils.showToast
import com.zxl.componentgallery.BR
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.fingergesture.widgets.CheckGesturePasswordDialog
import com.zxl.componentgallery.components.fingergesture.widgets.GestureLockLayout
import com.zxl.componentgallery.databinding.ActivityGestureLoginBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.text.SimpleDateFormat
import java.util.*


class GestureLoginActivity : BaseActivity<GestureLoginViewModel, ActivityGestureLoginBinding>() {

    //private lateinit var loginType: LoginTypeDialog
    private var isCheckLogin = false


    var UserGesturePassword: String by PreferencesExt(
        "UserGesturePassword",
        "",
        "EnvironmentVariable"
    )
    var ShowGesture by PreferencesExt("ShowGesture", true, "EnvironmentVariable")
    var LoginType by PreferencesExt("LoginMode", BASE_LOGIN, "EnvironmentVariable")


    //检验出错
    val checkGesturePassword: CheckGesturePasswordDialog by lazy {
        CheckGesturePasswordDialog(this)
    }

    companion object {
        private const val TYPE_RESET = 1
        private const val TYPE_CHECK = 2

        //修改
        private const val TYPE_UPDATE_PASSWORD = 3
        private var currentType = 2

        //检查手势密码
        fun startActivityForCheck(context: Context) {
            currentType = TYPE_CHECK
            context.startActivity(Intent(context, GestureLoginActivity::class.java))
        }

        //设置手势密码
        fun startActivityForReset(context: Context) {
            currentType = TYPE_RESET
            context.startActivity(Intent(context, GestureLoginActivity::class.java))
        }

        //重置手势密码
        fun startActivityForUpdate(context: Context) {
            currentType = TYPE_UPDATE_PASSWORD
            context.startActivity(Intent(context, GestureLoginActivity::class.java))
        }

        private const val FINGERPRINT_LOGIN: String = "FingerprintLogin"
        private const val GESTURE_LOGIN: String = "GestureLogin"
        private const val BASE_LOGIN: String = "BaseLogin"

    }

    override fun obtainViewModel(): GestureLoginViewModel = getViewModel()

    override fun initViewModelId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_gesture_login


    override fun initDefaultObserver() {
        super.initDefaultObserver()

        //显示更多登陆方式
        mViewModel.uiChange.showMoreEvent.observe(this) {
//            loginType.show()
        }

        mViewModel.gestureResultShow.value = currentType != TYPE_CHECK

        //关闭页面
        mViewModel.uiChange.closeActivityEvent.observe(this) {
//            if (currentType == TYPE_RESET) {
//                EventBus.getDefault().postSticky(GestureEvent(false))
//                finish()
//            } else if (currentType == TYPE_CHECK && isCheckLogin) {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            } else if (currentType == TYPE_UPDATE_PASSWORD) {
//                EventBus.getDefault().postSticky(GestureEvent(true))
//                finish()
//            } else {
//                EventBus.getDefault().postSticky(GestureEvent(false))
//                finish()
//            }
        }

    }

    override fun initData() {
        super.initData()
        val extras = intent.extras
        extras?.apply {
            isCheckLogin = this.getBoolean("checkLogin")
        }

        if (currentType == TYPE_CHECK) {
            mDataBinding.gestureLockLayout.showDraw = ShowGesture
        }

        //loginType = LoginTypeDialog(this)
        //检验手势密码
        if (currentType == TYPE_CHECK) {
            if (!TextUtils.isEmpty(mViewModel.gesturePassword.get())) {
                mDataBinding.tvGestureContent.text = getTimeStr()
                mDataBinding.gestureLockLayout.setCheckPasswordMode(mViewModel.gesturePassword.get()!!)
            }
        } else if (currentType == TYPE_RESET || currentType == TYPE_UPDATE_PASSWORD) {
            //手势密码重置
            mDataBinding.tvGestureDesc.text = GestureLockLayout.TextHolder.RESET_PASSWORD
            mDataBinding.gestureLockLayout.setResetMode()
            mDataBinding.tvGestureContent.text = GestureLockLayout.TextHolder.REST_PASSWORD
            mDataBinding.tvGestureMore.visibility = View.GONE
        }

        mDataBinding.gestureLockLayout.setOnGestureListener(object :
            GestureLockLayout.OnGestureListener {
            //本次绘制的手势密码
            override fun onPasswordFinish(password: List<Int>) {
                mDataBinding.gllShow.refreshPwdKeyboard(password) //通知另一个小密码盘，将密码点展示出来，但是不展示轨迹线
            }

            override fun overResetCount(maxRestCount: Int) {

            }

            override fun sendMessage(content: String, color: Int, showAnimator: Boolean) {
                mDataBinding.tvGestureDesc.setTextColor(color)
                mDataBinding.tvGestureDesc.text = content
                if (showAnimator) {
                    //设置摇摆动画
                    animate(mDataBinding.tvGestureDesc)
                }
            }

            //重置密码成功
            override fun resetPasswordSuccess(list: List<Int>) {
                val password = StringBuffer()
                list.forEach {
                    password.append(it)
                }
                if (currentType == TYPE_RESET) {
                    LoginType =
                        "${LoginType},${GESTURE_LOGIN}"
                }
                UserGesturePassword = password.toString()
                showToast("重置手势密码成功")
                finish()
            }

            //检验手势密码成功
            override fun checkGesturePasswordSuccess() {
                if (currentType == TYPE_RESET) {
                    mDataBinding.tvGestureContent.text = GestureLockLayout.TextHolder.REST_PASSWORD
                    mDataBinding.gestureLockLayout.setResetMode()
                } else {
                    //登陆
                    showToast("手势密码验证成功")
                    finish()
                }

            }

            //超过最大检验次数
            override fun overMaxCheckCount() {
                checkGesturePassword.show()
                checkGesturePassword.setOnDismissListener { finish() }
            }

        })

    }


    /**
     * 文案的左右摇动动画
     */
    private fun animate(tv_go: View) {
        val objectAnimator = ObjectAnimator.ofFloat(tv_go, "translationX", -20f, 20f, -20f, 0f)
        objectAnimator.duration = 300
        objectAnimator.interpolator = LinearInterpolator()
        /*   objectAnimator.addListener(object : AnimatorListenerAdapter() {
               override fun onAnimationEnd(animation: Animator) {
                   super.onAnimationEnd(animation)
               }
           })*/
        objectAnimator.start()
    }


    @SuppressLint("SimpleDateFormat")
    private fun getTimeStr(): String {
        val dateFormat = SimpleDateFormat("HH")
        when (dateFormat.format(Date()).toInt()) {
            in 0..12 -> {
                return resources.getString(R.string.text_morning)
            }
            in 13..18 -> {
                return resources.getString(R.string.text_aftermoon)
            }
            in 19..23 -> {
                return resources.getString(R.string.text_evening)
            }
        }
        return ""
    }

    class GestureEvent(var flag: Boolean)


}
