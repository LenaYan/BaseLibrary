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

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;

public final class EditTextTextChangesOnSubscribe implements FlowableOnSubscribe<CharSequence> {

    private EditText editText;

    public EditTextTextChangesOnSubscribe(EditText editText) {
        editText = Preconditions.checkNotNull(editText);
        this.editText = editText;
    }

    @Override
    public void subscribe(FlowableEmitter<CharSequence> e) throws Exception {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!e.isCancelled()) {
                    e.onNext(s);
                }
            }
        };

        editText.addTextChangedListener(textWatcher);
        e.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                editText.removeTextChangedListener(textWatcher);
            }
        });
        e.onNext(editText.getText());
    }
}
