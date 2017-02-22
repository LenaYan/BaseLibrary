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
import android.support.annotation.VisibleForTesting;
import android.support.multidex.MultiDex;

import com.ray.mvvm.lib.di.IBuildComp;
import com.ray.mvvm.lib.di.modules.AppModule;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.widget.eventbus.RxBus;
import com.ray.mvvm.lib.widget.utils.DeviceUtil;
import com.ray.mvvm.lib.widget.utils.StringUtil;
import com.ray.mvvm.lib.widget.utils.ToastUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class BaseApplication extends Application implements IBuildComp {

    AppComp appComp;
    RefWatcher refWatcher;
    private Subscription subscription;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static AppComp getAppComp(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.appComp;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildComp();
        DeviceUtil.init(this);
        subscribeEvent();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher = LeakCanary.install(this);
        }
    }

    @VisibleForTesting
    public void setAppComp(AppComp appComp) {
        this.appComp = appComp;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        appComp.releaseableReferenceManager().releaseStrongReferences();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void buildComp() {
        appComp = DaggerAppComp
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void subscribeEvent() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = RxBus.instance()
                .asObservable(ErrorEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleEvent);
    }

    protected void handleEvent(ErrorEvent errorEvent) {
        if (!StringUtil.isEmpty(errorEvent.getMessage()))
            ToastUtil.show(BaseApplication.this, errorEvent.getMessage());
    }

}
