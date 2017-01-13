package com.ray.mvvm.lib.view.adapter;

import android.view.View;

public interface OnItemLongClick<T> {

    void onItemLongClicked(int position, View view, T t);

}
