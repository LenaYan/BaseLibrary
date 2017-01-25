/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Sat, 8 Oct 2016 23:47:37 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Sat, 8 Oct 2016 23:47:37 +0800.
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

package com.ray.mvvm.lib.view.base.page;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ray.mvvm.lib.R;
import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.ray.mvvm.lib.widget.lifecycle.RxPageLifecycle;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class BaseActivity extends AppCompatActivity implements IView {

    protected BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();
    private ProgressDialog progressDialog;

    @Override
    public ProgressDialog getProgressDialog(boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(cancelable);
            progressDialog.setContentView(com.ray.mvvm.lib.R.layout.layout_progress);
        }
        return progressDialog;
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }



    @Override
    public AppCompatActivity activity() {
        return this;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isPageAlive() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? !isFinishing() && !isDestroyed() : !isFinishing();
    }

    @CheckResult
    @Nonnull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable().doOnError(throwable -> {
            showToast(throwable.getMessage());
            throwable.printStackTrace();
        });
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull LifecycleEvent activityEvent) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, activityEvent);
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilLastEvent() {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, LifecycleEvent.DESTROY);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxPageLifecycle.bind(lifecycleSubject);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.doOnError(throwable -> {
            throwable.printStackTrace();
            showToast(throwable.getMessage());
        });
        lifecycleSubject.onNext(LifecycleEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    public void onResume() {
        lifecycleSubject.onNext(LifecycleEvent.RESUME);
        super.onResume();
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Deprecated
    protected final void initViews(int layoutResID) {
        initViews(layoutResID, true);
    }

    @Deprecated
    protected final void initViews(int layoutResID, boolean homeAsUp) {
        setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUp);
            }
        }
    }

}
