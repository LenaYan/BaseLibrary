package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SymbolUnitDefine implements IUnitDefine {

    private EnumMap<RomanNumber, String> romanSymbolMap = new EnumMap<RomanNumber, String>(RomanNumber.class);

    @Override
    public ResultState define(List<String> stringList) {
        if (stringList == null || stringList.size() != 2)
            return ResultState.error("Have no symbols ,pls check your input.");
        String roman = stringList.get(1);
        if (StringUtil.isEmpty(roman))
            return ResultState.error("Have no romans ,pls check your input.");
        RomanNumber romanNumber = ConvertUtil.getRomanFromValue(roman.charAt(0));
        if (romanNumber == null) {
            return ResultState.error(roman + "is not defined in our number list.");
        }
        String sombol = stringList.get(0);
        if (StringUtil.isEmpty(sombol)) {
            return ResultState.error("Invalide sombol,pls check your input");
        }
        saveUnit(romanNumber, sombol);
        return ResultState.success();
    }

    @Override
    public RomanNumber getRomanBySymbol(String symbol) {
        Map<RomanNumber, String> map = romanSymbolMap;
        for (Map.Entry<RomanNumber, String> entry : map.entrySet()) {
            if (symbol.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public int getUnitcount() {
        return romanSymbolMap.size();
    }

    private void saveUnit(RomanNumber romanNumber, String symbol) {
        if (romanSymbolMap.containsKey(romanNumber)) {
            String oldValue = romanSymbolMap.get(romanNumber);
            romanSymbolMap.put(romanNumber, symbol);
            StringUtil.out("Unite Replaced from (" + romanNumber.name() + ":" + symbol + "[" + oldValue + "]" + ")," + romanSymbolMap.size() + " is defined.");
        } else {
            romanSymbolMap.put(romanNumber, symbol);
            StringUtil.out("New Unite Defined (" + romanNumber.name() + ":" + symbol + ")," + romanSymbolMap.size() + " is defined.");
        }
    }

}
