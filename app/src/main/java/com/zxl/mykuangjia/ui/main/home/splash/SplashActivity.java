package com.zxl.mykuangjia.ui.main.home.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zxl.basecommon.base.BaseContext;
import com.zxl.mykuangjia.MainActivity;
import com.zxl.mykuangjia.R;

import java.io.File;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private SplashDataBean splashDataBean = null;
    private ImageView ivSplash;
    private TextView tvCountDown;
    private static final String TAG = "SplashActivity";
    private Handler handler = new Handler(Looper.getMainLooper());
    private CountDownTimer countDownTimer = new CountDownTimer(3400, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvCountDown.setText("请稍后" + millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            countDownTimer.cancel();
            startMainActivity();
        }
    };

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash = findViewById(R.id.iv_splash);
        tvCountDown = findViewById(R.id.tv_countDown);
        ivSplash.setOnClickListener(this);
        //加载本地闪屏页数据
        loadLocalSplashData();
        //获取闪屏页数据
        loadSplashFromNet();
    }

    private void loadSplashFromNet() {
        startService(new Intent(this, LoadSplashDataService.class));
    }

    //加载本地欢迎页数据
    private void loadLocalSplashData() {
        splashDataBean = getLocalSplashData();
        if (splashDataBean != null) {
            //获取到闪屏页数据
            File file = new File(BaseContext.Companion.getApplication().getExternalFilesDir("") + "/splash", splashDataBean.getFileName());
            if (file.exists()) {
                Glide.with(this).load(file).into(ivSplash);
                countDownTimer.start();
            }


        } else {
            //没有获取到闪屏页数据
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2500);
        }
    }

    //获取本地闪屏页数据
    private SplashDataBean getLocalSplashData() {
        SplashDataBean data = null;
        try {
            String splashDataStr = SPUtils.getInstance().getString(LoadSplashDataService.SPLASH_DATA, "");
            Gson gson = new Gson();
            if (!TextUtils.isEmpty(splashDataStr)) {
                data = gson.fromJson(splashDataStr, SplashDataBean.class);

            } else {

            }


        } catch (Exception e) {
            Log.e(TAG, "获取本地闪屏页数据失败" + e.getMessage());
            e.printStackTrace();

        }
        return data;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_splash) {
            if (splashDataBean != null) {
                ToastUtils.showShort("跳转");
            }
        }
    }

}