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

package com.ray.mvvm.lib.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ray.mvvm.lib.di.modules.AppModule;
import com.ray.mvvm.lib.di.scope.PerApplication;
import com.ray.mvvm.lib.model.cache.IFileControl;
import com.ray.mvvm.lib.widget.anotations.ContextType;
import com.rayman.interview.lib.executor.interfaces.IConvertor;
import com.rayman.interview.lib.executor.interfaces.IPriceCalculate;
import com.rayman.interview.lib.executor.interfaces.IPriceDefine;
import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.squareup.moshi.Moshi;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Named;

import dagger.Component;
import dagger.releasablereferences.ForReleasableReferences;
import dagger.releasablereferences.ReleasableReferenceManager;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@PerApplication
@Component(modules = {AppModule.class})
public interface AppComp {

    @ForReleasableReferences(PerApplication.class)
    ReleasableReferenceManager releaseableReferenceManager();

    @Named(ContextType.APPLICATION)
    Context appContext();

    Application app();

    SharedPreferences preference();

    Retrofit retrofit();

    IFileControl fileControl();

    Moshi moshi();

    OkHttpClient okHttpClient();

    Realm realm();

    RxPermissions rxPermission();

    IUnitDefine unitDefine();

    IPriceDefine priceDefine();

    IConvertor convertor();

    IPriceCalculate priceCalculate();

}
