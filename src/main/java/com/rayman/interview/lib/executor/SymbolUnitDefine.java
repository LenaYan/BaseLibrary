package com.rayman.interview.lib.executor;

import com.ray.mvvm.lib.R;
import com.ray.mvvm.lib.widget.anotations.ActionType;
import com.rayman.interview.lib.model.model.MatchResult;
import com.rayman.interview.lib.model.model.ResultEntity;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.model.model.UnitEntity;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SymbolUnitDefine implements IUnitDefine {

    private EnumMap<RomanNumber, String> unitMap = new EnumMap<>(RomanNumber.class);

    public SymbolUnitDefine() {
    }

    @Override
    public ResultEntity<UnitEntity> handleInput(String input) {
        MatchResult matchResult;
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_UNIT_DEFINE, 2);
        if (!matchResult.isMatch()) {
            return ResultEntity.ERROR(R.string.no_symbols_message);
        }
        List<String> stringList = matchResult.getGroupList();
        if (stringList == null || stringList.size() != 2) {
            return ResultEntity.ERROR(R.string.no_symbols_message);
        }
        String roman = stringList.get(1);
        if (StringUtil.isEmpty(roman)) {
            return ResultEntity.ERROR(R.string.no_romans_message);
        }
        RomanNumber romanNumber = ConvertUtil.getRomanFromValue(roman.charAt(0));
        if (romanNumber == null) {
            return ResultEntity.ERROR(R.string.number_not_defined_format, roman);
        }
        String symbol = stringList.get(0);
        if (StringUtil.isEmpty(symbol)) {
            return ResultEntity.ERROR(R.string.invalide_symbol_message);
        }
        if (unitMap.containsValue(symbol)) {
            return ResultEntity.ERROR(R.string.symbol_already_defined_format, symbol);
        }
        int actionType = saveUnit(romanNumber, symbol);
        ResultEntity<UnitEntity> resultEntity;
        switch (actionType) {
            default:
            case ActionType.ACTION_ADD:
                resultEntity = ResultEntity.ADD(R.string.no_symbols_message);
                break;
            case ActionType.ACTION_UPDATE:
                resultEntity = ResultEntity.UPDATE(R.string.updated_successfully);
                break;
        }
        resultEntity.setData(new UnitEntity(romanNumber, symbol));
        return resultEntity;
    }

    private int saveUnit(RomanNumber romanNumber, String symbol) {
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
