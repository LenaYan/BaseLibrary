package com.ray.mvvm.lib.widget.lifecycle;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.OutsideLifecycleException;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena
 * Date: 05/01/2017
 * Time: 11:08 PM
 * \ ----------------------------------------
 * \| A small leak will sink a great ship.!  |
 * \ ----------------------------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
public class RxPageLifecycle {

    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bind(@NonNull final Observable<LifecycleEvent> lifecycle) {
        return RxLifecycle.bind(lifecycle, PAGE_LIFECYCLE);
    }

    private static final Func1<LifecycleEvent, LifecycleEvent> PAGE_LIFECYCLE = (lastEvent) -> {
        switch (lastEvent) {
            case CREATE:
                return LifecycleEvent.DESTROY;
            case START:
                return LifecycleEvent.STOP;
            case RESUME:
                return LifecycleEvent.PAUSE;
            case PAUSE:
                return LifecycleEvent.STOP;
            case STOP:
                return LifecycleEvent.DESTROY;
            case DESTROY:
                throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
            default:
                throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
        }
    };
}
