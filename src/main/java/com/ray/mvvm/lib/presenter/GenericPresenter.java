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

import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.model.model.GenericRespEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class GenericPresenter extends CommonPresenter {

    private <Y extends GenericRespEntity> Single<Y> respCheck(Y respEntity) {
        return Single.create((emitter) -> {
            if (respEntity == null) {
                emitter.onError(new ErrorEvent(ErrorEvent.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == ErrorEvent.FAILURE || respEntity.getCode() != ErrorEvent.SUCCESS) {
                ErrorEvent errorEvent = new ErrorEvent(respEntity.getCode(), respEntity.getMessage());
                emitter.onError(errorEvent);
            } else {
                emitter.onSuccess(respEntity);
            }
        });
    }

    protected <T> Single<GenericRespEntity<T>> mockGenericWith(T t) {
        return Single
                .<GenericRespEntity<T>>create(emitter ->
                        emitter.onSuccess(new GenericRespEntity<>(ErrorEvent.SUCCESS, t))
                )
                .delay(3, TimeUnit.SECONDS);
    }

    protected <R, T extends GenericRespEntity<R>> SingleTransformer<T, R> applyAsyncReq() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T extends GenericRespEntity> SingleTransformer<T, T> applyAsyncReqNoneResp() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <R, T extends GenericRespEntity<R>> SingleTransformer<T, R> applyAsyncReq(Consumer<Disposable> doOnSubscribe) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .doOnSubscribe(doOnSubscribe);
    }

    protected <R, T extends GenericRespEntity<R>> SingleTransformer<T, R> applyAsyncReq(Function<? super R, ? extends Single<? extends R>> func) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .map(T::getData)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .flatMap(func);
    }

    protected <R, T extends GenericRespEntity<R>> SingleTransformer<T, R> applyAsyncReq(Function<? super T, ? extends Single<? extends R>> func, Consumer<Disposable> doOnSubscribe) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(doOnSubscribe)
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .flatMap(func);
    }

}
