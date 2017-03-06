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

package com.ray.mvvm.lib.db;

import com.ray.mvvm.lib.interfaces.Action;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmModel;

public interface IDBManager<T extends RealmModel> {

    T findItemById(long id);

    Single<T> findItemByPoisitionObs(int position);

    Single<T> findFirstObs();

    Single<T> findLastObs();

    T findFirst();

    T findFirst(Realm realm);

    T findLast();

    Single<List<T>> findAllObs();

    Single<List<T>> insertListObs(List<T> list);

    Completable insertListAsyncWithoutReturn(List<T> list);

    Single<List<T>> insertListAsyncWithReturn(List<T> list);

    Single<T> insertItemObs(T t);

    Single<T> updateItemObs(T t);

    Completable updateItemAsync(T t);

    Completable removeItemAsync(long id);

    Single<Boolean> removeAllAsync();

    Completable executeTransactionAsync(Action action0);

    Completable volumeCheckAsync();

    boolean executeTransaction(Action action0);

}
