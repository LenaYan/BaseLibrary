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

import com.ray.mvvm.lib.interfaces.OnStartAction;
import com.ray.mvvm.lib.model.http.ErrorType;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.model.model.GenericRespEntity;
import com.ray.mvvm.lib.model.model.RespEntity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class GenericPresenter extends CommonPresenter {

    private <Y extends GenericRespEntity> Observable<Y> respCheck(Y respEntity) {
        return Observable.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorType.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == RespEntity.FAILURE || respEntity.getCode() != GenericRespEntity.SUCCESS) {
                ErrorEvent errorEvent = new ErrorEvent(respEntity.getCode(), respEntity.getMessage());
                postError(errorEvent);
                subscriber.onError(errorEvent);
            } else {
                subscriber.onNext(respEntity);
            }
        });
    }

    protected <T> Observable<GenericRespEntity<T>> mockGenericWith(T t) {
        return Observable
                .create((Subscriber<? super GenericRespEntity<T>> subscriber) ->
                        subscriber.onNext(new GenericRespEntity<>(GenericRespEntity.SUCCESS, t))
                )
                .delay(3, TimeUnit.SECONDS);
    }

    protected <R, T extends GenericRespEntity<R>> Observable.Transformer<T, R> applyAsyncGeneric() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <T extends GenericRespEntity> Observable.Transformer<T, T> applyAsyncGenericVoid() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <R, T extends GenericRespEntity<R>> Observable.Transformer<T, R> applyAsyncGeneric(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle())
                .doOnSubscribe(startListener::onStart);
    }

    protected <R, T extends GenericRespEntity<R>> Observable.Transformer<T, R> applyAsyncGeneric(Func1<? super R, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .map(T::getData)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <R, T extends GenericRespEntity<R>> Observable.Transformer<T, R> applyAsyncGeneric(Func1<? super T, ? extends Observable<? extends R>> func, OnStartAction startListener) {
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
