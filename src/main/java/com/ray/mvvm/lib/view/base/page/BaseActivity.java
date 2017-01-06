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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ray.mvvm.lib.R;
import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.anotations.ActivityAction;
import com.ray.mvvm.lib.widget.eventbus.RxBus;
import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.ray.mvvm.lib.widget.lifecycle.RxPageLifecycle;
import com.ray.mvvm.lib.widget.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class BaseActivity extends AppCompatActivity implements IView {

    protected BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();
    private ProgressDialog progressDialog;

    @CheckResult
    @Nonnull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
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
    public <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext) {
        RxBus.instance()
                .asObservable(aClass)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilLastEvent())
                .subscribe(onNext);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void showProgressDialog() {
        showProgressDialog(true);
    }

    @Override
    public void showProgressDialog(boolean cancelable) {
        if (isFinishing()) return;
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(cancelable);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            progressDialog.setContentView(com.ray.mvvm.lib.R.layout.layout_progress);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (isFinishing()) return;
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.show(getApplicationContext(), getString(stringRes));
    }

    @Override
    public void showToast(String string) {
        ToastUtil.show(getApplicationContext(), string);
    }

    protected void initViews(int layoutResID) {
        initViews(layoutResID, true);
    }

    protected void initViews(int layoutResID, boolean homeAsUp) {
        setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUp);
            }
        }
    }

    @Override
    public <T extends Activity> void intent(Class<T> aClass) {
        intent(aClass, new Bundle());
    }

    @Override
    public <T extends Activity> void intentForResult(Class<T> aClass, int requestCode) {
        intentForResult(aClass, requestCode, null);
    }

    @Override
    public <T extends Activity> void intent(Class<T> aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void intent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public <T extends Activity> void intent(Class<T> aClass, Intent intent) {
        intent.setClass(this, aClass);
        startActivity(intent);
    }

    @Override
    public <T extends Activity> void intentForResult(Class<T> aClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void intentFinish() {
        finish();
    }

    @Override
    public void intentFinish(@ActivityAction int action) {
        setResult(action);
        finish();
    }

    @Override
    public void intentFinish(Intent intent, int action) {
        setResult(action, intent);
        finish();
    }

    @Override
    public void intentFinish(Bundle bundle, @ActivityAction int action) {
        Intent intent = getIntent();
        if (bundle != null)
            intent.putExtras(bundle);
        setResult(action, intent);
        finish();
    }

    @Override
    public <T extends Activity> void intentFinishNewTask(Class<T> aClass) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent(aClass, intent);
    }

    @Override
    public String findString(int resId) {
        return super.getString(resId);
    }

    @Override
    public Drawable findDrawable(int resId) {
        return ContextCompat.getDrawable(this, resId);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void refreshOptionsMenu() {
        supportInvalidateOptionsMenu();
    }

    protected boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Override
    public void setSubTitle(String subTitle) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public String[] findStringRes(int resId) {
        return getResources().getStringArray(resId);
    }

    @Override
    public int findDimen(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    @Override
    public int findColor(int resId) {
        return ContextCompat.getColor(this, resId);
    }

    @Override
    public void hideSoftwareInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.post(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            });
        }
    }

    @Override
    public void showSoftwareInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.post(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            });
        }
    }

    @Override
    public void postRunnable(Runnable runnable) {
        this.getWindow().getDecorView().post(runnable);
    }

    @Override
    public <V> void subscribeThrottleViewEvent(Observable<V> observable, Action1<? super V> action) {
        observable
                .throttleFirst(1, TimeUnit.SECONDS)
                .debounce(500, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(LifecycleEvent.DESTROY))
                .subscribe(action::call);
    }

    public void setupTouchOutSideUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftwareInput();
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupTouchOutSideUI(innerView);
            }
        }
    }
}
