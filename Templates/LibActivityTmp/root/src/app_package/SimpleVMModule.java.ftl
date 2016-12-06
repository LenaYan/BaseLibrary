package ${packageName}.vm.module;
<#if isWithList>
import android.support.v7.widget.LinearLayoutManager;

import ${libPackage}.di.modules.LayoutManagerModule;
import ${libPackage}.widget.anotations.ListType;
import javax.inject.Named;
</#if>

import ${libPackage}.di.scope.PerActivity;
import ${packageName}.contract.${pageName}Contract;
import ${packageName}.presenter.${pageName}P;
import ${packageName}.vm.${pageName}VM;

import dagger.Module;
import dagger.Provides;

<#if isWithList>
@Module(includes = LayoutManagerModule.class)
<#else>
@Module
</#if>
public class ${pageName}VMModule {

	private ${pageName}Contract.View view;

	public ${pageName}VMModule(${pageName}Contract.View view){
		this.view=view;
	}

	@Provides
	@PerActivity
<#if isWithList>
	${pageName}VM provideVM(${pageName}P presenter, @Named(ListType.VERTICAL) LinearLayoutManager layoutManager){
		return new ${pageName}VM(presenter,view, layoutManager, adapter);
<#else>
	${pageName}VM provideVM(${pageName}P presenter){
		return new ${pageName}VM(presenter,view);
</#if>
	}

}