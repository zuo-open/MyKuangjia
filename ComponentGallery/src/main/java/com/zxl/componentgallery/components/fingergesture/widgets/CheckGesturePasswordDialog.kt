package com.zxl.componentgallery.components.fingergesture.widgets

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.zxl.componentgallery.R
import com.zxl.componentgallery.databinding.PopupGesturePasswordCheckBinding


class CheckGesturePasswordDialog(context: Context) : Dialog(context, R.style.Dialog) {

    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_gesture_password_check)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.apply {
            val attributes = this.attributes
            val outSize = Point()
            this.windowManager.defaultDisplay.getSize(outSize)
            attributes.width = (outSize.x * 0.8).toInt()
            this.attributes = attributes
            initView()
        }

    }

    private fun initView() {
        tvCancel = findViewById(R.id.tv_gesture_cancel)
        tvConfirm = findViewById(R.id.tv_gesture_sure)
        //取消
        tvCancel.setOnClickListener {
            dismiss()
        }
        //确定
        tvConfirm.setOnClickListener {
//            context.startActivity(Intent(context, LoginActivity::class.java))
            dismiss()
        }
    }


}




