package com.just.agentweb.sample.widget;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.just.agentweb.IWebLayout;
import com.just.agentweb.sample.R;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * Created by cenxiaozhong on 2017/7/1.
 * source code  https://github.com/Justson/AgentWeb
 */

public class WebLayout implements IWebLayout {

    private Activity mActivity;
    private final TwinklingRefreshLayout mTwinklingRefreshLayout;
    private WebView mWebView = null;

    public WebLayout(Activity activity) {
        this.mActivity = activity;
        mTwinklingRefreshLayout = (TwinklingRefreshLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_twk_web, null);
        mTwinklingRefreshLayout.setPureScrollModeOn();//是否开启纯净的越界回弹模式,开启时刷新和加载更多控件不显示
        mWebView = (WebView) mTwinklingRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mTwinklingRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }


}
