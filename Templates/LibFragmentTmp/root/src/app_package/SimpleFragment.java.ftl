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
    protected int onCreateView() {
        return R.layout.${layoutName};
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
