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

package com.ray.mvvm.lib.widget.anotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.ray.mvvm.lib.widget.anotations.ListViewItemType.LOAD_MORE;
import static com.ray.mvvm.lib.widget.anotations.ListViewItemType.LOAD_MORE_ERROR;
import static com.ray.mvvm.lib.widget.anotations.ListViewItemType.NO_MORE;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena
 * Date: 17/11/2016
 * Time: 10:59 AM
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
@Documented
@Retention(RetentionPolicy.SOURCE)
@IntDef({LOAD_MORE, NO_MORE, LOAD_MORE_ERROR})
public @interface ListViewItemType {
    int LOAD_MORE = -399;
    int NO_MORE = -508;
    int LOAD_MORE_ERROR = -762;
}
