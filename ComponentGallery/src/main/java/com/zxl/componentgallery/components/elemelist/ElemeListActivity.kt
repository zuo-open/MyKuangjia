package com.zxl.componentgallery.components.elemelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.zxl.componentgallery.R

class ElemeListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBtn1: AppCompatButton
    private lateinit var mBtn2: AppCompatButton
    private lateinit var mBtn3: AppCompatButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleme_list)
        mBtn1 = findViewById(R.id.btn1)
        mBtn2 = findViewById(R.id.btn2)
        mBtn3 = findViewById(R.id.btn3)
        mBtn1.setOnClickListener(this)
        mBtn2.setOnClickListener(this)
        mBtn3.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn1 -> {
                Intent(this, ElemeListActivity1::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.btn2 -> {
            }
            R.id.btn3 -> {
            }
        }
    }
}