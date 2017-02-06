package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.executor.interfaces.IConvertor;
import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.List;

public class SymbolConvertor implements IConvertor {

    private final IUnitDefine iUnitDefine;

    public SymbolConvertor(IUnitDefine iUnitDefine) {
        this.iUnitDefine = iUnitDefine;
    }

    @Override
    public ResultState convert(List<String> stringList) {
        if (iUnitDefine == null)
            return ResultState.error("Pls define unit map.");
        if (stringList == null || stringList.size() != 1)
            return ResultState.error("Have no symbols ,pls check your input.");
        String symbols = stringList.get(0);
        if (StringUtil.isEmpty(symbols))
            return ResultState.error("Have no symbols ,pls check your input.");
        symbols = symbols.trim();
        String[] symbolArray = symbols.split("\\s+");
        if (symbolArray.length == 0) {
            return ResultState.error("Invalide symbols format ,pls check your input.");
        }
        StringBuilder romanString = new StringBuilder();
        for (String item : symbolArray) {
            RomanNumber romanNumber = iUnitDefine.getRomanBySymbol(item);
            if (romanNumber != null) {
                romanString.append(romanNumber.name());
            }
        }
        if (romanString.length() > 0) {
            int result = ConvertUtil.convertRomanToNumber(romanString.toString());
            StringUtil.out(symbols + " is " + result);
            return ResultState.success();
        } else {
            return ResultState.error("No symbol found ,pls check your input");
        }
    }
}
