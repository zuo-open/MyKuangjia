package com.zxl.mykuangjia.ui.main.home.splash;

import android.content.Context;

/**
 * 闪屏页数据保存数据
 */
public class SplashPathUtils {

    //本地欢迎数据保存路径
    public static String getLocalImagePath(Context context) {
        return context.getExternalFilesDirs("file") + "/splash/splash.json";
    }
}
