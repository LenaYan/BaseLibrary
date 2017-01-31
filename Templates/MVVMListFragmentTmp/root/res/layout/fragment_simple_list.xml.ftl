<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data class="${packageName}.binding.Fragment${pageName}Binding">

        <import type="${packageName}.vm.${pageName}VM" />

        <variable
            name="viewModel"
            type="${pageName}VM" />
    </data>

     <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            android:layout_width="match_parent"
            android:layout_height="match_parent">

<#if isWithSwipRefresh>
        <android.support.v4.widget.SwipeRefreshLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:colorSchemeResources="@{@color/SwipRefreshColor}"
            app:enabled="@{viewModel.enabled}"
            app:onRefreshListener="@{viewModel}"
            app:refresh="@{viewModel.refreshing}">

            <android.support.v7.widget.RecyclerView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:clipToPadding="false"
                android:paddingBottom="@dimen/padding_l"
                app:adapter="@{viewModel.adapter}"
                <#if isWithLoadMore>
                app:onLoadMore="@{viewModel}"
                </#if>
                app:layoutManager="@{viewModel.layoutManager}"/>
        </android.support.v4.widget.SwipeRefreshLayout>
<#else>
        <android.support.v7.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:clipToPadding="false"
            android:paddingBottom="@dimen/padding_l"
            app:adapter="@{viewModel.adapter}"
            <#if isWithLoadMore>
            app:onLoadMore="@{viewModel}"
            </#if>
            app:layoutManager="@{viewModel.layoutManager}"/>
</#if>
    </RelativeLayout>
</layout>