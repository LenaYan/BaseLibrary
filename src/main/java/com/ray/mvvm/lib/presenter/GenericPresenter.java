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
import com.ray.mvvm.lib.model.model.RespEntity;
import com.ray.mvvm.lib.model.model.VoidEntity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GenericPresenter extends ExPresenter {

    private <T> void subscribeGenericReq(@NonNull Observable<RespEntity<T>> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .compose(genericObservableTransformer())
                .subscribe(subscriber);
    }

    private <T, R> void subscribeGenericReqConvert(@NonNull Observable<RespEntity<T>> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber) {
        observable
                .compose(genericObservableTransformer())
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T, R> void subscribeGenericReqConvert(@NonNull Observable<RespEntity<T>> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber, Action1<T> action1) {
        observable
                .compose(genericObservableTransformer())
                .doOnNext(action1)
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T, R> void subscribeGenericReqConvert(@NonNull Observable<RespEntity<T>> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<R> observer) {
        observable
                .compose(genericObservableTransformer(observer))
                .concatMap(converter)
                .subscribe(observer);
    }

    private <T> void subscribeGenericReq(@NonNull Observable<RespEntity<T>> observable, ExObserver<T> observer) {
        observable
                .compose(genericObservableTransformer(observer))
                .doOnError(this::postError)
                .subscribe(observer);
    }

    private <T> void subscribeGenericReq(@NonNull Observable<RespEntity<T>> observable, ExObserver<T> observer, Action1<T> doOnNext) {
        observable
                .compose(genericObservableTransformer(observer))
                .doOnNext(doOnNext)
                .subscribe(observer);
    }

    private <T> void subscribeGenericReq(@NonNull Observable<RespEntity<T>> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        observable
                .compose(genericObservableTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    private <T extends RespEntity<VoidEntity>> void subscribeGenericNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .compose(genericObservableTransformerVoid())
                .subscribe(subscriber);
    }

    private <T extends RespEntity> void subscribeGenericNoResp(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        observable
                .compose(genericObservableTransformerVoid())
                .doOnNext(doOnNext)
                .doOnNext((t) -> Timber.i("doOnNext  %d", Thread.currentThread().getId()))
                .subscribe(subscriber);
    }

    private <Y extends RespEntity> Observable<Y> respFlatMap(Y respEntity) {
        return Observable.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorType.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == RespEntity.FAILURE || respEntity.getCode() != RespEntity.SUCCESS) {
                postError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
                subscriber.onError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
            } else {
                subscriber.onNext(respEntity);
            }
        });
    }

    private <Z> Observable<Z> genericRespFlatMap(RespEntity<Z> respEntity) {
        return Observable.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorType.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == RespEntity.FAILURE || respEntity.getCode() != RespEntity.SUCCESS) {
                postError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
                subscriber.onError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
            } else {
                subscriber.onNext(respEntity.getData());
            }
        });
    }

    protected Observable<RespEntity> mockGenericRespObservable() {
        return Observable
                .create(new Observable.OnSubscribe<RespEntity>() {
                    @Override
                    public void call(Subscriber<? super RespEntity> subscriber) {
                        subscriber.onNext(new RespEntity(RespEntity.SUCCESS));
                    }
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T> Observable<RespEntity<T>> mockGenericRespObservable(T t) {
        return Observable
                .create(new Observable.OnSubscribe<RespEntity<T>>() {
                    @Override
                    public void call(Subscriber<? super RespEntity<T>> subscriber) {
                        subscriber.onNext(new RespEntity<>(RespEntity.SUCCESS, t));
                    }
                })
                .delay(3, TimeUnit.SECONDS);
    }


    protected <R, T extends RespEntity<R>> Observable.Transformer<T, R> genericObservableTransformer() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::genericRespFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <T extends RespEntity> Observable.Transformer<T, T> genericObservableTransformerVoid() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::respFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <R, T extends RespEntity<R>> Observable.Transformer<T, R> genericObservableTransformer(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::genericRespFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(startListener::onStart);
    }

    protected <R, T extends RespEntity<R>> Observable.Transformer<T, R> genericObservableTransformer(Func1<? super R, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::genericRespFlatMap)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <R, T extends RespEntity<R>> Observable.Transformer<T, R> genericObservableTransformer(Func1<? super T, ? extends Observable<? extends R>> func, OnStartAction startListener) {
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
