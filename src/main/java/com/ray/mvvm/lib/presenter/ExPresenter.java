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
import com.ray.mvvm.lib.model.model.RespEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class ExPresenter extends CommonPresenter {

    private <Y extends RespEntity> Single<Y> respCheck(Y respEntity) {
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

    protected Single<RespEntity> mockResp() {
        return Single
                .create((SingleEmitter<RespEntity> emitter) ->
                        emitter.onSuccess(new RespEntity(ErrorEvent.SUCCESS)))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends RespEntity> Single<T> mockResp(T t) {
        return Single
                .<T>create(emitter ->
                        emitter.onSuccess(t))
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends RespEntity> SingleTransformer<T, T> applyAsyncExReq() {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle());
    }

    protected <T extends RespEntity> SingleTransformer<T, T> applyAsyncExReq(Consumer<Disposable> doOnSubscribe) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .doOnSubscribe(doOnSubscribe);
    }

    protected <T extends RespEntity, R> SingleTransformer<T, R> applyAsyncExReq(Function<? super T, ? extends Single<? extends R>> func) {
        return single -> single
                .subscribeOn(Schedulers.io())
                .doOnDispose(this::onUnsubscribe)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::postError)
                .compose(bindLifecycle())
                .flatMap(func);
    }

}
