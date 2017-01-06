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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

public class BaseFragment extends Fragment implements IView {

    protected BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();
    private ProgressDialog progressDialog;

    @Nonnull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull LifecycleEvent lifecycleEvent) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, lifecycleEvent);
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntilLastEvent() {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, LifecycleEvent.DETACH);
    }

    @Override
    public <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext) {
        RxBus.instance()
                .asObservable(aClass)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilLastEvent())
                .subscribe(onNext);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxPageLifecycle.bind(lifecycleSubject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifecycleSubject.onNext(LifecycleEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(LifecycleEvent.CREATE);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(LifecycleEvent.CREATE_VIEW);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        lifecycleSubject.onNext(LifecycleEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void showToast(int stringRes) {
        ToastUtil.show(getActivity(), getString(stringRes));
    }

    @Override
    public void showToast(String string) {
        ToastUtil.show(getActivity(), string);
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(true);
    }

    @Override
    public void showProgressDialog(boolean cancelable) {
        if (!isFragmentActive()) return;
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
        if (isFragmentActive() && progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void intent(Class aClass) {
        intent(aClass, null);
    }

    @Override
    public void intentForResult(Class aClass, int requestCode) {
        intentForResult(aClass, requestCode, null);
    }

    @Override
    public void intent(Class aClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void intent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void intent(Intent intent, Class<?> aClass) {
        intent.setClass(getActivity(), aClass);
        startActivity(intent);
    }

    @Override
    public void intentForResult(Class aClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void intentFinish() {
        getActivity().finish();
    }

    @Override
    public void intentFinish(@ActivityAction int action) {
        getActivity().setResult(action);
        getActivity().finish();
    }

    @Override
    public void intentFinish(Intent intent, int action) {
        getActivity().setResult(action, intent);
        getActivity().finish();
    }

    @Override
    public void intentFinish(Bundle bundle, @ActivityAction int action) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(action, intent);
        getActivity().finish();
    }

    @Override
    public void intentFinishNewTask(Class<?> aClass) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent(intent, aClass);
    }

    @Override
    public String findString(int resId) {
        return super.getString(resId);
    }

    @Override
    public Drawable findDrawable(int resId) {
        return ContextCompat.getDrawable(getActivity(), resId);
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
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void refreshOptionsMenu() {
        getActivity().invalidateOptionsMenu();
    }

    public boolean isFragmentActive() {
        return getActivity() != null && !isDetached() && isAdded();
    }

    @Override
    public void setSubTitle(String subTitle) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity.getSupportActionBar() != null)
            appCompatActivity.getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public int findColor(int resId) {
        return ContextCompat.getColor(getActivity(), resId);
    }

    @Override
    public void hideSoftwareInput() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void showSoftwareInput() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void postRunnable(Runnable runnable) {
        View view = getView();
        if (view != null)
            view.post(runnable);
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
}
