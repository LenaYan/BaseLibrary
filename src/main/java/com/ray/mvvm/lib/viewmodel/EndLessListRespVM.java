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

import android.support.v7.widget.LinearLayoutManager;

import com.ray.mvvm.lib.interfaces.ILoadMore;
import com.ray.mvvm.lib.model.model.ListRespEntity;
import com.ray.mvvm.lib.presenter.IPresenter;
import com.ray.mvvm.lib.view.adapter.list.base.ListAdapter;
import com.ray.mvvm.lib.view.base.view.IView;
import com.ray.mvvm.lib.widget.anotations.ListViewItemType;
import com.ray.mvvm.lib.widget.anotations.PageState;

public abstract class EndLessListRespVM<P extends IPresenter, V extends IView, D> extends ListRespVM<P, V, D> implements ILoadMore {

    private static final int PAGE_NUM_START = 1;

    private int pageNum = PAGE_NUM_START;
    private boolean hasMore = true;
    private int loadedPage = -1;

    public EndLessListRespVM(P presenter, V view, LinearLayoutManager layoutManager, ListAdapter<D> adapter) {
        super(presenter, view, layoutManager, adapter);
    }

    @Override
    public void startRequest(@PageState int startState) {
        setState(startState);
        pageNum = PAGE_NUM_START;
        exePageRequest(PAGE_NUM_START);
    }

    @Override
    protected final void exeRequest() {
    }

    @Override
    public final void onLoadMore() {
        if (getAdapter().getDataCount() == 0)
            return;
        if (!hasMore || loadedPage < pageNum)
            return;
        setState(PageState.LOAD_MORE);
        setListItemType(ListViewItemType.LOAD_MORE);
        pageNum = pageNum + 1;
        exePageRequest(pageNum);
    }

    @Override
    protected void handleOnErrorState() {
        super.handleOnErrorState();
        final int totalCount = getAdapter().getItemCount();
        getAdapter().notifyItemChanged(totalCount - 1);
    }

    @Override
    protected void handleOnNextState(ListRespEntity<D> data) {
        super.handleOnNextState(data);
        this.hasMore = data.isHasMore();
        loadedPage = pageNum;
        setListItemType(hasMore ? ListViewItemType.LOAD_MORE : ListViewItemType.NO_MORE);
        final int totalCount = getAdapter().getItemCount();
        getAdapter().notifyItemChanged(totalCount - 1);
        int viewCount = getLayoutManager().getChildCount();
        if (viewCount == totalCount) {
            onLoadMore();
        }
    }

    protected abstract void exePageRequest(int pageNum);

    @Override
    protected void bindResp(ListRespEntity<D> data, int originState) {
        switch (originState) {
            case PageState.LOAD_MORE:
                getAdapter().addItems(data.getList());
                break;
            case PageState.CONTENT:
            case PageState.LOADING:
            case PageState.EMPTY:
            case PageState.ERROR:
                super.bindResp(data, originState);
                break;
        }
    }
}
