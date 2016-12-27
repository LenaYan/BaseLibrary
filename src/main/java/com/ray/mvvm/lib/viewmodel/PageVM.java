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
import android.view.View;

import com.ray.mvvm.lib.BR;
import com.ray.mvvm.lib.model.http.ExObserver;
import com.ray.mvvm.lib.model.http.event.ErrorEvent;
import com.ray.mvvm.lib.presenter.IPresenter;
import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.anotations.ListViewItemType;
import com.ray.mvvm.lib.widget.anotations.PageState;

import java.io.IOException;

public abstract class PageVM<P extends IPresenter, V extends IView, D> extends StateVM<P, V> implements ExObserver<D> {

    protected D entity;

    public PageVM(P presenter, V view) {
        super(presenter, view);
    }

    @Override
    public void onRetryClicked(View view) {
        startRequest(PageState.LOADING);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onError(Throwable throwable) {
        String errorString;
        if (throwable instanceof ErrorEvent) {
            errorString = throwable.getMessage();
        } else if (throwable instanceof IOException) {
            setNetworkError(true);
            errorString = view.findString(com.ray.mvvm.lib.R.string.state_network_error_msg);
        } else {
            errorString = throwable.getMessage();
        }
        view.showToast(errorString);
        setErrorString(errorString);
        throwable.printStackTrace();
        handleOnErrorState();
        onCompleted();
    }

    @Override
    public void onNext(D data) {
        final int state = getState();
        handleOnNextState(data);
        bindResp(data, state);
        onCompleted();
    }

    @Override
    public void onCompleted() {
    }

    protected void handleOnStartState(int lastState) {

    }

    protected void handleOnNextState(D data) {
        final int startState = getState();
        switch (startState) {
            case PageState.LOAD_MORE:
                setState(PageState.CONTENT);
                break;
            case PageState.LOADING:
            case PageState.CONTENT:
                setState(isRespNull(data) ? PageState.EMPTY : PageState.CONTENT);
                break;
        }
    }

    protected void handleOnErrorState() {
        final int startState = getState();
        switch (startState) {
            case PageState.LOADING:
                setState(PageState.ERROR);
                break;
            case PageState.LOAD_MORE:
                setListItemType(ListViewItemType.LOAD_MORE_ERROR);
                break;
            case PageState.CONTENT:
                break;
        }
    }

    public void setEntity(D entity) {
        this.entity = entity;
        notifyPropertyChanged(BR.entity);
    }

    @Bindable
    public D getEntity() {
        return entity;
    }

    protected void bindResp(D data, int originState) {
        setEntity(data);
    }

    public void startRequest() {
        startRequest(PageState.LOADING);
    }

    public void startRequest(@PageState int startState) {
        setState(startState);
        exeRequest();
    }

    protected boolean isRespNull(D data) {
        return data == null;
    }

    protected void exeRequest() {

    }

}
