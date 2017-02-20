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
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.model.model.GenericRespEntity;

import java.util.concurrent.TimeUnit;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class GenericPresenter extends CommonPresenter {

    private <Y extends GenericRespEntity> Single<Y> respCheck(Y respEntity) {
        return Single.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorEvent.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == ErrorEvent.FAILURE || respEntity.getCode() != ErrorEvent.SUCCESS) {
                ErrorEvent errorEvent = new ErrorEvent(respEntity.getCode(), respEntity.getMessage());
                subscriber.onError(errorEvent);
            } else {
                subscriber.onSuccess(respEntity);
            }
        });
    }

    protected <T> Single<GenericRespEntity<T>> mockGenericWith(T t) {
        return Single
                .<GenericRespEntity<T>>create(subscriber ->
                        subscriber.onSuccess(new GenericRespEntity<>(ErrorEvent.SUCCESS, t))
                )
                .delay(3, TimeUnit.SECONDS);
    }

    protected <R, T extends GenericRespEntity<R>> Single.Transformer<T, R> applyAsyncGeneric() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T extends GenericRespEntity> Single.Transformer<T, T> applyAsyncGenericVoid() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <R, T extends GenericRespEntity<R>> Single.Transformer<T, R> applyAsyncGeneric(OnStartAction startListener) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .doOnSubscribe(startListener::onStart);
    }

    protected <R, T extends GenericRespEntity<R>> Single.Transformer<T, R> applyAsyncGeneric(Func1<? super R, ? extends Single<? extends R>> func) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .flatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <R, T extends GenericRespEntity<R>> Single.Transformer<T, R> applyAsyncGeneric(Func1<? super T, ? extends Single<? extends R>> func, OnStartAction startListener) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .flatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(startListener::onStart)
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

}
