package com.just.agentweb.sample.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.google.gson.Gson;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebUIControllerImplBase;
import com.just.agentweb.AgentWebUtils;
import com.just.agentweb.BaseIndicatorView;
import com.just.agentweb.DefaultDownloadImpl;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.IWebLayout;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.WebViewClient;
import com.just.agentweb.filechooser.FileCompressor;
import com.just.agentweb.sample.R;
import com.just.agentweb.sample.app.App;
import com.just.agentweb.sample.client.MiddlewareChromeClient;
import com.just.agentweb.sample.client.MiddlewareWebViewClient;
import com.just.agentweb.sample.common.CommonWebChromeClient;
import com.just.agentweb.sample.common.UIController;
import com.just.agentweb.sample.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import top.zibin.luban.Luban;

/**
 * Created by cenxiaozhong on 2017/7/22.
 * <p>
 * source code  https://github.com/Justson/AgentWeb
 * 不能支持h5 input上传,h5视频全屏播放
 */

public abstract class BaseCommonAgentWebFragment extends Fragment implements View.OnClickListener, FileCompressor.FileCompressEngine {

    protected AgentWeb mAgentWeb;
    private AgentWebUIControllerImplBase mAgentWebUIController;
    protected ErrorLayoutEntity mErrorLayoutEntity;
    private MiddlewareWebChromeBase mMiddleWareWebChrome;
    private MiddlewareWebClientBase mMiddleWareWebClient;
    private TextView tvTitle;//标题
    private ImageView ivBack;//返回
    private ImageView ivMore;//更多
    private static final String TAG = "BaseCommonAgentWebActiv";
    public static final String URL_KEY = "url_key";
    private AgentWeb.CommonBuilder builder;
    protected BridgeWebView mBridgeWebView;
    private PopupMenu mPopupMenu;

    /**
     * 用于方便打印测试
     */
    private Gson mGson = new Gson();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflateView(inflater, container);
    }


    abstract protected View inflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        buildAgentWeb(view);
    }

    protected void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_web_title);
        ivBack = view.findViewById(R.id.iv_web_back);
        ivMore = view.findViewById(R.id.iv_web_more);
        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);

        FileCompressor.getInstance().registerFileCompressEngine(this);
    }


    protected void buildAgentWeb(View view) {
        mBridgeWebView = new BridgeWebView(getActivity());
        ErrorLayoutEntity mErrorLayoutEntity = getErrorLayoutEntity();
        AgentWeb.IndicatorBuilder indicatorBuilder = AgentWeb.with(this)
                .setAgentWebParent(getAgentWebParent(view), -1, new ViewGroup.LayoutParams(-1, -1));
        BaseIndicatorView baseIndicatorView = needCustomProgress();
        if (baseIndicatorView != null) {
            builder = indicatorBuilder.setCustomIndicator(baseIndicatorView);
        } else {
            builder = indicatorBuilder.useDefaultIndicator(getIndicatorColor(), getIndicatorHeight());
        }
        mAgentWeb = builder.setAgentWebWebSettings(getAgentWebSettings())//设置 IAgentWebSettings。
                .setWebViewClient(getWebViewClient())////WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(new CommonWebChromeClient())
                .setWebView(mBridgeWebView)
                .setPermissionInterceptor(getPermissionInterceptor())
                .setWebLayout(getWebLayout())
                .setAgentWebUIController(getAgentWebUIController())
                .setMainFrameErrorView(mErrorLayoutEntity.layoutRes, mErrorLayoutEntity.reloadId)
                .useMiddlewareWebChrome(getMiddleWareWebChrome())
                .useMiddlewareWebClient(getMiddleWareWebClient())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownUrl()
                .setOpenOtherPageWays(getOpenOtherAppWay())
                .createAgentWeb()
                .ready()
                .go(getUrl());
        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供 ， 请从WebView方面入手设置。
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        //WebSettings ws = mAgentWeb.getWebCreator().getWebView().getSettings();
//        ws.setUseWideViewPort(true);//图片缩放
//        ws.setLoadWithOverviewMode(true);
        //添加JS接口和桥接方法
        addJSInterface();
    }

    //添加JS接口和桥接方法
    protected abstract void addJSInterface();


    protected @NonNull
    ErrorLayoutEntity getErrorLayoutEntity() {
        if (this.mErrorLayoutEntity == null) {
            this.mErrorLayoutEntity = new ErrorLayoutEntity();
        }
        return mErrorLayoutEntity;
    }

    protected AgentWeb getAgentWeb() {
        return this.mAgentWeb;
    }


    protected static class ErrorLayoutEntity {
        private int layoutRes = com.just.agentweb.R.layout.agentweb_error_page;
        private int reloadId;

        public void setLayoutRes(int layoutRes) {
            this.layoutRes = layoutRes;
            if (layoutRes <= 0) {
                layoutRes = -1;
            }
        }

        public void setReloadId(int reloadId) {
            this.reloadId = reloadId;
            if (reloadId <= 0) {
                reloadId = -1;
            }
        }
    }


    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();

    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        FileCompressor.getInstance().unregisterFileCompressEngine(this);
        super.onDestroy();
    }


    protected
    @Nullable
    String getUrl() {
        String target = "";

        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "http://cw.gzyunjuchuang.com/";
        }

