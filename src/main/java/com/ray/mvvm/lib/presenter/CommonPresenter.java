/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Fri, 11 Nov 2016 22:14:52 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Fri, 11 Nov 2016 22:14:52 +0800.
 *  This file is originally created by Lena.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
import com.ray.mvvm.lib.model.http.ExObserver;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.eventbus.RxBus;
import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Android Studio.
 * ProjectName: WuLiu
 * Author:  Lena
 * Date: 27/10/2016
 * Time: 12:11 AM
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
public class CommonPresenter implements IPresenter {

    private CompositeSubscription subscription = new CompositeSubscription();
    private Observable<LifecycleEvent> lifecycleEventObs;
    private LifecycleEvent lifecycleEvent;

    public CommonPresenter() {
    }

    @Override
    public void setLifecycleObs(@NonNull Observable<LifecycleEvent> obs, @NonNull LifecycleEvent lifecycleEvent) {
        this.lifecycleEventObs = obs;
        this.lifecycleEvent = lifecycleEvent;
    }

    protected <T> LifecycleTransformer<T> lifecycleTransformer() {
        return RxLifecycle.bindUntilEvent(lifecycleEventObs, lifecycleEvent);
    }

    private <T> void subscribe(Observable<? extends T> observable, Subscriber<? super T> subscriber) {
        observable
                .compose(lifecycleTransformer())
                .subscribe(subscriber);
    }

    private void subscribe(Subscription subscription) {
        this.subscription.add(subscription);
    }

    private void unsubscribe() {
        subscription.unsubscribe();
    }

    private <T> void subscribe(Observable<? extends T> observable, Action1<? super T> action) {
        observable
                .onBackpressureBuffer()
                .compose(lifeCycleOnMainThread())
                .subscribe(action::call);
    }

    private <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> eventAction) {
        RxBus.instance()
                .asObservable(aClass)
                .compose(lifeCycleOnMainThread())
                .subscribe(eventAction);
    }

    private <T> void subscribeCommonReq(@NonNull Observable<? extends T> observable, Action1<? super T> action1) {
        observable
                .compose(commonObservableTransformer())
                .subscribe(action1);
    }

    private <T> void subscribeAsync(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super T> subscriber) {
        observable
                .compose(commonObservableTransformer())
                .subscribe(subscriber);
    }

    private <T> void subscribeAsync(@NonNull Observable<? extends T> observable, @NonNull ExObserver<? super T> observer) {
        observable
                .compose(commonObservableTransformer(observer))
                .subscribe(observer);
    }

    private <T> void subscribeCommonReq(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super T> subscriber) {
        observable
                .compose(commonObservableTransformer())
                .subscribe(subscriber);
    }

    private <T, R> void subscribeCommonReqConcat(@NonNull Observable<? extends T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<? super R> subscriber) {
        observable
                .compose(commonObservableTransformer())
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T, R> void subscribeCommonReqConcat(@NonNull Observable<? extends T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<? super R> subscriber, Action1<? super T> action1) {
        observable
                .compose(commonObservableTransformer())
                .doOnNext(action1)
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T, R> void subscribeCommonReqConcat(@NonNull Observable<? extends T> observable, Func1<? super T, ? extends Observable<? extends R>> converter) {
        observable
                .compose(commonObservableTransformer())
                .concatMap(converter)
                .subscribe();
    }

    private <T, R> void subscribeCommonReqConcat(@NonNull Observable<? extends T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<? super R> observer) {
        observable
                .compose(commonObservableTransformer(observer))
                .concatMap(converter)
                .subscribe(observer);
    }

    private <T, R, N> void subscribeCommonReqConcat(@NonNull Observable<? extends T> observable, Func1<? super T, ? extends Observable<? extends R>> before, Func1<? super R, ? extends Observable<? extends N>> after, @NonNull ExObserver<? super N> observer) {
        observable
                .compose(commonObservableTransformer(before))
                .doOnSubscribe(observer::onStart)
                .concatMap(after)
                .subscribe(observer);
    }

    private <T> void subscribeCommonReq(@NonNull Observable<? extends T> observable, ExObserver<? super T> observer) {
        observable
                .compose(commonObservableTransformer(observer))
                .subscribe(observer);
    }

    private <T> void subscribeCommonReq(@NonNull Observable<? extends T> observable, ExObserver<? super T> observer, Action1<? super T> doOnNext) {
        observable
                .compose(commonObservableTransformer(observer))
                .doOnNext(doOnNext)
                .subscribe(observer);
    }

    private <T> void subscribeCommonReq(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super T> subscriber, Action1<? super T> doOnNext) {
        observable
                .compose(commonObservableTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    private <T> void subscribeCommonNoResp(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super T> subscriber) {
        observable
                .compose(commonObservableTransformer())
                .subscribe(subscriber);
    }

    private <T> void subscribeCommonNoResp(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super T> subscriber, Action1<? super T> doOnNext) {
        observable
                .compose(commonObservableTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    private <T, R> void subscribeCommonReqWithFunc(@NonNull Observable<? extends T> observable, @NonNull Subscriber<? super R> subscriber, Func1<? super T, ? extends Observable<? extends R>> func) {
        observable
                .compose(commonObservableTransformer())
                .concatMap(func)
                .subscribe(subscriber);
    }

    protected void onUnsubscribe() {
        Timber.i("onUnsubscribe---------------------- %d", Thread.currentThread().getId());
    }

    protected <N> Observable<N> dataFlatMap(N dataEntity) {
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

    protected void postError(Throwable throwable) {
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

    private static RequestBody convertToText(String text) {
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

    protected <T> Observable<T> mockCommonRespObs(T t) {
        return Observable
                .create((Subscriber<? super T> subscriber) -> {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Observable<T> mockCommonRespObsFunc(Func0<T> func) {
        return Observable
                .create((Subscriber<? super T> subscriber) -> {
                    subscriber.onNext(func.call());
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected Observable mockErrorRespObs() {
        return Observable
                .error(new IllegalArgumentException("Error response"))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Observable.Transformer<T, T> lifeCycleOnMainThread() {
        return (observable) ->
                observable.observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycleTransformer());
    }

    protected <T> Observable.Transformer<T, T> commonObservableTransformer() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <T, R> Observable.Transformer<T, R> commonObservableTransformer(Func1<? super T, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <T> Observable.Transformer<T, T> commonObservableTransformer(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(startListener::onStart);
    }

    protected <T> Observable.Transformer<T, T> commonObservableTransformer1(Func1<T, ? extends Observable<T>> func, OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(startListener::onStart)
                .compose(lifecycleTransformer());
    }

    protected <T, R> Observable.Transformer<T, R> commonObservableTransformer2(Func1<? super T, ? extends Observable<? extends R>> func, OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(startListener::onStart)
                .compose(lifecycleTransformer());
    }
}
