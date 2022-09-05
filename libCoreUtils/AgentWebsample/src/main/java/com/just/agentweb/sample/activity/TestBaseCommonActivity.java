package com.just.agentweb.sample.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.just.agentweb.BaseIndicatorView;
import com.just.agentweb.sample.R;
import com.just.agentweb.sample.base.BaseCommonAgentWebActivity;
import com.just.agentweb.sample.common.AndroidInterface;
import com.just.agentweb.sample.fragment.JsbridgeWebFragment;
import com.just.agentweb.sample.widget.CoolIndicatorLayout;

import org.json.JSONObject;

public class TestBaseCommonActivity extends BaseCommonAgentWebActivity {

    private String url;
    private View item;
    private boolean showExtra = false;//是否显示调用JS方法布局
    private boolean isCustomIndicator = false;//是否需要自定义进度条
    private Button btnCallJsNoParamsButton;//调用无参JS方法
    private Button btnCallJsOneParamsButton;//调用一个参数Js方法
    private Button btnCallJsMoreParamsButton;//调用多个参数JS方法
    private Button btnJsJavaCommunicationButton;//调用JS方法后H5调用Andoid方法


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_base_common);
    }

    @Override
    protected void initView() {
        super.initView();
        item = findViewById(R.id.item_js);
        if (getIntent() != null) {
            showExtra = getIntent().getBooleanExtra("showExtra", false);
        }

        if (showExtra) {
            item.setVisibility(View.VISIBLE);
            showView();
        }

    }

    private void showView() {
        btnCallJsNoParamsButton = item.findViewById(R.id.callJsNoParamsButton);
        btnCallJsOneParamsButton = item.findViewById(R.id.callJsOneParamsButton);
        btnCallJsMoreParamsButton = item.findViewById(R.id.callJsMoreParamsButton);
        btnJsJavaCommunicationButton = item.findViewById(R.id.jsJavaCommunicationButton);
        btnCallJsNoParamsButton.setOnClickListener(this);
        btnCallJsOneParamsButton.setOnClickListener(this);
        btnCallJsMoreParamsButton.setOnClickListener(this);
        btnJsJavaCommunicationButton.setOnClickListener(this);
    }

    @Override
    protected void addJSInterface() {
        //注入对象
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
        //JS桥接
        callJSBridge();

    }

    //JS桥接
    private void callJSBridge() {
        mBridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });

        JsbridgeWebFragment.User user = new JsbridgeWebFragment.User();
        JsbridgeWebFragment.Location location = new JsbridgeWebFragment.Location();
        location.address = "SDU";
        user.location = location;
        user.name = "Agentweb --> Jsbridge";
        mBridgeWebView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Toast.makeText(TestBaseCommonActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

        mBridgeWebView.send("hello");
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return findViewById(R.id.ll_container);
    }

    @Nullable
    @Override
    protected String getUrl() {
        url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            url = "http://www.baidu.com";
        }
        return url;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.callJsNoParamsButton) {
            mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroid");
        } else if (view.getId() == R.id.callJsOneParamsButton) {
            mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidParam", "Hello ! Agentweb");
        } else if (view.getId() == R.id.callJsMoreParamsButton) {
            mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("Info", "value:" + value);
                }
            }, getJson(), "say:", " Hello! Agentweb");
        } else if (view.getId() == R.id.jsJavaCommunicationButton) {
            mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidInteraction", "你好Js");
        }
    }

    @Override
    public BaseIndicatorView needCustomProgress() {
        if (getIntent() != null) {
            boolean isCustomIndicator = getIntent().getBooleanExtra("isCustomIndicator", false);
            if (isCustomIndicator) {
                return new CoolIndicatorLayout(this);
            }
        }
        return null;
    }

    private String getJson() {

        String result = "";
        try {
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("id", 1);
            mJSONObject.put("name", "Agentweb");
            mJSONObject.put("age", 18);
            result = mJSONObject.toString();
        } catch (Exception e) {

        }

        return result;
    }
}