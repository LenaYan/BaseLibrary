package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.executor.interfaces.IPriceCalculate;
import com.rayman.interview.lib.executor.interfaces.IPriceDefine;
import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.List;

public class SymbolPriceCalculator implements IPriceCalculate {

    private IUnitDefine iUnitDefine;
    private IPriceDefine iPriceDefine;

    public SymbolPriceCalculator(IUnitDefine iUnitDefine, IPriceDefine iPriceDefine) {
        this.iUnitDefine = iUnitDefine;
        this.iPriceDefine = iPriceDefine;
    }

    @Override
    public ResultState calculate(List<String> stringList) {
        if (iUnitDefine == null || iPriceDefine == null)
            return ResultState.error("Pls define units and price map before calculating.");
        if (stringList == null || stringList.size() != 1)
            return ResultState.error("Have no entries ,pls check your input.");
        String entryDes = stringList.get(0);
        if (StringUtil.isEmpty(entryDes))
            return ResultState.error("Have no entries ,pls check your input.");
        entryDes = entryDes.trim();
        String[] array = entryDes.split("\\s+");
        if (array.length == 0) {
            return ResultState.error("Invalide input ,pls check your input.");
        }
        String entryName = null;
        StringBuilder romanString = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            RomanNumber romanNumber = iUnitDefine.getRomanBySymbol(array[i]);
            if (romanNumber != null) {
                romanString.append(romanNumber.name());
            } else if (i == array.length - 1) {
                entryName = array[i];
            } else {
                StringUtil.out(array[i] + " has not been defined yet,Pls define it.");
            }
        }
        if (romanString.length() > 0 && !StringUtil.isEmpty(entryName)) {
            int entryCount = ConvertUtil.convertRomanToNumber(romanString.toString());
            int totalPrice = (int) (iPriceDefine.getPricePerEntry(entryName) * entryCount);
            StringUtil.out(entryDes + " is " + totalPrice + " Credits.");
            return ResultState.success();
        } else {
            return ResultState.error("No symbol found for  " + entryName);
        }
    }
}
