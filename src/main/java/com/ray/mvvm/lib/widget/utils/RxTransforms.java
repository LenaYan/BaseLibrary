package com.ray.mvvm.lib.widget.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena
 * Date: 25/01/2017
 * Time: 2:11 PM
 * \ --------------------------------------------
 * \| The only thing that is constant is change!  |
 * \ --------------------------------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
public final class RxTransforms {

    public static <T> Observable.Transformer<T, T> viewThrottleTransform() {
        return observable ->
                observable.throttleFirst(1, TimeUnit.SECONDS)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .onBackpressureLatest()
                        .observeOn(AndroidSchedulers.mainThread());
    }

}
