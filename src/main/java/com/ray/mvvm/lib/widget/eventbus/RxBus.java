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

package com.ray.mvvm.lib.widget.eventbus;

import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class RxBus {

    private static volatile RxBus instance = new RxBus();
    private Subject<? super BaseEvent> bus = PublishSubject.create().toSerialized();

    private RxBus() {
    }

    public static RxBus instance() {
        RxBus rxBus = instance;
        if (rxBus == null) {
            synchronized (RxBus.class) {
                rxBus = instance;
                if (rxBus == null) {
                    rxBus = instance = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public <T extends BaseEvent> void post(T event) {
        bus.onNext(event);
    }

    public <T extends BaseEvent> Observable<T> asObservable(final Class<T> aClass) {
        return bus
                .filter(aClass::isInstance)
                .cast(aClass);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
