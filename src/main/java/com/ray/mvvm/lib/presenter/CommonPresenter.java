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

    protected CompositeSubscription subscription = new CompositeSubscription();
    protected boolean isAlive;

    public CommonPresenter() {
    }

    @Override
    public void onViewAttach() {
        isAlive = true;
    }

    @Override
    public void onViewDetach() {
        isAlive = false;
        subscription.unsubscribe();
    }

    @Override
    public void subscribe(Subscription subscription) {
        this.subscription.add(subscription);
    }

    @Override
    public <V> void subscribe(Observable<V> publishSubject, Action1<? super V> action) {
        subscribe(publishSubject
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action::call));
    }

    @Override
    public <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> eventAction) {
        subscribe(
                RxBus.instance()
                        .asObservable(aClass)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(eventAction)
        );
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, Action1<T> action1) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(t -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action1)
        );
    }

    protected <T> void subscribeAsync(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(t -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    protected <T> void subscribeAsync(@NonNull Observable<T> observable, @NonNull ExObserver<T> observer) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(t -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer)
        );
    }

    protected boolean isAlive() {
        return isAlive;
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap(converter)
                        .subscribe(subscriber)
        );
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber, Action1<T> action1) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(action1)
                        .concatMap(converter)
                        .subscribe(subscriber)
        );
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap(converter)
                        .subscribe()
        );
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<R> observer) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap(converter)
                        .subscribe(observer)
        );
    }

    protected <T, R> void subscribeCommonReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends T>> before, Func1<? super T, ? extends Observable<? extends R>> after, @NonNull ExObserver<R> observer) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .concatMap(before)
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap(after)
                        .subscribe(observer)
        );
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, ExObserver<T> observer) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(this::postError)
                        .subscribe(observer)
        );
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, ExObserver<T> observer, Action1<T> doOnNext) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(doOnNext)
                        .subscribe(observer)
        );
    }

    protected <T> void subscribeCommonReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(doOnNext)
                        .subscribe(subscriber)
        );
    }

    protected <T> void subscribeCommonNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    protected <T> void subscribeCommonNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(data -> isAlive)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(doOnNext)
                        .doOnNext((t) -> Timber.i("doOnNext %s  %d", isAlive, Thread.currentThread().getId()))
                        .subscribe(subscriber)
        );
    }

    public <T, R> void subscribeCommonReqWithFunc(@NonNull Observable<T> observable, @NonNull Subscriber<R> subscriber, Func1<? super T, ? extends Observable<? extends R>> func) {
        subscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .doOnUnsubscribe(this::unSubscribe)
                        .doOnError(this::postError)
                        .filter(respEntity -> isAlive)
                        .flatMap(this::dataFlatMap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap(func)
                        .subscribe(subscriber)
        );
    }

    protected void unSubscribe() {
        Timber.i("unSubscribe %s  %d", isAlive, Thread.currentThread().getId());
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
