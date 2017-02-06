package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.executor.interfaces.IPriceDefine;
import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolPriceDefine implements IPriceDefine {

    private HashMap<String, Double> priceMap = new HashMap<String, Double>();
    private final IUnitDefine iUnitDefine;

    public SymbolPriceDefine(IUnitDefine iUnitDefine) {
        this.iUnitDefine = iUnitDefine;
    }

    @Override
    public ResultState define(List<String> stringList) {
        if (iUnitDefine == null)
            return ResultState.error("Pls define unit map.");
        if (stringList == null || stringList.size() != 2)
            return ResultState.error("Invalide input ,pls check your input.");
        String entryDes = stringList.get(0);
        String totalPriceString = stringList.get(1);
        if (StringUtil.isEmpty(entryDes) || StringUtil.isEmpty(totalPriceString))
            return ResultState.error("Invalide input ,pls check your input.");
        entryDes = entryDes.trim();
        totalPriceString = totalPriceString.trim();
        if (!StringUtil.isInteger(totalPriceString)) {
            return ResultState.error("This entry has no valide Price, pls check your input.");
        }
        int totalPrice = Integer.parseInt(totalPriceString);
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
            double perPrice = (double) totalPrice / (double) entryCount;
            priceMap.put(entryName, perPrice);
            StringUtil.out("Entry Price inserted :(" + entryName + " is " + perPrice + " Credits pre unit); PriceMap now has " + priceMap.size() + " items.");
            return ResultState.success();
        } else {
            return ResultState.error("No symbol found for  " + entryDes);
        }
    }

    @Override
    public double getPricePerEntry(String entryName) {
        Map<String, Double> map = priceMap;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entryName.equals(entry.getKey()))
                return entry.getValue();
        }
        return 0d;
    }

    @Override
    public int getEntryCount() {
        return priceMap.size();
    }
}
