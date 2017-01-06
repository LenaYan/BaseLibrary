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

    @Override
    public <T> void subscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable
                .compose(lifecycleTransformer())
                .subscribe(subscriber);
    }

    protected void subscribe(Subscription subscription) {
        this.subscription.add(subscription);
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
    }

    @Override
    public <V> void subscribe(Observable<V> observable, Action1<? super V> action) {
        observable
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(action::call);
    }

    @Override
    public <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> eventAction) {
        RxBus.instance()
                .asObservable(aClass)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(eventAction);
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, Action1<T> action1) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(action1);
    }

    protected <T> void subscribeAsync(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(subscriber);
    }

    protected <T> void subscribeAsync(@NonNull Observable<T> observable, @NonNull ExObserver<T> observer) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(observer::onStart)
                .subscribe(observer);
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(subscriber);
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .concatMap(converter)
                .subscribe(subscriber);
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber, Action1<T> action1) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnNext(action1)
                .concatMap(converter)
                .subscribe(subscriber);
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .concatMap(converter)
                .subscribe();
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<R> observer) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(observer::onStart)
                .concatMap(converter)
                .subscribe(observer);
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends T>> before, Func1<? super T, ? extends Observable<? extends R>> after, @NonNull ExObserver<R> observer) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .concatMap(before)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(observer::onStart)
                .concatMap(after)
                .subscribe(observer);
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, ExObserver<T> observer) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(observer::onStart)
                .doOnError(this::postError)
                .subscribe(observer);
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, ExObserver<T> observer, Action1<T> doOnNext) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(observer::onStart)
                .doOnNext(doOnNext)
                .subscribe(observer);
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    protected <T> void subscribeCommonNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .subscribe(subscriber);
    }

    protected <T> void subscribeCommonNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    public <T, R> void subscribeCommonReqWithFunc(@NonNull Observable<T> observable, @NonNull Subscriber<R> subscriber, Func1<? super T, ? extends Observable<? extends R>> func) {
        observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
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

    public static RequestBody convertToText(String text) {
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

}
