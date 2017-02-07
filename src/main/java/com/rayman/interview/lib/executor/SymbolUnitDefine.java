package com.rayman.interview.lib.executor;

import android.util.SparseArray;

import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.model.model.UnitEntity;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.List;

public class SymbolUnitDefine implements IUnitDefine {

    private SparseArray<UnitEntity> sparseArray = new SparseArray<>();

    public SymbolUnitDefine() {
    }

    @Override
    public ResultState define(List<String> stringList) {
        if (stringList == null || stringList.size() != 2)
            return ResultState.error("Have no symbols ,pls check your input.");
        String roman = stringList.get(1);
        if (StringUtil.isEmpty(roman))
            return ResultState.error("Have no romans ,pls check your input.");
        RomanNumber romanNumber = ConvertUtil.getRomanFromValue(roman.charAt(0));
        String symbol = stringList.get(0);
        if (romanNumber == null) {
            return ResultState.error(roman + "is not defined in our number list.");
        }
        if (StringUtil.isEmpty(symbol)) {
            return ResultState.error("Invalide symbol,pls check your input");
        }
        if (sparseArray.get(symbol.hashCode()) != null) {
            UnitEntity unitEntity = sparseArray.get(symbol.hashCode());
            String oldValue = unitEntity.getSmybol();
            unitEntity.setRomanNumber(romanNumber);
            StringUtil.out("Unite Replaced from (" + romanNumber.name() + ":" + symbol + "[" + oldValue + "]" + ")," + sparseArray.size() + " is defined.");
        } else {
            sparseArray.put(symbol.hashCode(), new UnitEntity(romanNumber, symbol));
            StringUtil.out("New Unite Defined (" + romanNumber.name() + ":" + symbol + ")," + sparseArray.size() + " is defined.");
        }
        return ResultState.success();
    }

    @Override
    public void saveUnit(RomanNumber romanNumber, String symbol) {

    }

    @Override
    public RomanNumber getRomanBySymbol(String symbol) {
        UnitEntity unitEntity = sparseArray.get(symbol.hashCode());
        return unitEntity == null ? null : unitEntity.getRomanNumber();
    }

    @Override
    public int getUnitcount() {
        return sparseArray.size();
    }

    @Override
    public boolean isContainsSymbol(String symbol) {
        return false;
    }

}
