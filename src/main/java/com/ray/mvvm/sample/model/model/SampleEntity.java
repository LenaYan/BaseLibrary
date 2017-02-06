package com.ray.mvvm.sample.model.model;

import com.ray.mvvm.lib.view.adapter.OnItemClick;

/**
 * Created by Android Studio.
 * ProjectName: AndroidMVVM
 * Author:  Lena
 * Date: 27/01/2017
 * Time: 7:44 PM
 * \ --------------------------------------------
 * \| The only thing that is constant is change!  |
 * \ --------------------------------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
public final class SampleEntity {

    private String name;
    private OnItemClick<SampleEntity> itemClick;

    public SampleEntity(String name, OnItemClick<SampleEntity> itemClick) {
        this.name = name;
        this.itemClick = itemClick;
    }

    public String getName() {
        return name;
    }

    public OnItemClick<SampleEntity> getItemClick() {
        return itemClick;
    }
}