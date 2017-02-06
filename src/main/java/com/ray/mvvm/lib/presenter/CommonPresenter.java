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

import android.support.annotation.NonNull;

import com.ray.mvvm.lib.interfaces.OnStartAction;
import com.ray.mvvm.lib.model.http.ErrorType;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CommonPresenter implements IPresenter {

    private Observable<LifecycleEvent> lifecycleEventObs;
    private LifecycleEvent lifecycleEvent;

    public CommonPresenter() {
    }

    private <N> Observable<N> respCheck(N dataEntity) {
        return Observable.create((subscriber) -> {
                    if (dataEntity == null) {
                        subscriber.onError(new ErrorEvent(ErrorType.RESP_BODY_EMPTY, "Response Data is empty."));
                    } else {
                        subscriber.onNext(dataEntity);
                    }
                    subscriber.onCompleted();
                }
        );
    }

    @Override
    public void setLifecycleObs(@NonNull Observable<LifecycleEvent> obs, @NonNull LifecycleEvent lifecycleEvent) {
        this.lifecycleEventObs = obs;
        this.lifecycleEvent = lifecycleEvent;
    }

    protected <T> Observable<T> mockResp(T t) {
        return Observable
                .create((Subscriber<? super T> subscriber) -> {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Observable<T> mockResp(Func0<T> func) {
        return Observable
                .create((Subscriber<? super T> subscriber) -> {
                    subscriber.onNext(func.call());
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected Observable mockError() {
        return Observable
                .error(new IllegalArgumentException("Error response"))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> LifecycleTransformer<T> bindLifecycle() {
        return RxLifecycle.bindUntilEvent(lifecycleEventObs, lifecycleEvent);
    }

    protected <T> Observable.Transformer<T, T> bindLifecycleOnMainThread() {
        return (observable) ->
                observable.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle());
    }

    protected <T> Observable.Transformer<T, T> applyAsync() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <T, R> Observable.Transformer<T, R> applyAsync(Func1<? super T, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <T> Observable.Transformer<T, T> applyAsync(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle())
                .doOnSubscribe(startListener::onStart);
    }

    protected <T> Observable.Transformer<T, T> applyAsync(Func1<? super T, ? extends Observable<? extends T>> func, OnStartAction startListener) {
        return applyAsyncWithMap(func, startListener);
    }

    protected <T, R> Observable.Transformer<T, R> applyAsyncWithMap(Func1<? super T, ? extends Observable<? extends R>> func, OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(startListener::onStart)
                .compose(bindLifecycle());
    }
}
