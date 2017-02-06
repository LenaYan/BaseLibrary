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

package com.ray.mvvm.lib.view.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ray.mvvm.lib.widget.eventbus.RxBus;
import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.ray.mvvm.lib.widget.utils.RxTransforms;
import com.ray.mvvm.lib.widget.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public interface IView extends IRedirect, IPageControl, LifecycleProvider<LifecycleEvent> {

    <T> LifecycleTransformer<T> bindUntilLastEvent();

    ProgressDialog getProgressDialog(boolean cancel);

    boolean isPageAlive();

    @TargetApi(Build.VERSION_CODES.N)
    default ProgressDialog getProgressDialog() {
        return getProgressDialog(false);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void showProgressDialog() {
        showProgressDialog(true);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void showProgressDialog(boolean cancelable) {
        if (!isPageAlive())
            return;
        ProgressDialog dialog = getProgressDialog(cancelable);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void hideProgressDialog() {
        ProgressDialog dialog = getProgressDialog();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default String findString(int resId) {
        return activity().getString(resId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default Drawable findDrawable(int resId) {
        return ContextCompat.getDrawable(activity(), resId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void refreshOptionsMenu() {
        activity().supportInvalidateOptionsMenu();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default String[] findStringRes(int resId) {
        return activity().getResources().getStringArray(resId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default int findDimen(int resId) {
        return activity().getResources().getDimensionPixelSize(resId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default int findColor(int resId) {
        return ContextCompat.getColor(activity(), resId);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void hideSoftwareInput() {
        View view = activity().getCurrentFocus();
        if (view != null) {
            view.post(() -> {
                InputMethodManager imm = (InputMethodManager) activity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void showSoftwareInput() {
        View view = activity().getCurrentFocus();
        if (view != null) {
            view.post(() -> {
                InputMethodManager imm = (InputMethodManager) activity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void setupTouchOutSideUI(View view) {
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

    @TargetApi(Build.VERSION_CODES.N)
    default void postRunnable(Runnable runnable) {
        activity().getWindow().getDecorView().post(runnable);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void setTitle(CharSequence title) {
        ActionBar actionBar = activity().getSupportActionBar();
        actionBar.setTitle(title);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void setSubTitle(CharSequence subTitle) {
        ActionBar actionBar = activity().getSupportActionBar();
        if (actionBar != null)
            actionBar.setSubtitle(subTitle);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void setSubTitle(int stringRes) {
        ActionBar actionBar = activity().getSupportActionBar();
        if (actionBar != null)
            actionBar.setSubtitle(activity().getString(stringRes));
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void showToast(int stringRes) {
        Activity activity = activity();
        ToastUtil.show(activity.getApplicationContext(), activity.getString(stringRes));
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void showToast(String string) {
        Activity activity = activity();
        ToastUtil.show(activity.getApplicationContext(), string);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <V> void subscribeThrottleViewEvent(Observable<V> observable, Action1<? super V> action) {
        observable
                .compose(RxTransforms.viewThrottleTransform())
                .compose(bindUntilEvent(LifecycleEvent.DESTROY))
                .subscribe(action::call);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext) {
        RxBus.instance()
                .asObservable(aClass)
                .compose(delayToResumeOnMainThread())
                .subscribe(onNext);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext, LifecycleEvent event) {
        RxBus.instance()
                .asObservable(aClass)
                .compose(delayToOnMainThread(event))
                .subscribe(onNext);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends BaseEvent> Observable.Transformer<T, T> delayToResumeOnMainThread() {
        return delayToOnMainThread(LifecycleEvent.RESUME);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends BaseEvent> Observable.Transformer<T, T> delayToOnMainThread(LifecycleEvent lifecycleEvent) {
        return (observable) ->
                observable.observeOn(AndroidSchedulers.mainThread())
                        .delay(t ->
                                lifecycle()
                                        .filter(event -> event == lifecycleEvent)
                                        .map(event -> t)
                        )
                        .compose(bindUntilLastEvent());
    }

}
