package com.rayman.interview.lib.model.model;

import com.ray.mvvm.lib.view.adapter.OnItemClick;

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
