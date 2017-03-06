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

package com.ray.mvvm.lib.rx.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.ray.mvvm.lib.rx.onsubscribe.EditTextTextChangesOnSubscribe;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;


/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Rayman
 * Date: 09/02/2017
 * Time: 1:50 PM
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
public final class RxEditText {

    @CheckResult
    @NonNull
    public static Flowable<CharSequence> textChanges(@NonNull EditText editText) {
        return Flowable.create(new EditTextTextChangesOnSubscribe(editText), BackpressureStrategy.BUFFER);
    }

}
