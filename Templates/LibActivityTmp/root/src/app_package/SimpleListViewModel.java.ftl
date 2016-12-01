package ${packageName}.vm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ${libPackage}.view.adapter.list.base.ListAdapter;
import ${libPackage}.viewmodel.ListVM;
import ${packageName}.contract.${pageName}Contract;

<#if isWithLoadMore>
public class ${pageName}VM extends EndLessListVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{

   public ${pageName}VM(${pageName}Contract.Presenter presenter, ${pageName}Contract.View view, LinearLayoutManager layoutManager, ListAdapter<T> adapter) {
<#else>
public class ${pageName}VM extends ListVM<${pageName}Contract.Presenter, ${pageName}Contract.View, T>{

   public ${pageName}VM(${pageName}Contract.Presenter presenter, ${pageName}Contract.View view, RecyclerView.LayoutManager layoutManager, ListAdapter<T> adapter) {
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