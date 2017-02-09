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

package com.ray.mvvm.lib.rx.onsubscribe;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.common.base.Preconditions;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

public final class EditTextTextChangesOnSubscribe implements Observable.OnSubscribe<CharSequence> {

    private EditText editText;

    public EditTextTextChangesOnSubscribe(EditText editText) {
        Preconditions.checkNotNull(editText);
        this.editText = editText;
    }

    @Override
    public void call(Subscriber<? super CharSequence> subscriber) {
        checkUiThread();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(s);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                editText.removeTextChangedListener(textWatcher);
            }
        });
        subscriber.onNext(editText.getText());
    }
}
