package com.rayman.interview.lib.executor;

import com.ray.mvvm.lib.widget.anotations.ActionType;
import com.rayman.interview.lib.model.model.RomanNumber;

import java.util.EnumMap;
import java.util.Map;

public class SymbolUnitDefine implements IUnitDefine {

    private EnumMap<RomanNumber, String> unitMap = new EnumMap<>(RomanNumber.class);

    public SymbolUnitDefine() {
    }

    @Override
    public int saveUnit(RomanNumber romanNumber, String symbol) {
        int action = unitMap.containsKey(romanNumber) ? ActionType.ACTION_UPDATE : ActionType.ACTION_ADD;
        unitMap.put(romanNumber, symbol);
        return action;
    }

    @Override
    public RomanNumber getRomanBySymbol(String symbol) {
        Map<RomanNumber, String> map = unitMap;
        for (Map.Entry<RomanNumber, String> entry : map.entrySet()) {
            if (symbol.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public int getUnitcount() {
        return unitMap.size();
    }

    @Override
    public void clear() {
        unitMap.clear();
    }

    @Override
    public void remove(RomanNumber romanNumber) {
        if (unitMap.containsKey(romanNumber))
            unitMap.remove(romanNumber);
    }
}
