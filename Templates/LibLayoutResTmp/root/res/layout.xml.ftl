<?xml version="1.0" encoding="utf-8"?>
<#if isBindingLayout>
<layout>

    <data>
    </data>

    <${rootTag} xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </${rootTag}>
</layout>
<#else>
<${rootTag} xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</${rootTag}>
</#if>