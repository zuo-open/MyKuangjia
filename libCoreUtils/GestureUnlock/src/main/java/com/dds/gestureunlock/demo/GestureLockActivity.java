package com.dds.gestureunlock.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dds.gestureunlock.R;

public class GestureLockActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnSetGesturePassword;
    private AppCompatButton btnUpdateGesturePassword;
    private AppCompatButton btnVerifyGesturePassword;
    public static final int TYPE_GESTURE_CREATE = 1;
    public static final int TYPE_GESTURE_VERIFY = 2;
    public static final int TYPE_GESTURE_MODIFY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        btnSetGesturePassword = findViewById(R.id.btn_setGuesturePassword);
        btnUpdateGesturePassword = findViewById(R.id.btn_updateGuesturePassword);
        btnVerifyGesturePassword = findViewById(R.id.btn_verifyGuesturePassword);
        btnSetGesturePassword.setOnClickListener(this);
        btnUpdateGesturePassword.setOnClickListener(this);
        btnVerifyGesturePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_setGuesturePassword) {
            GestureUnlock.getInstance().init(this.getApplicationContext());
            GestureUnlock.getInstance().createGestureUnlock(this);
        } else if (id == R.id.btn_updateGuesturePassword) {
            GestureUnlock.getInstance().init(this.getApplicationContext());
            GestureUnlock.getInstance().modifyGestureUnlock(this);
        } else if (id == R.id.btn_verifyGuesturePassword) {
            GestureUnlock.getInstance().init(this.getApplicationContext());
            GestureUnlock.getInstance().verifyGestureUnlock(this);
        }
    }
}