//		return "http://ggzy.sqzwfw.gov.cn/WebBuilderDS/WebbuilderMIS/attach/downloadZtbAttach.jspx?attachGuid=af982055-3d76-4b00-b5ab-36dee1f90b11&appUrlFlag=sqztb&siteGuid=7eb5f7f1-9041-43ad-8e13-8fcb82ea831a";
        return target;

    }

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getAgentWebSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.download.library:Downloader:4.1.1' ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        new DefaultDownloadImpl(getActivity(),
                                webView,
                                this.mAgentWeb.getPermissionInterceptor()) {

                            @Override
                            protected ResourceRequest createResourceRequest(String url) {
                                return DownloadImpl.getInstance(getActivity())
                                        .url(url)
                                        .quickProgress()
                                        .addHeader("", "")
                                        .setEnableIndicator(true)
                                        .autoOpenIgnoreMD5()
                                        .setRetry(5)
                                        .setBlockMaxTime(100000L);
                            }

                            @Override
                            protected void taskEnqueue(ResourceRequest resourceRequest) {
                                resourceRequest.enqueue(new DownloadListenerAdapter() {
                                    @Override
                                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                                    }

                                    @MainThread
                                    @Override
                                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                                        super.onProgress(url, downloaded, length, usedTime);
                                    }

                                    @Override
                                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                                        return super.onResult(throwable, path, url, extra);
                                    }
                                });
                            }
                        });
            }
        };
    }

    protected abstract @NonNull
    ViewGroup getAgentWebParent(View view);

    protected @Nullable
    WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (tvTitle != null) {
                    tvTitle.setText(title);
                }

            }
        };
    }

    protected @ColorInt
    int getIndicatorColor() {
        return -1;
    }

    protected int getIndicatorHeight() {
        return -1;
    }

    protected @Nullable
    WebViewClient getWebViewClient() {

        return new WebViewClient() {
            BridgeWebViewClient mBridgeWebViewClient = new BridgeWebViewClient(mBridgeWebView);
            private HashMap<String, Long> timer = new HashMap<>();

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mBridgeWebViewClient.shouldOverrideUrlLoading(view, request.getUrl().toString())) {
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            //
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {

                Log.i(TAG, "view:" + new Gson().toJson(view.getHitTestResult()));
                Log.i(TAG, "mWebViewClient shouldOverrideUrlLoading:" + url);
                if (mBridgeWebViewClient.shouldOverrideUrlLoading(view, url)) {
                    return true;
                }

                //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
                if (url.startsWith("intent://") && url.contains("com.youku.phone")) {
                    return true;
                }
			/*else if (isAlipay(view, mUrl))   //1.2.5开始不用调用该方法了 ，只要引入支付宝sdk即可 ， DefaultWebClient 默认会处理相应url调起支付宝
			    return true;*/
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "mUrl:" + url + " onPageStarted  target:" + getUrl());
                timer.put(url, System.currentTimeMillis());
//                if (url.equals(getUrl())) {
//                    pageNavigator(View.GONE);
//                } else {
//                    pageNavigator(View.VISIBLE);
//                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (timer.get(url) != null) {
                    long overTime = System.currentTimeMillis();
                    Long startTime = timer.get(url);
                    Log.i(TAG, "  page mUrl:" + url + "  used time:" + (overTime - startTime));
                }
                mBridgeWebViewClient.onPageFinished(view, url);
            }
            /*错误页回调该方法 ， 如果重写了该方法， 上面传入了布局将不会显示 ， 交由开发者实现，注意参数对齐。*/
	   /* public void onMainFrameError(AbsAgentWebUIController agentWebUIController, WebView view, int errorCode, String description, String failingUrl) {

            Log.i(TAG, "AgentWebFragment onMainFrameError");
            agentWebUIController.onMainFrameError(view,errorCode,description,failingUrl);

        }*/

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

