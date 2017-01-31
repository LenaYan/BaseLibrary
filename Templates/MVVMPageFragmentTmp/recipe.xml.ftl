<?xml version="1.0"?>
<recipe>

    <instantiate from="root/src/app_package/SimpleFragment.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${fragmentName}.java" />
    <instantiate from="root/src/app_package/SimpleContract.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/contract/${pageName}Contract.java" />
    <instantiate from="root/src/app_package/SimplePresenter.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/presenter/${pageName}P.java" />
    <instantiate from="root/src/app_package/SimpleVMModule.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/vm/module/${pageName}VMModule.java" />

    <instantiate from="root/src/app_package/SimpleViewModel.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/vm/${pageName}VM.java" />

<#if isWithSwipRefresh>
    <instantiate from="root/res/layout/fragment_simple_swiprefresh.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
<#else>
    <#if isWithPageState>
    <instantiate from="root/res/layout/fragment_simple_state.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
    <#else>
    <instantiate from="root/res/layout/fragment_simple.xml.ftl"
                to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
    </#if>
</#if>

    <instantiate from="root/res/layout/fragment_simple.xml.ftl"
                       to="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />

    <open file="${escapeXmlAttribute(resOut)}/layout/${escapeXmlAttribute(layoutName)}.xml" />

</recipe>
