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

package com.ray.mvvm.lib.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.ray.mvvm.lib.model.model.ListRespEntity;
import com.ray.mvvm.lib.presenter.IPresenter;
import com.ray.mvvm.lib.view.adapter.list.base.ListAdapter;
import com.ray.mvvm.lib.view.base.view.IView;

public abstract class ListRespVM<P extends IPresenter, V extends IView, D> extends SwipRefreshVM<P, V, ListRespEntity<D>> {

    private final RecyclerView.LayoutManager layoutManager;
    private final ListAdapter<D> adapter;

    public ListRespVM(P presenter, V view, RecyclerView.LayoutManager layoutManager, ListAdapter<D> adapter) {
        super(presenter, view);
        this.layoutManager = layoutManager;
        this.adapter = adapter;
    }

    @Override
    protected final boolean isRespNull(ListRespEntity<D> data) {
        return data == null || data.getList() == null || data.getList().size() == 0;
    }

    public ListAdapter<D> getAdapter() {
        return adapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    protected void bindResp(ListRespEntity<D> data, int originState) {
        adapter.setList(data.getList());
        layoutManager.scrollToPosition(0);
    }

}
