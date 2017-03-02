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

package com.ray.mvvm.lib.view.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ray.mvvm.lib.widget.anotations.ActivityAction;

public interface IRedirect {

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intent(Class<T> aClass) {
        intent(aClass, new Bundle());
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intentForResult(Class<T> aClass, int requestCode) {
        intentForResult(aClass, requestCode, null);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intent(Class<T> aClass, Bundle bundle) {
        Activity activity = activity();
        Intent intent = new Intent(activity, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void intent(Intent intent) {
        Activity activity = activity();
        activity.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intent(Class<T> aClass, Intent intent) {
        Activity activity = activity();
        intent.setClass(activity, aClass);
        activity.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intentForResult(Class<T> aClass, int requestCode, Bundle bundle) {
        Activity activity = activity();
        Intent intent = new Intent(activity, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intentForResult(Fragment fragment, Class<T> aClass, int requestCode, Bundle bundle) {
        AppCompatActivity activity = activity();
        Intent intent = new Intent(activity, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityFromFragment(fragment, intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void intentFinish() {
        Activity activity = activity();
        activity.finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void intentFinish(@ActivityAction int action) {
        Activity activity = activity();
        activity.setResult(action);
        activity.finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void intentFinish(Intent intent, int action) {
        Activity activity = activity();
        activity.setResult(action, intent);
        activity.finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default void intentFinish(Bundle bundle, @ActivityAction int action) {
        Activity activity = activity();
        Intent intent = activity.getIntent();
        if (bundle != null)
            intent.putExtras(bundle);
        activity.setResult(action, intent);
        activity.finish();
    }

    @TargetApi(Build.VERSION_CODES.N)
    default <T extends Activity> void intentFinishNewTask(Class<T> aClass) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent(aClass, intent);
    }

    AppCompatActivity activity();
}
