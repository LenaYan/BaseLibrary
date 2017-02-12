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

import android.databinding.ViewDataBinding;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ray.mvvm.lib.BR;
import com.ray.mvvm.lib.view.adapter.list.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public abstract class ListAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int NO_INDEX = -99;
    public static final int NO_POSITION = -1;
    public static final int ITEM_TYPE_NONE = -1;

    private List<T> list;
    private LongSparseArray<T> wrapMap = new LongSparseArray<>();

    public ListAdapter() {
    }

    public ListAdapter(List<T> list) {
        setList(list, false);
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolderIml(parent, viewType);
    }

    BaseViewHolder onCreateViewHolderIml(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = createBinding(LayoutInflater.from(parent.getContext()), parent, viewType);
        return new BaseViewHolder(viewDataBinding);
    }

    @Override
    public int getItemCount() {
        return getDataCount() + getHeaderCount();
    }

    public int getDataCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        bindingViewHolder(holder, position);
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        bindingViewHolder(holder, position);
    }

    void bindingViewHolder(BaseViewHolder holder, int position) {
        final int viewType = holder.getItemViewType();
        holder.bindData(getViewModelBRId(viewType), createViewModel(holder, position));
    }

    protected int getViewModelBRId(int viewType) {
        return BR.viewModel;
    }

    protected abstract ViewDataBinding createBinding(LayoutInflater layoutInflater, ViewGroup parent, int viewType);

    protected abstract Object createViewModel(RecyclerView.ViewHolder holder, int position);

    public void setList(List<T> list) {
        setList(list, true);
    }

    public void setList(List<T> list, boolean notify) {
        this.list = list;
        wrapMap.clear();
        if (this.list != null && getItemCount() > 0) {
            for (T t : this.list) {
                final long index = getIndex(t);
                if (index == NO_INDEX)
                    break;
                wrapMap.put(index, t);
            }
        }
        if (notify)
            notifyDataSetChanged();
    }

    public void setListWithoutHeader(List<T> list) {
        int oldSize = 0;
        if (this.list != null) {
            oldSize = this.list.size();
        }
        this.list = list;
        wrapMap.clear();
        if (getItemCount() > 0) {
            for (T t : this.list) {
                final long index = getIndex(t);
                if (index == NO_INDEX)
                    break;
                wrapMap.put(index, t);
            }
        }
        notifyItemRangeRemoved(getHeaderCount(), oldSize);
        notifyItemRangeInserted(getHeaderCount(), list.size());
    }

    public List<T> getList() {
        return list;
    }

    public final T getItem(int position) {
        final int headerCount = getHeaderCount();
        return list == null ? null : list.get(position - headerCount);
    }

    public T getItemByIndex(long id) {
        return wrapMap.get(id);
    }

    public void addItemToHead(T t) {
        addItemToHead(t, true);
    }

    public void addItemToHead(T t, boolean notify) {
        if (t == null)
            return;
        if (list == null) {
            list = new ArrayList<>();
            wrapMap.clear();
        }
        list.add(0, t);
        final long index = getIndex(t);
        if (index != NO_INDEX)
            wrapMap.put(index, t);
        if (notify)
            notifyItemInserted(0);
    }

    public void addItemToFoot(T t) {
        addItemToFoot(t, true);
    }

    public void addItemToFoot(T t, boolean notify) {
        if (t == null)
            return;
        if (list == null) {
            list = new ArrayList<>();
            wrapMap.clear();
        }
        final int position = list.size();
        list.add(position, t);
        final long index = getIndex(t);
        if (index != NO_INDEX)
            wrapMap.put(index, t);
        if (notify)
            notifyItemInserted(position);
    }

    public void addItem(int position, T t) {
        if (t == null)
            return;
        if (list == null) {
            list = new ArrayList<>();
            wrapMap.clear();
        }
        list.add(position, t);
        final long index = getIndex(t);
        if (index != NO_INDEX) {
            wrapMap.clear();
            if (this.list != null && getItemCount() > 0) {
                for (T item : this.list) {
                    final long itemIndex = getIndex(t);
                    if (itemIndex == NO_INDEX)
                        break;
                    wrapMap.put(index, item);
                }
            }
        }
        notifyItemInserted(position);
    }

    public void addItems(List<T> insertList) {
        if (insertList == null || insertList.size() == 0)
            return;
        if (list == null) {
            list = new ArrayList<>();
            wrapMap.clear();
        }
        int oldEnd = getItemCount() - 1;
        int insertCount = insertList.size();
        list.addAll(insertList);
        for (T t : insertList) {
            long index = getIndex(t);
            if (index == NO_INDEX)
                break;
            wrapMap.put(index, t);
        }
        notifyItemRangeInserted(oldEnd, insertCount);
    }

    public void setItem(int position, T t) {
        if (t == null || position == NO_POSITION)
            return;
        if (list == null) {
            list = new ArrayList<>();
            wrapMap.clear();
        }
        list.set(position, t);
        final long index = getIndex(t);
        if (index != NO_INDEX)
            wrapMap.put(index, t);
        notifyItemChanged(position);
    }

    public boolean notifyItem(T t) {
        if (t == null) return false;
        final long key = getIndex(t);
        if (key == NO_INDEX) return false;
        T localItem = wrapMap.get(key);
        if (localItem == null) return false;
        final int position = list.indexOf(localItem);
        if (position == NO_POSITION) return false;
        final int headerCount = getHeaderCount();
        notifyItemChanged(position + headerCount);
        return true;
    }

    public boolean updateItem(T t) {
        return updateItem(t, true);
    }

    public boolean updateItem(T t, boolean notify) {
        if (t == null) return false;
        final long key = getIndex(t);
        if (key == NO_INDEX) return false;
        T localItem = wrapMap.get(key);
        if (localItem == null) return false;
        final int position = list.indexOf(localItem);
        if (position == NO_POSITION) return false;
        final int headerCount = getHeaderCount();
        wrapMap.put(key, t);
        list.set(position, t);
        if (notify)
            notifyItemChanged(position + headerCount);
        return true;
    }

    public boolean updateItemByIndex(long id) {
        T localItem = wrapMap.get(id);
        if (localItem == null) return false;
        final int position = list.indexOf(localItem);
        if (position == NO_POSITION) return false;
        final int headerCount = getHeaderCount();
        notifyItemChanged(position + headerCount);
        return true;
    }

    public void removeItemByIndex(long key) {
        T localItem = wrapMap.get(id);
        if (localItem == null) return;
        final int position = list.indexOf(localItem);
        if (position == NO_POSITION) return;
        final int headerCount = getHeaderCount();
        wrapMap.remove(key);
        list.remove(position);
        notifyItemRemoved(position + headerCount);
    }

    public void removeItemByPosition(int position) {
        T t = list.get(position);
        if (t == null) return;
        final long index = getIndex(t);
        if (index == NO_INDEX) return;
        wrapMap.remove(index);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(T t) {
        removeItem(t, false);
    }

    public void removeItem(T t, boolean notifyAll) {
        if (t == null) return;
        long key = getIndex(t);
        if (key == NO_INDEX) return;
        T localItem = wrapMap.get(key);
        if (localItem == null) return;
        int position = list.indexOf(localItem);
        if (position == NO_POSITION) return;
        final int viewPosition = position + getHeaderCount();
        wrapMap.remove(key);
        list.remove(position);
        if (notifyAll || list.size() == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(viewPosition);
        }
    }

    public void removeItemNotifySelf(T t) {
        if (t == null) return;
        long key = getIndex(t);
        if (key == NO_INDEX) return;
        T localItem = wrapMap.get(key);
        if (localItem == null) return;
        int position = list.indexOf(localItem);
        if (position == NO_POSITION) return;
        final int viewPosition = position + getHeaderCount();
        wrapMap.remove(key);
        list.remove(position);
        notifyItemChanged(viewPosition);
    }

    public int getRealPosition(T t) {
        if (t == null) return RecyclerView.NO_POSITION;
        final long key = getIndex(t);
        if (key == NO_INDEX) return RecyclerView.NO_POSITION;
        T localItem = wrapMap.get(key);
        if (localItem == null) return RecyclerView.NO_POSITION;
        return list.indexOf(localItem);
    }

    public void clearList() {
        if (getItemCount() > 0) {
            list.clear();
            notifyDataSetChanged();
            wrapMap.clear();
        }
    }

    public void resetList() {
        if (getItemCount() > 0) {
            list = new ArrayList<>();
            notifyDataSetChanged();
            wrapMap.clear();
        }
    }

    public ArrayList<T> getArrayList() {
        if (list != null)
            return new ArrayList<>(list);
        return null;
    }

    public long getIndex(T t) {
        return NO_INDEX;
    }

    public int getHeaderCount() {
        return 0;
    }

}
