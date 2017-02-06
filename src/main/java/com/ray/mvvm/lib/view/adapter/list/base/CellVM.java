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

package com.ray.mvvm.lib.view.adapter.list.base;

import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ray.mvvm.lib.view.adapter.OnItemClick;

public class CellVM<T> extends BaseObservable {

    protected T data;
    private RecyclerView.ViewHolder viewHolder;
    private int position = ListAdapter.NO_POSITION;
    private OnItemClick<T> itemClick;

    public CellVM() {
    }

    public CellVM(T data, OnItemClick<T> itemClick) {
        this.data = data;
        this.itemClick = itemClick;
    }

    public CellVM(T data) {
        this.data = data;
    }

    public CellVM(T data, RecyclerView.ViewHolder viewHolder) {
        this.data = data;
        this.viewHolder = viewHolder;
    }

    public CellVM(T data, RecyclerView.ViewHolder viewHolder, OnItemClick<T> itemClick) {
        this.data = data;
        this.viewHolder = viewHolder;
        this.itemClick = itemClick;
    }

    public CellVM(T data, int position) {
        this.data = data;
        this.position = position;
    }

    public CellVM(T data, int position, OnItemClick<T> itemClick) {
        this.data = data;
        this.itemClick = itemClick;
        this.position = position;
    }

    public void setItemClick(OnItemClick<T> itemClick) {
        this.itemClick = itemClick;
    }

    public T getData() {
        return data;
    }

    public OnItemClick<T> getItemClick() {
        return itemClick;
    }

    public void onContentClicked(View view) {
        if (itemClick != null) {
            itemClick.onItemClick(getPosition(), view, data);
        }
    }

    public int getPosition() {
        return viewHolder == null ? position : viewHolder.getAdapterPosition();
    }

}
