<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data class="${packageName}.binding.Fragment${pageName}Binding">

        <import type="${packageName}.vm.${pageName}VM" />

        <variable
            name="viewModel"
            type="${pageName}VM" />
    </data>

    <RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v4.widget.SwipeRefreshLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:visibility="@{viewModel.contentVisibility}"
            app:colorSchemeResources="@{@color/SwipRefreshColor}"
            app:enabled="@{viewModel.enabled}"
            app:onRefreshListener="@{viewModel}"
            app:refresh="@{viewModel.refreshing}">

            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"/>

        </android.support.v4.widget.SwipeRefreshLayout>

    <#if isWithPageState>
        <include
            layout="@layout/state_empty_layout"
            app:viewModel="@{viewModel}" />

        <include
            layout="@layout/state_loading_layout"
            app:viewModel="@{viewModel}" />

        <include
            layout="@layout/state_error_layout"
            app:viewModel="@{viewModel}" />
    </#if>
    </RelativeLayout>
</layout>