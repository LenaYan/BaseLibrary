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

package com.ray.mvvm.lib.view.base.page;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ray.mvvm.lib.app.BaseApplication;
import com.ray.mvvm.lib.di.IBuildComp;
import com.ray.mvvm.lib.view.base.comp.ActivityComp;
import com.ray.mvvm.lib.view.base.comp.DaggerFragmentComp;
import com.ray.mvvm.lib.view.base.comp.FragmentComp;
import com.ray.mvvm.lib.viewmodel.BaseVM;
import com.ray.mvvm.lib.widget.lifecycle.LifecycleEvent;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseDIFragment extends BaseFragment implements IBuildComp {

    private FragmentComp fragmentComp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildComp();
    }

    protected void bindLifecycle(BaseVM viewModel) {
        viewModel.presenter().setLifecycleObs(lifecycleSubject.asObservable(), LifecycleEvent.DETACH);
        lifecycleSubject
                .subscribe(activityEvent -> {
                    if (activityEvent == LifecycleEvent.DETACH) {
                        final RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
                        if (refWatcher != null) {
                            refWatcher.watch(viewModel);
                            refWatcher.watch(viewModel.presenter());
                            refWatcher.watch(this);
                        }
                    }
                });
    }

    protected FragmentComp fragmentComp() {
        if (fragmentComp == null)
            fragmentComp = DaggerFragmentComp.builder()
                    .activityComp(activityComp())
                    .build();
        return fragmentComp;
    }

    private ActivityComp activityComp() {
        return ((BaseDIActivity) getActivity()).getActivityComp();
    }


}
