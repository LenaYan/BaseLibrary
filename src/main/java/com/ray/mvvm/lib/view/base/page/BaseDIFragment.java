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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ray.mvvm.lib.BR;
import com.ray.mvvm.lib.di.IBuildComp;
import com.ray.mvvm.lib.view.base.comp.ActivityComp;
import com.ray.mvvm.lib.view.base.comp.DaggerFragmentComp;
import com.ray.mvvm.lib.view.base.comp.FragmentComp;
import com.ray.mvvm.lib.viewmodel.BaseVM;
import com.squareup.leakcanary.RefWatcher;

public abstract class BaseDIFragment extends BaseFragment implements IBuildComp {

    private FragmentComp fragmentComp;
    private BaseVM viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildComp();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, onCreateView(), container, false);
        dataBinding.setVariable(BR.viewModel, viewModel);
        return dataBinding.getRoot();
    }

    protected abstract int onCreateView();

    public void setViewModel(BaseVM viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel != null)
            viewModel.presenter().onViewAttach();
    }

    @Override
    public void onDetach() {
        if (viewModel != null)
            viewModel.presenter().onViewDetach();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        final RefWatcher refWatcher = fragmentComp.refWatcher();
        refWatcher.watch(viewModel.presenter());
        refWatcher.watch(viewModel);
        refWatcher.watch(this);
        super.onDestroy();
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
