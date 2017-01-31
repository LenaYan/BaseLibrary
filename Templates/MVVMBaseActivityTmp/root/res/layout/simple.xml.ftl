<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data class="${packageName}.binding.<#if hasToolbar && appBarLayoutName??>Content<#else>Activity</#if>${pageName}Binding">

        <import type="${packageName}.vm.${pageName}VM" />

        <variable
            name="viewModel"
            type="${pageName}VM" />
    </data>

    <android.support.constraint.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
    <#if hasToolbar && appBarLayoutName??>
        app:layout_behavior="@string/appbar_scrolling_view_behavior"
        tools:showIn="@layout/${appBarLayoutName}"
    </#if>
        tools:context="${relativePackage}.${activityClass}">

    <#if isNewProject!false>
        <TextView
            android:text="Hello World!"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </#if>
    </android.support.constraint.ConstraintLayout>
</layout>