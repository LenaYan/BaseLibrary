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
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CommonPresenter implements IPresenter {

    private Observable<LifecycleEvent> lifecycleEventObs;
    private LifecycleEvent lifecycleEvent;

    public CommonPresenter() {
    }

    private <N> Single<N> respCheck(N dataEntity) {
        return Single.create((subscriber) -> {
                    if (dataEntity == null) {
                        subscriber.onError(new ErrorEvent(ErrorEvent.RESP_BODY_EMPTY, "Response Data is empty."));
                    } else {
                        subscriber.onSuccess(dataEntity);
                    }
                }
        );
    }

    @Override
    public void setLifecycleObs(@NonNull Observable<LifecycleEvent> obs, @NonNull LifecycleEvent lifecycleEvent) {
        this.lifecycleEventObs = obs;
        this.lifecycleEvent = lifecycleEvent;
    }

    protected <T> Single<T> mockResp(T t) {
        return Single
                .<T>create(singleSubscriber ->
                        singleSubscriber.onSuccess(t))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Single<T> mockResp(Func0<T> func) {
        return Single
                .<T>create((subscriber) ->
                        subscriber.onSuccess(func.call()))
                .delay(3, TimeUnit.SECONDS);
    }

    protected Single mockError() {
        return Single
                .error(new IllegalArgumentException("Error response"))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> LifecycleTransformer<T> bindLifecycle() {
        return RxLifecycle.bindUntilEvent(lifecycleEventObs, lifecycleEvent);
    }

    protected <T> Observable.Transformer<T, T> bindObsToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle());
    }

    protected <T> Single.Transformer<T, T> bindSingleToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle().forSingle());
    }

    protected Completable.Transformer bindCompletableToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle().forCompletable());
    }

    protected <T> Single.Transformer<T, T> applyAsyncSingle() {
        return applyAsyncSingle(Schedulers.io());
    }

    protected <T> Single.Transformer<T, T> applyAsyncSingle(Scheduler scheduler) {
        return single -> single
                .subscribeOn(scheduler)
                .doOnUnsubscribe(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle().forSingle());
    }

    protected Completable.Transformer applyAsyncCompletable() {
        return applyAsyncCompletable(Schedulers.io());
    }

    protected <T> Completable.Transformer applyAsyncCompletable(Scheduler scheduler) {
        return single -> single
                .subscribeOn(scheduler)
                .doOnUnsubscribe(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle().forCompletable());
    }

    protected <T> Observable.Transformer<T, T> applyAsyncObs() {
        return applyAsyncObs(Schedulers.io());
    }

    protected <T> Observable.Transformer<T, T> applyAsyncObs(Scheduler scheduler) {
        return observable -> observable
                .subscribeOn(scheduler)
                .doOnUnsubscribe(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T> Single.Transformer<T, T> applyAsyncRequest() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle().forSingle());
    }

    protected <T, R> Single.Transformer<T, R> applyAsyncRequest(Func1<? super T, ? extends Single<? extends R>> func) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .flatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle().forSingle());
    }

    protected <T> Single.Transformer<T, T> applyAsyncRequest(OnStartAction startListener) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle().forSingle())
                .doOnSubscribe(startListener::onStart);
    }

    protected <T> Single.Transformer<T, T> applyAsyncRequest(Func1<? super T, ? extends Single<? extends T>> func, OnStartAction startListener) {
        return applyAsyncReqWithMap(func, startListener);
    }

    protected <T, R> Single.Transformer<T, R> applyAsyncReqWithMap(Func1<? super T, ? extends Single<? extends R>> func, OnStartAction startListener) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .flatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .doOnSubscribe(startListener::onStart)
                .compose(bindLifecycle().forSingle());
    }
}
