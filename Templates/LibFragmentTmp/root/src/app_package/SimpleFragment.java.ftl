package ${packageName};

import ${libPackage}.view.base.page.BaseDIFragment;
import ${realAppPackage}.R;
import ${packageName}.contract.Dagger${pageName}Contract_Comp;
import ${packageName}.contract.${pageName}Contract;
import ${packageName}.vm.${pageName}VM;
import ${packageName}.vm.module.${pageName}VMModule;

import javax.inject.Inject;

public class ${fragmentName} extends BaseDIFragment implements ${pageName}Contract.View{

    @Inject ${pageName}VM viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fragment${pageName}Binding binding = Fragment${pageName}Binding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        bindLifecycle(viewModel);
        return binding.getRoot();
    }

    @Override
    public void buildComp() {
        Dagger${pageName}Contract_Comp
            .builder()
            .fragmentComp(fragmentComp())
            .${moduleNameUncapFirst}(new ${pageName}VMModule(this))
            .build()
            .inject(this);

    }
}
