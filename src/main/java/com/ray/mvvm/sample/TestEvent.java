package com.ray.mvvm.sample;

import com.ray.mvvm.lib.widget.eventbus.event.BaseEvent;
import com.ray.mvvm.sample.model.model.TestEntity;

/**
 * Created by Android Studio.
 * ProjectName: AndroidMVVM
 * Author:  Lena
 * Date: 28/01/2017
 * Time: 10:34 PM
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
public class TestEvent extends BaseEvent {

    private final TestEntity entity;

    public TestEvent(TestEntity entity) {
        this.entity = entity;
    }

    public TestEntity getEntity() {
        return entity;
    }
}
