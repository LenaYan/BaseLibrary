/*
 *  Copyright (C) 2015 Rayman Yan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ray.mvvm.lib.presenter;

import android.annotation.TargetApi;
import android.os.Build;

import com.ray.mvvm.lib.model.http.ErrorType;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.eventbus.RxBus;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;

import java.io.IOException;

import rx.Observable;
import timber.log.Timber;

public interface IPresenter {

    void setLifecycleObs(Observable<LifecycleEvent> obs, LifecycleEvent lifecycleEvent);

    @TargetApi(Build.VERSION_CODES.N)
    default void postError(Throwable throwable) {
        throwable.printStackTrace();
        ErrorEvent errorEvent;
        if (throwable instanceof ErrorEvent) {
            errorEvent = (ErrorEvent) throwable;
        } else if (throwable instanceof IOException) {
            errorEvent = new ErrorEvent(ErrorType.NETWORK, "网络异常");
        } else {
            errorEvent = new ErrorEvent(ErrorType.OTHER, throwable.getMessage());
        }
        RxBus.instance().post(errorEvent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void onUnsubscribe() {
        Timber.i("onUnsubscribe---------------------- %d", Thread.currentThread().getId());
    }
}