//			Log.i(TAG, "onReceivedHttpError:" + 3 + "  request:" + mGson.toJson(request) + "  errorResponse:" + mGson.toJson(errorResponse));
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

//			Log.i(TAG, "onReceivedError:" + errorCode + "  description:" + description + "  errorResponse:" + failingUrl);
            }
        };

    }


    protected @Nullable
    WebView getWebView() {
        return null;
    }

    protected @Nullable
    IWebLayout getWebLayout() {
        return null;
    }

    protected @Nullable
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor() {

            /**
             * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
             * @param url
             * @param permissions
             * @param action
             * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
             */
            @Override
            public boolean intercept(String url, String[] permissions, String action) {
                Log.i(TAG, "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
                return false;
            }
        };
    }

    public @Nullable
    AgentWebUIControllerImplBase getAgentWebUIController() {
        return new UIController(getActivity());
    }

    public @Nullable
    DefaultWebClient.OpenOtherPageWays getOpenOtherAppWay() {
        return null;
    }

    protected @NonNull
    MiddlewareWebChromeBase getMiddleWareWebChrome() {
        return this.mMiddleWareWebChrome = new MiddlewareChromeClient() {
        };
    }

    protected void setTitle(WebView view, String title) {

    }

    protected @NonNull
    MiddlewareWebClientBase getMiddleWareWebClient() {
        return this.mMiddleWareWebClient = new MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading url:" + url);
				/*if (url.startsWith("agentweb")) { // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
					Log.i(TAG, "agentweb scheme ~");
					return true;
				}*/

                if (super.shouldOverrideUrlLoading(view, url)) { // 执行 DefaultWebClient#shouldOverrideUrlLoading
                    return true;
                }
                // do you work
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.e(TAG, "MiddlewareWebClientBase#shouldOverrideUrlLoading request url:" + request.getUrl().toString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (request.isForMainFrame() && error.getErrorCode() != -1) {
                        super.onReceivedError(view, request, error);
                    }
                }
            }
        };
    }

    /**
     * 选择文件后回调该方法， 这里可以做文件压缩 / 也可以做图片的方向调整
     *
     * @param type     customize/system  ， customize 表示通过js方式获取文件， 把文件
     *                 转成base64的方式返回给js，这种方式兼容性高，但是存在文件过大转成base64时
     *                 字符串长度过长，导致与js通信失败问题，所以很有必要压缩文件， 尽量控制字符串长度在512kb以内。
     *                 <p>
     *                 system 这种方式，是由input/file 标签触发的文件选择，这种方式缺点是在Android 4.4 不回调
     *                 fileChooser，存在兼容性问题，但是经过升级，基本可以忽略了，api 的兼容性越来越好了， 回调
     *                 返回是于uri形式，所以不存在文件大小问题，作图片预览也很快。(推荐这种方式)
     * @param uri      文件的uri
     * @param callback
     */
    @Override
    public void compressFile(String type, final Uri[] uri, ValueCallback<Uri[]> callback) {
        Log.e(TAG, "compressFile type:" + type);
        if ("system".equals(type)) { // input/file 标签触发的文件选择，这种方式不存在性能问题，可压缩也可以不压缩，具体看自己业务要求
            callback.onReceiveValue(uri);
            return;
        }
        // customize.equals(type)  这种方式强烈建议文件压缩
        if (uri == null || uri.length == 0) {
            callback.onReceiveValue(uri);
        } else {
            final String[] paths = AgentWebUtils.uriToPath(getActivity(), uri);
            if (paths == null || paths.length == 0) {
                callback.onReceiveValue(uri);
                return;
            }

            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
                try {
                    Uri[] result = new Uri[paths.length];
                    for (int i = 0; i < paths.length; i++) {
                        String filePath = paths[i];
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getExtensionByFilePath(filePath));
                        if (TextUtils.isEmpty(mimeType) || !mimeType.startsWith("image")) {
                            result[i] = uri[i];
                        } else {
                            File origin = new File(filePath);
                            File file = Luban.with(App.mContext).ignoreBy(100).setTargetDir(AgentWebUtils.getAgentWebFilePath(App.mContext)).get(filePath);
                            Log.e(TAG, "原文件大小：" + byte2FitMemorySize(origin.length()));
                            Log.e(TAG, "压缩后文件大小：" + byte2FitMemorySize(file.length()));

                            Uri fileUri = AgentWebUtils.getUriFromFile(App.mContext, file);
                            result[i] = fileUri;

                        }
                    }
                    AgentWebUtils.runInUiThread(() -> callback.onReceiveValue(result));
                } catch (IOException e) {
                    e.printStackTrace();
                    AgentWebUtils.runInUiThread(() -> callback.onReceiveValue(uri));
                }
            });

        }

    }

    private static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.1fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.1fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.1fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.1fGB", (double) byteNum / 1073741824);
        }
    }

