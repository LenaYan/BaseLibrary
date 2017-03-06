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

import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class CommonPresenter implements IPresenter {

    private Observable<LifecycleEvent> lifecycleEventObs;
    private LifecycleEvent lifecycleEvent;

    public CommonPresenter() {
    }

    private <N> Single<N> respCheck(N dataEntity) {
        return Single.create((emitter) -> {
                    if (dataEntity == null) {
                        emitter.onError(new ErrorEvent(ErrorEvent.RESP_BODY_EMPTY, "Response Data is empty."));
                    } else {
                        emitter.onSuccess(dataEntity);
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
                .<T>create(emitter ->
                        emitter.onSuccess(t))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Single<T> mockResp(Callable<T> func) {
        return Single
                .<T>create((emitter) ->
                        emitter.onSuccess(func.call()))
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

    protected <T> ObservableTransformer<T, T> bindObsToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle());
    }

    protected <T> SingleTransformer<T, T> bindSingleToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle());
    }

    protected CompletableTransformer bindCompletableToLifecycleOnMain() {
        return single ->
                single.observeOn(AndroidSchedulers.mainThread())
                        .compose(bindLifecycle());
    }

    protected <T> SingleTransformer<T, T> applyAsyncSingle() {
        return applyAsyncSingle(Schedulers.io());
    }

    protected <T> SingleTransformer<T, T> applyAsyncSingle(Scheduler scheduler) {
        return single -> single
                .subscribeOn(scheduler)
                .doOnDispose(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected CompletableTransformer applyAsyncCompletable() {
        return applyAsyncCompletable(Schedulers.io());
    }

    protected <T> CompletableTransformer applyAsyncCompletable(Scheduler scheduler) {
        return single -> single
                .subscribeOn(scheduler)
                .doOnDispose(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T> ObservableTransformer<T, T> applyAsyncObs() {
        return applyAsyncObs(Schedulers.io());
    }

    protected <T> ObservableTransformer<T, T> applyAsyncObs(Scheduler scheduler) {
        return observable -> observable
                .subscribeOn(scheduler)
                .doOnDispose(this::onUnsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T> SingleTransformer<T, T> applyAsyncRequest() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T, R> SingleTransformer<T, R> applyAsyncRequest(Function<? super T, ? extends Single<? extends R>> func) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .flatMap(func);
    }

    protected <T> SingleTransformer<T, T> applyAsyncRequest(Consumer<Disposable> doOnSubscribe) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .doOnSubscribe(doOnSubscribe);
    }

    protected <T> SingleTransformer<T, T> applyAsyncRequest(Function<? super T, ? extends Single<? extends T>> func, Consumer<Disposable> doOnSubscribe) {
        return applyAsyncReqWithMap(func, doOnSubscribe);
    }

    protected <T, R> SingleTransformer<T, R> applyAsyncReqWithMap(Function<? super T, ? extends Single<? extends R>> func, Consumer<Disposable> doOnSubscribe) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .doOnSubscribe(doOnSubscribe)
                .compose(bindLifecycle())
                .flatMap(func);
    }
}
