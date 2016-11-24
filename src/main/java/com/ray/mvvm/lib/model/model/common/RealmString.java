package com.ray.mvvm.lib.model.model.common;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by Android Studio.
 * ProjectName: GankClient
 * Author:  Lena
 * Date: 24/11/2016
 * Time: 12:42 AM
 * \ ----------------------------------------
 * \| A small leak will sink a great ship.!  |
 * \ ----------------------------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
@RealmClass
public class RealmString implements RealmModel {
    private String value;

    public RealmString() {
    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