//    @Override
//    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
//        return mAgentWeb.handleKeyEvent(keyCode, event);
//
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_web_back) {
            getActivity().onBackPressed();
        } else if (id == R.id.iv_web_more) {
            showPoPup(view);
        }
    }

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(getActivity(), view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }


    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int itemId = item.getItemId();
            if (itemId == R.id.refresh) {
                if (mAgentWeb != null) {
                    mAgentWeb.getUrlLoader().reload(); // 刷新
                }
                return true;
            } else if (itemId == R.id.copy) {
                if (mAgentWeb != null) {
                    toCopy(getActivity(), mAgentWeb.getWebCreator().getWebView().getUrl());
                }
                return true;
            } else if (itemId == R.id.default_browser) {
                if (mAgentWeb != null) {
                    openBrowser(mAgentWeb.getWebCreator().getWebView().getUrl());
                }
                return true;
            } else if (itemId == R.id.default_clean) {
                toCleanWebCache();
                return true;
            } else if (itemId == R.id.error_website) {
                loadErrorWebSite();
                // test DownloadingService
//			        LogUtils.i(TAG, " :" + mDownloadingService + "  " + (mDownloadingService == null ? "" : mDownloadingService.isShutdown()) + "  :" + mExtraService);
//                    if (mDownloadingService != null && !mDownloadingService.isShutdown()) {
//                        mExtraService = mDownloadingService.shutdownNow();
//                        LogUtils.i(TAG, "mExtraService::" + mExtraService);
//                        return true;
//                    }
//                    if (mExtraService != null) {
//                        mExtraService.performReDownload();
//                    }

                return true;
            }
            return false;

        }
    };

    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private void toCopy(Context context, String text) {

        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));

    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            Toast.makeText(getActivity(), targetUrl + " 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        startActivity(intent);
    }

    /**
     * 测试错误页的显示
     */
    private void loadErrorWebSite() {
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl("http://www.unkownwebsiteblog.me");
        }
    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {

        if (this.mAgentWeb != null) {

            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(getActivity(), "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
//            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }

    //是否自定义进度条
    abstract public BaseIndicatorView needCustomProgress();


}
