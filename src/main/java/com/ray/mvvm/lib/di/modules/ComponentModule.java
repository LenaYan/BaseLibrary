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

package com.ray.mvvm.lib.di.modules;

import android.content.Context;

import com.ray.mvvm.lib.BuildConfig;
import com.ray.mvvm.lib.di.scope.PerApplication;
import com.ray.mvvm.lib.model.http.adapter.GenericRespEntityAdapter;
import com.ray.mvvm.lib.widget.anotations.ContextType;
import com.rayman.interview.lib.executor.IPriceDefine;
import com.rayman.interview.lib.executor.IUnitDefine;
import com.rayman.interview.lib.executor.SymbolPriceDefine;
import com.rayman.interview.lib.executor.SymbolUnitDefine;
import com.squareup.moshi.Moshi;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

@Module
public final class ComponentModule {

    @Provides
    @PerApplication
    static Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(new GenericRespEntityAdapter())
                .build();
    }

    @Provides
    @PerApplication
    static Realm provideRealm(@Named(ContextType.APPLICATION) Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .migration((realm, oldVersion, newVersion) ->
                        Timber.i("migration  Old version %d -> New version %d", oldVersion, newVersion))
                .schemaVersion(BuildConfig.VERSION_CODE)
                .deleteRealmIfMigrationNeeded()
                .name("Library.realm")
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        return Realm.getDefaultInstance();
    }

    @Provides
    @PerApplication
    static IUnitDefine provideUnitDefine() {
        return new SymbolUnitDefine();
    }

    @Provides
    @PerApplication
    static IPriceDefine providePriceDefine(IUnitDefine unitDefine) {
        return new SymbolPriceDefine(unitDefine);
    }
}
