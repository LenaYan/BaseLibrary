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
import com.ray.mvvm.lib.model.model.ExRespEntity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class ExPresenter extends CommonPresenter {

    private <T extends ExRespEntity> void subscribeReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber) {
        observable
                .compose(exObservableTransformer())
                .subscribe(subscriber);
    }

    private <T extends ExRespEntity, R> void subscribeReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber) {
        observable
                .compose(exObservableTransformer())
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T extends ExRespEntity, R> void subscribeReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull Subscriber<R> subscriber, Action1<T> action1) {
        observable
                .compose(exObservableTransformer())
                .doOnNext(action1)
                .concatMap(converter)
                .subscribe(subscriber);
    }

    private <T extends ExRespEntity, R> void subscribeReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter) {
        observable
                .compose(exObservableTransformer())
                .concatMap(converter)
                .subscribe();
    }

    private <T extends ExRespEntity, R> void subscribeReqConcat(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<R> observer) {
        observable
                .compose(exObservableTransformer())
                .doOnSubscribe(observer::onStart)
                .concatMap(converter)
                .subscribe(observer);
    }

    private <T extends ExRespEntity, R> void subscribeReqFlatMap(@NonNull Observable<T> observable, Func1<? super T, ? extends Observable<? extends R>> converter, @NonNull ExObserver<R> observer) {
        observable
                .compose(exObservableTransformer(observer))
                .flatMap(converter)
                .subscribe(observer);
    }

    private <T extends ExRespEntity> void subscribeReq(@NonNull Observable<T> observable, ExObserver<T> observer) {
        observable
                .compose(exObservableTransformer(observer))
                .subscribe(observer);
    }

    private <T extends ExRespEntity> void subscribeReq(@NonNull Observable<T> observable, ExObserver<T> observer, Action1<T> doOnNext) {
        observable
                .compose(exObservableTransformer(observer))
                .doOnNext(doOnNext)
                .subscribe(observer);
    }

    private <T extends ExRespEntity> void subscribeReq(@NonNull Observable<T> observable, @NonNull Subscriber<T> subscriber, Action1<T> doOnNext) {
        observable
                .compose(exObservableTransformer())
                .doOnNext(doOnNext)
                .subscribe(subscriber);
    }

    private <T extends ExRespEntity, R> void subscribeReqWithFunc(@NonNull Observable<T> observable, @NonNull Subscriber<R> subscriber, Func1<? super T, ? extends Observable<? extends R>> func) {
        observable
                .compose(exObservableTransformer())
                .concatMap(func)
                .subscribe(subscriber);
    }

    private void subscribeNoResp(@NonNull Observable<ExRespEntity> observable, @NonNull Subscriber<ExRespEntity> subscriber) {
        observable
                .compose(exObservableTransformer())
                .subscribe(subscriber);
    }

    private void subscribeNoResp(@NonNull Observable<ExRespEntity> observable, @NonNull Subscriber<ExRespEntity> subscriber, Action1<ExRespEntity> doOnNext) {
        observable
                .compose(exObservableTransformer())
                .doOnNext(doOnNext)
                .doOnNext((t) -> Timber.i("doOnNext  %d", Thread.currentThread().getId()))
                .subscribe(subscriber);
    }

    protected <Y extends ExRespEntity> Observable<Y> respFlatMap(Y respEntity) {
        return Observable.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorType.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (!respEntity.isSuscess()) {
                postError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
                subscriber.onError(new ErrorEvent(respEntity.getCode(), respEntity.getMessage()));
            } else {
                subscriber.onNext(respEntity);
            }
            subscriber.onCompleted();
        });
    }

    protected Observable<ExRespEntity> mockRespObservable() {
        return Observable
                .create(new Observable.OnSubscribe<ExRespEntity>() {
                    @Override
                    public void call(Subscriber<? super ExRespEntity> subscriber) {
                        subscriber.onNext(new ExRespEntity(true));
                        subscriber.onCompleted();
                    }
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends ExRespEntity> Observable<T> mockRespObservable(T t) {
        return Observable
                .create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(t);
                        subscriber.onCompleted();
                    }
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends ExRespEntity> Observable.Transformer<T, T> exObservableTransformer() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::respFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

    protected <T extends ExRespEntity> Observable.Transformer<T, T> exObservableTransformer(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::respFlatMap)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer())
                .doOnSubscribe(startListener::onStart);
    }

    protected <T extends ExRespEntity, R> Observable.Transformer<T, R> exObservableTransformer(Func1<? super T, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::dataFlatMap)
                .flatMap(this::respFlatMap)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleTransformer());
    }

}
