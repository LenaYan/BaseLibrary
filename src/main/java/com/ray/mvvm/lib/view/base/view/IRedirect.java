/*
 *
 *  Copyright (c) 2016 Lena.t.Yan
 *  Unauthorized copying of this file, via any medium is strictly prohibited proprietary and confidential.
 *  Created on Sat, 8 Oct 2016 23:47:37 +0800.
 *  ProjectName: V2EXAndroidClient ; ModuleName: app ; ClassName: TopicListCellVM.
 *  Author: Lena; Last Modified: Sat, 8 Oct 2016 23:47:37 +0800.
 *  This file is originally created by Lena.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ray.mvvm.lib.view.base.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ray.mvvm.lib.widget.anotations.ActivityAction;

public interface IRedirect {

    void intent(Intent intent);

    <T extends Activity> void intent(Class<T> aClass);

    <T extends Activity> void intent(Class<T> aClass, Bundle bundle);

    <T extends Activity> void intent(Class<T> aClass, Intent intent);

    <T extends Activity> void intentForResult(Class<T> aClass, int requestCode);

    <T extends Activity> void intentForResult(Class<T> aClass, int requestCode, Bundle bundle);

    void intentFinish();

    void intentFinish(@ActivityAction int action);

    void intentFinish(Intent intent, @ActivityAction int action);

    void intentFinish(Bundle bundle, @ActivityAction int action);

    <T extends Activity> void intentFinishNewTask(Class<T> aClass);
}
