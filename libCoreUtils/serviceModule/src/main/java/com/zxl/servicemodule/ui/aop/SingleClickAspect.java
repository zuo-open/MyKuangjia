package com.zxl.servicemodule.ui.aop;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SingleClickAspect {

    private static final String TAG = "SingleClickAspect";

    @Pointcut("execution(@com.zxl.servicemodule.ui.aop.SingleClick * *(..))")
    public void method() {

    }

    @Around("method() && @annotation(singleClick)")
    public void fastClick(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        View view = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof View) {
                //只找第一个参数类型为View的
                view = (View) arg;
            }
            break;
        }

        if (view != null) {
            if (!SingleClickUtils.canClick(singleClick.value())) {
                Log.e(TAG, "发生快速点击");
                return;
            }

        }
        joinPoint.proceed();

    }
}
