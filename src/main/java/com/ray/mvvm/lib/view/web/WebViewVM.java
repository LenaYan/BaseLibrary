package com.ray.mvvm.lib.view.web;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ray.mvvm.lib.BR;

/**
 * Created by Android Studio.
 * ProjectName: V2EXAndroidClient
 * Author:  Lena
 * Date: 16/01/2017
 * Time: 11:06 AM
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
public class WebViewVM extends BaseObservable implements SwipeRefreshLayout.OnRefreshListener {

    private String url;
    private boolean refreshing = false;

    public WebViewVM(String url) {
        this.url = url;
    }

    public void loadUrl(String url) {
        setUrl(url);
        setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        setUrl(url);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }
}
