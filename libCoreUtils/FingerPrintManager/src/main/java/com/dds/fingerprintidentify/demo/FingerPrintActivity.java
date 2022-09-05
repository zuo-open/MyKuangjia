package com.dds.fingerprintidentify.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.dds.fingerprintidentify.FingerprintIdentify;
import com.dds.fingerprintidentify.base.BaseFingerprint;
import com.dds.fingerprintmanager.R;

public class FingerPrintActivity extends AppCompatActivity {

    private AppCompatButton btnInit;
    private AppCompatButton btnFingerVerify;
    private TextView mVerifyResult;
    private FingerprintIdentify mFingerprintIdentify;
    private SweetAlertDialog mAlertDialog;
    private static final String TAG = "FingerPrintActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        btnInit = findViewById(R.id.btn_init);
        mVerifyResult = findViewById(R.id.tv_finger_result);
        btnFingerVerify = findViewById(R.id.btn_fingerVerify);
        initListener();
    }

    private void initListener() {
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFingerPrint();
            }
        });

        btnFingerVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "mStartFingerPrint onClick");
                mVerifyResult.setText("等待指纹验证结果");
                mAlertDialog = new SweetAlertDialog(FingerPrintActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("指纹验证!")
                        .setContentText("请将手指放于指纹传感器上.")
                        .setCustomImage(R.drawable.icon_finger)
                        .setCancelText("取消");
                mAlertDialog.show();
                //开始指纹识别
                mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
                    @Override
                    public void onSucceed() {
                        Log.i(TAG, "mFingerprintIdentify onSucceed");
                        mAlertDialog.setTitleText("验证通过")
                                .setContentText("匹配成功")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAlertDialog.isShowing()) {
                                    mAlertDialog.dismiss();
                                }
                            }
                        }, 1500);
                        mVerifyResult.setText("指纹验证通过");
                        Toast.makeText(FingerPrintActivity.this, "onSucceed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNotMatch(int availableTimes) {
                        Log.i(TAG, "mFingerprintIdentify onNotMatch, availableTimes: " + availableTimes);
                        mVerifyResult.setText("指纹验证不通过");
                        mAlertDialog.setTitleText("验证失败")
                                .setContentText("请重试！")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        Toast.makeText(FingerPrintActivity.this, "onNotMatch", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {
                        Log.i(TAG, "mFingerprintIdentify onFailed");
                        mAlertDialog.setTitleText("警告")
                                .setContentText("指纹验证错误次数超过上限！")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mAlertDialog.isShowing()) {
                                    mAlertDialog.dismiss();
                                }
                            }
                        }, 1500);
                        mVerifyResult.setText("指纹验证错误次数超过上限");
                        Toast.makeText(FingerPrintActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
                        mFingerprintIdentify.cancelIdentify();
                    }
                });
            }
        });
    }

    /**
     * 初始化指纹识别
     */
    private void initFingerPrint() {
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.i(TAG, "initFingerPrint", exception);
                mVerifyResult.setText("指纹验证初始化异常：" + exception.getMessage());
            }
        });
        if (!mFingerprintIdentify.isHardwareEnable()) {
            mVerifyResult.setText("指纹验证初始化失败：设备硬件不支持指纹识别");
            return;
        }

        if (!mFingerprintIdentify.isRegisteredFingerprint()) {
            mVerifyResult.setText("指纹验证初始化失败：未注册指纹");
            return;
        }

        if (!mFingerprintIdentify.isFingerprintEnable()) {
            mVerifyResult.setText("指纹验证初始化失败：指纹识别不可用");
            return;
        }
        mVerifyResult.setText("指纹验证初始化正常");
    }
}