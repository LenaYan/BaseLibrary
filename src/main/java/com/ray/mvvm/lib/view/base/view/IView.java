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

package com.ray.mvvm.lib.view.base.view;

import android.graphics.drawable.Drawable;

import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.functions.Action1;

public interface IView extends IRedirect, IPageControl, LifecycleProvider<LifecycleEvent> {

    String findString(int resId);

    Drawable findDrawable(int resId);

    int findColor(int resId);

    String[] findStringRes(int resId);

    int findDimen(int resId);

    void setTitle(String title);

    void setSubTitle(String subTitle);

    void hideSoftwareInput();

    void showSoftwareInput();

    void postRunnable(Runnable runnable);

    <V> void subscribeThrottleViewEvent(Observable<V> observable, Action1<? super V> action);

    <T> LifecycleTransformer<T> bindUntilLastEvent();

    <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext);

    <T extends BaseEvent> void subscribeEvent(Class<T> aClass, Action1<T> onNext, LifecycleEvent lifecycleEvent);
}
