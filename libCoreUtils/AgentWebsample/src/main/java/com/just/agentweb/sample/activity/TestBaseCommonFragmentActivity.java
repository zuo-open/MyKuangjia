package com.just.agentweb.sample.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.just.agentweb.sample.R;

public class TestBaseCommonFragmentActivity extends AppCompatActivity {

    private String url;
    private boolean showExtra = false;//是否先是JS调用方法
    private boolean isCustomIndicator = false;//是否自定义进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_base_common_fragment);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            showExtra = intent.getBooleanExtra("showExtra", false);
            isCustomIndicator = intent.getBooleanExtra("isCustomIndicator", false);
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putBoolean("showExtra", showExtra);
            bundle.putBoolean("isCustomIndicator", isCustomIndicator);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, TestBaseCommonFragment.getInstance(bundle));
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}