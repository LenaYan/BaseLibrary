package com.ray.mvvm.lib.app;

import android.os.Looper;

public final class ConstantsClass {

    public static void argumentCheck(boolean wrongArg) {
        if (wrongArg)
            throw new IllegalArgumentException("默认参数异常，请检查传递数据");
    }

    protected static boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
