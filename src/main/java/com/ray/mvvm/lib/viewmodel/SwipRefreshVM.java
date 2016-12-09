/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Sat, 8 Oct 2016 23:56:12 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Sat, 8 Oct 2016 23:56:12 +0800.
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

package com.ray.mvvm.lib.viewmodel;

import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ray.mvvm.lib.BR;
import com.ray.mvvm.lib.presenter.IPresenter;
import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.anotations.PageState;

import rx.subjects.PublishSubject;

public abstract class SwipRefreshVM<P extends IPresenter, V extends IView, D> extends PageVM<P, V, D> implements SwipeRefreshLayout.OnRefreshListener {

    private PublishSubject<Boolean> refreshSubject;
    private boolean isRefreshing;

    private int[] colors = {com.ray.mvvm.lib.R.color.SwipRefreshColor};

    public SwipRefreshVM(P presenter, V view) {
        super(presenter, view);
        refreshSubject = PublishSubject.create();
        presenter.subscribe(refreshSubject
                        .filter(refresh -> refresh != isRefreshing),
                refresh -> {
                    isRefreshing = refresh;
                    notifyPropertyChanged(BR.refreshing);
                    if (isRefreshing)
                        onRefresh();
                });
    }

    public int[] getColors() {
        return colors;
    }

    @Override
    public void setState(@PageState int state) {
        super.setState(state);
        notifyPropertyChanged(BR.enabled);
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        if (isRefreshing)
            refreshSubject.onNext(false);
    }

    @Override
    public void onRefresh() {
        if (getState() == PageState.LOADING) {
            refreshSubject.onNext(false);
            return;
        }
        this.isRefreshing = true;
        startRefreshRequest();
    }

    protected void startRefreshRequestAuto() {
        startRefreshRequestAuto(PageState.CONTENT);
    }

    protected void startRefreshRequestAuto(@PageState int state) {
        switch (state) {
            case PageState.EMPTY:
            case PageState.ERROR:
                startRequest();
                break;
            case PageState.CONTENT:
                refreshSubject.onNext(true);
                break;
            case PageState.LOADING:
                break;
        }
    }

    public void startRefreshRequest() {
        final int state = getState();
        switch (state) {
            case PageState.EMPTY:
            case PageState.ERROR:
                startRequest(PageState.LOADING);
//                refreshSubject.onNext(false);
                break;
            case PageState.CONTENT:
                startRequest(PageState.CONTENT);
                break;
            case PageState.LOADING:
                break;
        }
    }

    @Bindable
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Bindable
    public boolean isEnabled() {
        final int state = getState();
        return isRefreshing || state != PageState.LOADING;
    }
}
