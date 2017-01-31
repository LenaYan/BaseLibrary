package ${packageName}.vm;

<#if isWithSwipRefresh>
import ${libPackage}.viewmodel.SwipRefreshVM;
<#else>
    <#if isWithPageState>
import ${libPackage}.viewmodel.PageVM;
    <#else>
import ${libPackage}.viewmodel.BaseVM;
    </#if>
</#if>
import ${packageName}.contract.${pageName}Contract;

<#if isWithSwipRefresh>
public class ${pageName}VM extends SwipRefreshVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{
<#else>
    <#if isWithPageState>
public class ${pageName}VM extends PageVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{
    <#else>
public class ${pageName}VM extends BaseVM<${pageName}Contract.Presenter, ${pageName}Contract.View>{
    </#if>
</#if>

   public ${pageName}VM(${pageName}Contract.Presenter presenter, ${pageName}Contract.View view) {
   		super(presenter, view);
   }

}
