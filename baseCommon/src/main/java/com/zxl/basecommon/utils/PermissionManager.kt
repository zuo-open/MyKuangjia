package com.zxl.basecommon.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

typealias  PermissionCallBack = () -> Unit

/**
 * 权限请求工具
 */
class PermissionManager(val activity: FragmentActivity? = null, val fragment: Fragment? = null) :
    DefaultLifecycleObserver {

    private var mNecessary: Boolean = false
    private var mPermissionList = arrayOf<String>()
    private var mPermitCallBack: PermissionCallBack? = null
    private var mDenayCallBack: PermissionCallBack? = null

    //启动器
    private var launcher: ActivityResultLauncher<Array<out String>>? = null
    private var mActivityLauncher: ActivityResultLauncher<Intent>? = null


    init {
        activity?.apply {
            lifecycle.addObserver(this@PermissionManager)
            //注册请求权限
            registerPermission()
        }

        fragment?.apply {
            lifecycle.addObserver(this@PermissionManager)
            //注册请求权限
            registerPermission()
        }
    }

    /**
     * 请求权限
     * @param necessary 是否需要全部请求
     * @param permissionList 权限列表
     * @param permitCallBack 权限请求后回调
     * @param denayCallBack  权限请求失败回调
     */
    fun requestPermission(
        necessary: Boolean = mNecessary,
        permissionList: Array<String> = mPermissionList,
        permitCallBack: PermissionCallBack? = mPermitCallBack,
        denayCallBack: PermissionCallBack? = mDenayCallBack
    ) {
        mNecessary = necessary
        mPermissionList = permissionList
        mPermitCallBack = permitCallBack
        mDenayCallBack = denayCallBack
        launcher?.launch(mPermissionList)
    }

    //Activity注册权限请求
    private fun FragmentActivity.registerPermission() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                requestMultiPermission(this, it)
            }
        mActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_CANCELED) {
                    requestPermission()
                }
            }
    }

    //Fragment注册权限请求
    private fun Fragment.registerPermission() {
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                requestMultiPermission(requireActivity(), it)
            }
        mActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == AppCompatActivity.RESULT_CANCELED) {
                    requestPermission()
                }
            }
    }


    private fun requestMultiPermission(activity: Activity, permissions: Map<String, Boolean>) {
        //如果不需要全部授权
        if (!mNecessary) {
            mPermitCallBack?.invoke()
        } else {
            //授权通过的权限
            val allowPermissions = permissions.filterValues { it }.mapNotNull { it.key }
            //没有通过授权，但是可以重复请求的权限
            val denayPermissions =
                (permissions - allowPermissions).filterValues { !it }.mapNotNull {
                    it.key
                }.filter {
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                }
            //没有通过授权且不会再次重复请求的权限
            val alwaysDenayPermissions =
                permissions.mapNotNull { it.key } - allowPermissions - denayPermissions
            when {
                //存在不再重复请求的权限
                alwaysDenayPermissions.isNotEmpty() -> {
                    showAlwaysDeniedPermissionDialog(activity, {
                        mDenayCallBack?.invoke()
                    }, {
                        val packageURI = Uri.parse("package:${activity.packageName}")
                        mActivityLauncher?.launch(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        )
                    })
                }
                //存在可以重复请求的权限
                denayPermissions.isNotEmpty() -> {
                    //存在可重复请求的权限
                    showRequestPermissionDialog(activity) {
                        requestPermission()
                    }
                }
                else -> {
                    mPermitCallBack?.invoke()
                }
            }
        }
    }


    private fun showAlwaysDeniedPermissionDialog(
        activity: Activity,
        cancel: (() -> Unit)? = null,
        goSetting: (() -> Unit)? = null
    ) {
        QMUIDialog.MessageDialogBuilder(activity)
            .setMessage("您有未申请通过的权限，将会影响应用的正常使用，请在应用设置页面允许相关应用")
            .setTitle("权限申请")
            .addAction("取消") { dialog, _ ->
                dialog.dismiss()
                cancel?.invoke()
            }.addAction(
                "去申请"
            ) { dialog, _ ->
                dialog.dismiss()
                goSetting?.invoke()
            }.show()
    }

    //弹出请求权限对话框
    private fun showRequestPermissionDialog(activity: Activity, confirm: (() -> Unit)? = null) {
        QMUIDialog.MessageDialogBuilder(activity).setMessage("您有未申请通过的权限，将会影响应用的正常使用，请允许相关权限")
            .setTitle("权限申请")
            .addAction(
                "去申请"
            ) { dialog, _ ->
                dialog.dismiss()
                confirm?.invoke()
            }.show()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        launcher?.unregister()
        mActivityLauncher?.unregister()
    }

}

