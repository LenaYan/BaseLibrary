package ${packageName}.vm.module;

import ${libPackage}.di.scope.PerFragment;
import ${packageName}.${pageName}Fragment;
import ${packageName}.presenter.${pageName}P;
import ${packageName}.vm.${pageName}VM;

import dagger.Module;
import dagger.Provides;

@Module
public class ${pageName}VMModule {

	private ${pageName}Fragment fragment;

	public ${pageName}VMModule(${pageName}Fragment fragment) {
		this.fragment = fragment;
	}

	@Provides
	@PerFragment
	${pageName}VM provideVM(${pageName}P presenter){
    	${pageName}VM viewModel = new ${pageName}VM(presenter, fragment);
    	fragment.setViewModel(viewModel);
		return viewModel;
	}

}
