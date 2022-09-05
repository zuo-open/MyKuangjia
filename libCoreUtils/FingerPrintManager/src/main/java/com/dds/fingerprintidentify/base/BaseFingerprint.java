package com.dds.fingerprintidentify.base;

import android.app.Activity;

public abstract class BaseFingerprint {

    protected Activity mActivity;
    private FingerprintIdentifyListener mIdentifyListener;
    private FingerprintIdentifyExceptionListener mExceptionListener;

    private int mNumberOfFailures = 0;                      // number of failures   失败次数
    private int mMaxAvailableTimes = 3;                     // the most available times   最大尝试树
    private boolean mIsHardwareEnable = false;              // if the phone equipped fingerprint hardware   设备是否支持指纹识别
    private boolean mIsRegisteredFingerprint = false;       // if the phone has any fingerprints    当前是否有注册的指纹
    private boolean mIsCanceledIdentify = false;            // if canceled identify
    private boolean mIsCalledStartIdentify = false;         // if started identify

    //构建初始化
    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener exceptionListener) {
        mActivity = activity;
        mExceptionListener = exceptionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCalledStartIdentify = true;
        mIdentifyListener = listener;
        mIsCanceledIdentify = false;
        mNumberOfFailures = 0;

        doIdentify();
    }

    /**
     * Continue to call fingerprint identify, keep the number of failures.
     */
    public void resumeIdentify() {
        if (mIsCalledStartIdentify && mIdentifyListener != null && mNumberOfFailures < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    /**
     * stop identify and release hardware
     */
    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    /**
     * is that need to recall doIdentify again when not match
     *
     * @return needed
     */
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    /**
     * catch the all exceptions
     *
     * @param exception exception
     */
    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    /**
     * do identify actually
     */
    protected abstract void doIdentify();

    /**
     * do cancel identify actually
     */
    protected abstract void doCancelIdentify();

    /**
     * verification passed
     */
    protected void onSucceed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onSucceed();
                }
            });
        }

        cancelIdentify();
    }

    /**
     * fingerprint not match
     */
    protected void onNotMatch() {
        if (mIsCanceledIdentify) {
            return;
        }

        if (++mNumberOfFailures < mMaxAvailableTimes) {
            if (mIdentifyListener != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIdentifyListener.onNotMatch(mMaxAvailableTimes - mNumberOfFailures);
                    }
                });
            }

            if (needToCallDoIdentifyAgainAfterNotMatch()) {
                doIdentify();
            }

            return;
        }

        onFailed();
    }

    /**
     * verification failed
     */
    protected void onFailed() {
        if (mIsCanceledIdentify) {
            return;
        }

        mNumberOfFailures = mMaxAvailableTimes;

        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onFailed();
                }
            });
        }

        cancelIdentify();
    }

    /**
     * is that hardware detected and has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFingerprint;
    }

    /**
     * is that hardware detected
     *
     * @return yes
     */
    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    /**
     * save the value of hardware detected
     *
     * @param hardwareEnable detected
     */
    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    /**
     * is that has enrolled fingerprints
     *
     * @return yes
     */
    public boolean isRegisteredFingerprint() {
        return mIsRegisteredFingerprint;
    }

    /**
     * save the value of enrolled fingerprints
     *
     * @param registeredFingerprint enrolled
     */
    protected void setRegisteredFingerprint(boolean registeredFingerprint) {
        mIsRegisteredFingerprint = registeredFingerprint;
    }

    /**
     * identify callback
     */
    public interface FingerprintIdentifyListener {
        void onSucceed();

        void onNotMatch(int availableTimes);

        void onFailed();
    }

    /**
     * exception callback  错误回调
     */
    public interface FingerprintIdentifyExceptionListener {
        void onCatchException(Throwable exception);
    }
}