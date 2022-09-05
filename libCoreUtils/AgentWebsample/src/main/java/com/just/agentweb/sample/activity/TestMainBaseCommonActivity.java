package com.just.agentweb.sample.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.just.agentweb.sample.R;
import com.just.agentweb.sample.app.App;

public class TestMainBaseCommonActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnCommon;//一般调用
    private AppCompatButton btnH5DownLoad;//文件下载
    private AppCompatButton btnCallJs;//调用h5js方法
    private AppCompatButton btnCustomProgress;//自定义进度条
    private AppCompatButton btnCallAndroidNativeMethod;//调用原生方法
    private AppCompatButton btnFragmentCommon;//一般使用(fragment)
    private AppCompatButton btnFragmentH5download;//文件下载
    private AppCompatButton btnFragmentH5InputUpload;//H5文件上传
    private AppCompatButton btnFragmentCallJs;//调用h5js方法
    private AppCompatButton btnFragmentPlayVideoFullscreen;//全屏播放视频
    private AppCompatButton btnFragmentCustomProgress;//自定义进度条
    private AppCompatButton btnFragmentCallAndroidNativeMethod;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main_base_common);
        btnCommon = findViewById(R.id.btn_common);
        btnH5DownLoad = findViewById(R.id.btn_h5download);
        btnCallJs = findViewById(R.id.btn_callJs);
        btnCustomProgress = findViewById(R.id.btn_customProgress);
        btnCallAndroidNativeMethod = findViewById(R.id.btn_callAndroidNativeMethod);
        btnFragmentCommon = findViewById(R.id.btn_fragment_common);
        btnFragmentH5download = findViewById(R.id.btn_fragment_h5download);
        btnFragmentH5InputUpload = findViewById(R.id.btn_fragment_h5inputUpload);
        btnFragmentCallJs = findViewById(R.id.btn_fragment_callJs);
        btnFragmentPlayVideoFullscreen = findViewById(R.id.btn_fragment_play_video_fullscreen);
        btnFragmentCustomProgress = findViewById(R.id.btn_fragment_customProgress);
        btnFragmentCallAndroidNativeMethod = findViewById(R.id.btn_fragment_callAndroidNativeMethod);
        btnCommon.setOnClickListener(this);
        btnH5DownLoad.setOnClickListener(this);
        btnCallJs.setOnClickListener(this);
        btnCustomProgress.setOnClickListener(this);
        btnCallAndroidNativeMethod.setOnClickListener(this);
        btnFragmentCommon.setOnClickListener(this);
        btnFragmentH5download.setOnClickListener(this);
        btnFragmentH5InputUpload.setOnClickListener(this);
        btnFragmentCallJs.setOnClickListener(this);
        btnFragmentPlayVideoFullscreen.setOnClickListener(this);
        btnFragmentCustomProgress.setOnClickListener(this);
        btnFragmentCallAndroidNativeMethod.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_common) {
            Intent intent = new Intent(this, TestBaseCommonActivity.class);
            intent.putExtra("url", "https://www.jd.com/");
            startActivity(intent);
        } else if (id == R.id.btn_h5download) {
            Intent intent = new Intent(this, TestBaseCommonActivity.class);
            intent.putExtra("url", "http://android.myapp.com/");
            startActivity(intent);
        } else if (id == R.id.btn_callJs) {
            Intent intent = new Intent(this, TestBaseCommonActivity.class);
            intent.putExtra("url", "file:///android_asset/js_interaction/hello.html");
            intent.putExtra("showExtra", true);
            startActivity(intent);
        } else if (id == R.id.btn_customProgress) {
            Intent intent = new Intent(this, TestBaseCommonActivity.class);
            intent.putExtra("url", "https://www.jd.com/");
            intent.putExtra("isCustomIndicator", true);
            startActivity(intent);
        } else if (id == R.id.btn_callAndroidNativeMethod) {
            Intent intent = new Intent(this, TestBaseCommonActivity.class);
            intent.putExtra("url", "file:///android_asset/jsbridge/demo.html");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_common) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "https://www.jd.com/");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_h5download) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "http://android.myapp.com/");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_h5inputUpload) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "file:///android_asset/upload_file/uploadfile.html");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_callJs) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "file:///android_asset/js_interaction/hello.html");
            intent.putExtra("showExtra", true);
            startActivity(intent);
        } else if (id == R.id.btn_fragment_play_video_fullscreen) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "https://m.youku.com/alipay_video/id_XNTExMjg3Njg1Mg==.html?spm=a2hww.12630578.drawer1.dzj1_1");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_customProgress) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("isCustomIndicator", true);
            intent.putExtra("url", "https://www.jd.com");
            startActivity(intent);
        } else if (id == R.id.btn_fragment_callAndroidNativeMethod) {
            Intent intent = new Intent(this, TestBaseCommonFragmentActivity.class);
            intent.putExtra("url", "file:///android_asset/jsbridge/demo.html");
            startActivity(intent);
        }

    }
}