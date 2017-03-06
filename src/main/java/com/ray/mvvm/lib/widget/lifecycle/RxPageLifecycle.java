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

package com.ray.mvvm.lib.widget.lifecycle;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.OutsideLifecycleException;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
public final class RxPageLifecycle {

    private static final Function<LifecycleEvent, LifecycleEvent> PAGE_LIFECYCLE = (lastEvent) -> {
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

    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bind(@NonNull final Observable<LifecycleEvent> lifecycle) {
        return RxLifecycle.bind(lifecycle, PAGE_LIFECYCLE);
    }
}
