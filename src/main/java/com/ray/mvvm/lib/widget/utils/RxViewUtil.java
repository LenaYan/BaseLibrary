package com.ray.mvvm.lib.widget.utils;

import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Android Studio.
 * ProjectName: IndustrialControlCircle
 * Author:  Lena
 * Date: 5/4/16
 * Time: 11:38 AM
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
public final class RxViewUtil {

    public static Observable<Void> throttleClick(MenuItem menuItem) {
        return RxMenuItem
                .clicks(menuItem)
                .throttleLast(1, TimeUnit.SECONDS)
                .onBackpressureLatest();
    }

    public static Observable<Void> throttleClick(View view) {
        return RxView
                .clicks(view)
                .throttleLast(1, TimeUnit.SECONDS)
                .onBackpressureLatest();
    }

    public static Observable<CharSequence> throttleQueryTextChanges(SearchView searchView) {
        return RxSearchView.queryTextChanges(searchView)
                .filter(input -> !searchView.isIconified())
                .throttleLast(200, TimeUnit.MILLISECONDS)
//                .filter(charSequence -> charSequence != null && charSequence.length() > 0)
                .debounce(500, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Void> throttleViewClicked(View view) {
        return RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .debounce(500, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Void> throttleViewClicked(MenuItem menuItem) {
        return RxMenuItem.clicks(menuItem)
                .throttleFirst(1, TimeUnit.SECONDS)
                .debounce(500, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread());
    }

}
