package com.ray.mvvm.lib.viewmodel;

import android.support.v7.widget.RecyclerView;

import com.ray.mvvm.lib.presenter.IPresenter;
import com.ray.mvvm.lib.view.adapter.list.base.StateListAdapter;
import com.ray.mvvm.lib.view.base.view.IView;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena
 * Date: 20/01/2017
 * Time: 6:37 PM
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
public abstract class StateListRespVM<P extends IPresenter, V extends IView, D> extends ListRespVM<P, V, D> {

    public StateListRespVM(P presenter, V view, RecyclerView.LayoutManager layoutManager, StateListAdapter<D> adapter) {
        super(presenter, view, layoutManager, adapter);
        adapter.setStateVM(this);
    }
}
