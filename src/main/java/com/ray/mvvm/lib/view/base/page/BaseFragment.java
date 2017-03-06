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

package com.ray.mvvm.lib.view.base.page;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.ray.mvvm.lib.widget.lifecycle.RxPageLifecycle;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BaseFragment extends Fragment implements IView {

    protected BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();
    private ProgressDialog progressDialog;
    private AppCompatActivity activity;

    @Override
    public ProgressDialog getProgressDialog(boolean cancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(cancelable);
            progressDialog.setView(getActivity().getLayoutInflater().inflate(com.ray.mvvm.lib.R.layout.layout_progress, null));
        }
        return progressDialog;
    }

    @Override
    public AppCompatActivity activity() {
        if (activity == null)
            activity = (AppCompatActivity) getActivity();
        return activity;
    }

    @Nonnull
    @Override
    public Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject;
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
    public <T extends Activity> void intentForResult(Class<T> aClass, int requestCode) {
        intentForResult(this, aClass, requestCode, null);
    }

    @Override
    public <T extends Activity> void intentForResult(Class<T> aClass, int requestCode, Bundle bundle) {
        intentForResult(this, aClass, requestCode, bundle);
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
    public boolean isPageAlive() {
        return getActivity() != null && !isDetached() && isAdded();
    }
}
