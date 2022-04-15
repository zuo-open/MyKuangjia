package com.zxl.servicemodule.ui.aop;

import android.util.Log;

public class SingleClickUtils {
    private static final String TAG = "SingleClickUtils";
    private static Long lastClickTime;

    //首付可以点击
    public static boolean canClick(long clickTime) {
        boolean flag = true;
        long currentTime = System.currentTimeMillis();
        if (lastClickTime != null) {
            if (currentTime - lastClickTime < clickTime) {
                Log.e(TAG, "发生了快速点击");
                flag = false;
            }
        }
        lastClickTime = currentTime;
        return flag;
    }
}
