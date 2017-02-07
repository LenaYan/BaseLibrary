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

import com.ray.mvvm.lib.interfaces.OnStartAction;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.model.model.RespEntity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class ExPresenter extends CommonPresenter {

    private <Y extends RespEntity> Observable<Y> respCheck(Y respEntity) {
        return Observable.create((subscriber) -> {
            if (respEntity == null) {
                subscriber.onError(new ErrorEvent(ErrorEvent.RESP_BODY_EMPTY, "Response entity is empty."));
            } else if (respEntity.getCode() == ErrorEvent.FAILURE || respEntity.getCode() != ErrorEvent.SUCCESS) {
                ErrorEvent errorEvent = new ErrorEvent(respEntity.getCode(), respEntity.getMessage());
                subscriber.onError(errorEvent);
            } else {
                subscriber.onNext(respEntity);
            }
            subscriber.onCompleted();
        });
    }

    protected Observable<RespEntity> mockResp() {
        return Observable
                .create((Subscriber<? super RespEntity> subscriber) -> {
                    subscriber.onNext(new RespEntity(ErrorEvent.SUCCESS));
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends RespEntity> Observable<T> mockResp(T t) {
        return Observable
                .create((Subscriber<? super T> subscriber) -> {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                })
                .delay(3, TimeUnit.SECONDS);
    }

    protected <T extends RespEntity> Observable.Transformer<T, T> applyAsyncEx() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

    protected <T extends RespEntity> Observable.Transformer<T, T> applyAsyncEx(OnStartAction startListener) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle())
                .doOnSubscribe(startListener::onStart);
    }

    protected <T extends RespEntity, R> Observable.Transformer<T, R> applyAsyncEx(Func1<? super T, ? extends Observable<? extends R>> func) {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(this::onUnsubscribe)
                .doOnError(this::postError)
                .flatMap(this::respCheck)
                .concatMap(func)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindLifecycle());
    }

}
