package com.ray.mvvm.sample.model.model;

import com.ray.mvvm.lib.view.adapter.OnItemClick;
import com.ray.mvvm.lib.view.base.page.BaseDIActivity;

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
public class SampleEntity {

    private Class<? extends BaseDIActivity> tClass;
    private String name;
    private OnItemClick<SampleEntity> itemClick;

    public SampleEntity(Class<? extends BaseDIActivity> tClass, String name, OnItemClick<SampleEntity> itemClick) {
        this.tClass = tClass;
        this.name = name;
        this.itemClick = itemClick;
    }

    public Class<? extends BaseDIActivity> gettClass() {
        return tClass;
    }

    public String getName() {
        return name;
    }

    public OnItemClick<SampleEntity> getItemClick() {
        return itemClick;
    }
}
