<?xml version="1.0"?>
<recipe>
    <#include "recipe_manifest.xml.ftl" />

<#if hasToolbar>
    <instantiate from="root/res/layout/app_bar.xml.ftl"
                 to="${escapeXmlAttribute(resOut)}/layout/${appBarLayoutName}.xml" />

    <merge from="../common/root/res/values/app_bar_dimens.xml"
             to="${escapeXmlAttribute(resOut)}/values/dimens.xml" />

    <#include "../common/recipe_no_actionbar.xml.ftl" />
</#if>

<#if isWithList>
    <instantiate from="root/res/layout/simple_list.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${simpleLayoutName}.xml" />
<#else>
    <instantiate from="root/res/layout/simple.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${simpleLayoutName}.xml" />
</#if>

<#if (isNewProject!false) && !(excludeMenu!false)>
    <#include "../common/recipe_simple_menu.xml.ftl" />
</#if>

    <instantiate from="root/src/app_package/SimpleDIActivity.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />
    <instantiate from="root/src/app_package/SimpleContract.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/contract/${pageName}Contract.java" />
    <instantiate from="root/src/app_package/SimplePresenter.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presenter/${pageName}P.java" />
<#if isWithList>
    <instantiate from="root/src/app_package/SimpleListViewModel.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/vm/${pageName}VM.java" />
<#else>
    <instantiate from="root/src/app_package/SimpleViewModel.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/vm/${pageName}VM.java" />
</#if>
    <instantiate from="root/src/app_package/SimpleVMModule.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/vm/module/${pageName}VMModule.java" />

    <open file="${escapeXmlAttribute(srcOut)}/${activityClass}.java" />
    <open file="${escapeXmlAttribute(resOut)}/layout/${simpleLayoutName}.xml" />
</recipe>
