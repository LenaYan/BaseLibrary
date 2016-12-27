package ${packageName}.vm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ${libPackage}.view.adapter.list.base.StateListAdapter;
import ${libPackage}.viewmodel.ListVM;
import ${packageName}.contract.${pageName}Contract;

<#if isWithLoadMore>
public class ${pageName}VM extends EndLessListVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{

   public ${pageName}VM(${pageName}Contract.Presenter presenter, ${pageName}Contract.View view, LinearLayoutManager layoutManager, StateListAdapter<T> adapter) {
<#else>
public class ${pageName}VM extends <#if isShowPageState>State</#if>ListVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{

   public ${pageName}VM(${pageName}Contract.Presenter presenter, ${pageName}Contract.View view, RecyclerView.LayoutManager layoutManager, <#if isShowPageState>State</#if>ListAdapter<T> adapter) {
</#if>
   		super(presenter, view, layoutManager, adapter);
   }

    @Override
<#if isWithLoadMore>
    protected void exePageRequest(int pageNum) {
<#else>
    protected void exeRequest() {
</#if>
    }

}