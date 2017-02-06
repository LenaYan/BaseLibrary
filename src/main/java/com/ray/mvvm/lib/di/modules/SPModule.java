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

package com.ray.mvvm.lib.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.ray.mvvm.lib.di.scope.PerApplication;
import com.ray.mvvm.lib.widget.anotations.ContextType;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public final class SPModule {

    @Provides
    @PerApplication
    static SharedPreferences provideSP(@Named(ContextType.APPLICATION) Context context) {
        return context.getSharedPreferences(context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
    }

}
