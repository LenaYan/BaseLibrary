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

import com.ray.mvvm.lib.widget.utils.StringUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Completable;
import rx.CompletableSubscriber;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import timber.log.Timber;

public abstract class DBManager<T extends RealmModel> implements IDBManager<T> {

    static final long DATA_CACHE_TIME_LIMIT_DEFAULT = 7 * 24 * 60 * 60;
    static final int DATA_CACHE_COUNT_LIMIT_DEFAULT = 300;

    protected final Realm realm;
    private final Class<T> clazz;

    protected DBManager(Realm realm, Class<T> clazz) {
        this.realm = realm;
        this.clazz = clazz;
    }

    protected abstract String getPrimaryKey();

    @Override
    public Single<T> findItemByPoisitionObs(int position) {
        return Single.create((subscribe) -> {
                    RealmResults<T> results = realm.where(clazz).findAll();
                    if (results.size() == 0) {
                        subscribe.onSuccess(null);
                    } else {
                        subscribe.onSuccess(results.get(position));
                    }
                }
        );
    }

    @Override
    public T findItemById(long id) {
        return realm.where(clazz).equalTo(getPrimaryKey(), id).findFirst();
    }

    @Override
    public Single<T> findFirstObs() {
        return Single.create((onSubscribe) -> {
                    T t = realm.where(clazz).findFirst();
                    RealmResults<T> results = realm.where(clazz).findAll();
                    Timber.i("Count " + results.size());
                    onSubscribe.onSuccess(t);
                }
        );
    }

    @Override
    public T findFirst() {
        RealmResults<T> results = realm.where(clazz).findAll();
        if (results.size() == 0) {
            return null;
        } else {
            return results.first();
        }
    }

    @Override
    public T findFirst(Realm realm) {
        RealmResults<T> results = realm.where(clazz).findAll();
        if (results.size() == 0) {
            return null;
        } else {
            return results.first();
        }
    }

    @Override
    public T findLast() {
        RealmResults<T> results = realm.where(clazz).findAll();
        if (results.size() == 0) {
            return null;
        } else {
            return results.last();
        }
    }

    @Override
    public Single<T> findLastObs() {
        return Single.create(onSubscribe -> {
                    RealmResults<T> results = realm.where(clazz).findAll();
                    if (results.size() == 0) {
                        onSubscribe.onSuccess(null);
                    } else {
                        onSubscribe.onSuccess(results.last());
                    }
                }
        );
    }

    @Override
    public Single<List<T>> findAllObs() {
        return Single.create(subscriber -> {
                    RealmResults<T> realmResults = realm.where(clazz).findAll();
                    if (realmResults.size() == 0) {
                        subscriber.onSuccess(null);
                    } else {
                        subscriber.onSuccess(realmResults);
                    }
                }
        );
    }

    @Override
    public Single<T> insertItemObs(T t) {
        return Single
                .create(subscriber ->
                        realm.executeTransaction(
                                realm -> subscriber.onSuccess(realm.copyToRealmOrUpdate(t))));
    }

    @Override
    public Single<T> updateItemObs(T t) {
        return Single
                .create(subscriber ->
                        realm.executeTransaction(
                                realm -> subscriber.onSuccess(realm.copyToRealmOrUpdate(t))));
    }

    @Override
    public Completable updateItemAsync(T t) {
        return Completable
                .create(subscriber ->
                        realm.executeTransactionAsync(
                                realm -> realm.copyToRealmOrUpdate(t),
                                subscriber::onCompleted,
                                subscriber::onError));
    }

    @Override
    public Single<List<T>> insertListObs(List<T> list) {
        return Single
                .create(subscriber ->
                        realm.executeTransaction(
                                realm -> subscriber.onSuccess(realm.copyToRealmOrUpdate(list))));
    }

    @Override
    public Completable insertListAsyncWithoutReturn(List<T> list) {
        return Completable
                .create(subscriber ->
                        realm.executeTransactionAsync(
                                realm -> realm.copyToRealmOrUpdate(list),
                                subscriber::onCompleted,
                                subscriber::onError));
    }

    @Override
    public Single<List<T>> insertListAsyncWithReturn(List<T> list) {
        return Single
                .create(subscriber ->
                        realm.executeTransactionAsync(realm ->
                                        Single.create(singleSubscriber ->
                                                singleSubscriber.onSuccess(realm.copyToRealmOrUpdate(list)))
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(),
                                subscriber::onError));
    }

    @Override
    public Completable removeItemAsync(long id) {
        return Completable
                .create(subscriber ->
                        realm.executeTransactionAsync(realm ->
                                        Completable.create((CompletableSubscriber subscriber1) -> {
                                            try {
                                                RealmObject.deleteFromRealm(realm.where(clazz).equalTo(getPrimaryKey(), id).findFirst());
                                                subscriber1.onCompleted();
                                            } catch (Exception e) {
                                                subscriber1.onError(e);
                                            }
                                        })
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doOnCompleted(subscriber::onCompleted)
                                                .subscribe(),
                                subscriber::onError)
                );
    }

    @Override
    public Single<Boolean> removeAllAsync() {
        return Single
                .create(subscriber ->
                        realm.executeTransactionAsync(realm ->
                                        Single
                                                .create((SingleSubscriber<? super Boolean> subscriber1) ->
                                                        subscriber1.onSuccess(realm.where(clazz).findAll().deleteAllFromRealm()))
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doOnSuccess(subscriber::onSuccess)
                                                .subscribe(),
                                subscriber::onError)
                );
    }

    @Override
    public Completable volumeCheckAsync() {
        return Completable
                .create(subscriber ->
                        realm.executeTransactionAsync(realm ->
                                        Completable
                                                .create((CompletableSubscriber subscriber1) -> {
                                                    long current = System.currentTimeMillis() / 1000;
                                                    String updateTimeField = getUpdateTimeField();
                                                    if (!StringUtil.isEmpty(updateTimeField))
                                                        realm.where(clazz).lessThan(updateTimeField, current - DATA_CACHE_TIME_LIMIT_DEFAULT).findAll().deleteAllFromRealm();
                                                    RealmResults<T> realmResults = realm.where(clazz).findAll();
                                                    final int totalCount = realmResults.size();
                                                    if (totalCount > DATA_CACHE_COUNT_LIMIT_DEFAULT) {
                                                        final int start = DATA_CACHE_COUNT_LIMIT_DEFAULT;
                                                        final int end = totalCount - 1;
                                                        for (int i = start; i < end; i++) {
                                                            realmResults.deleteLastFromRealm();
                                                        }
                                                    }
                                                    Timber.i("Result" + realmResults.size());
                                                    subscriber1.onCompleted();
                                                })
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doOnCompleted(subscriber::onCompleted)
                                                .subscribe(),
                                subscriber::onError)
                );
    }

    public String getUpdateTimeField() {
        return null;
    }

    @Override
    public Completable executeTransactionAsync(Action0 action0) {
        return Completable
                .create((subscriber) ->
                        realm.executeTransactionAsync(
                                realm -> action0.call(),
                                subscriber::onCompleted,
                                subscriber::onError)
                );
    }

    @Override
    public boolean executeTransaction(Action0 action0) {
        try {
            realm.executeTransaction(realm -> action0.call());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
