package com.rayman.interview.lib.executor;

import com.ray.mvvm.lib.R;
import com.ray.mvvm.lib.widget.anotations.ActionType;
import com.rayman.interview.lib.model.model.MatchResult;
import com.rayman.interview.lib.model.model.PriceEntity;
import com.rayman.interview.lib.model.model.ResultEntity;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.tools.ConvertUtil;
import com.rayman.interview.lib.tools.StringUtil;

import java.util.HashMap;
import java.util.List;

public class SymbolPriceDefine implements IPriceDefine {

    private final IUnitDefine unitDefine;
    private HashMap<String, Double> priceMap = new HashMap<>();

    public SymbolPriceDefine(IUnitDefine unitDefine) {
        this.unitDefine = unitDefine;
    }

    @Override
    public ResultEntity<PriceEntity> handleInput(String input) {
        MatchResult matchResult;
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_PRICE_DEFINE, 2);
        if (!matchResult.isMatch()) {
            return ResultEntity.ERROR(R.string.invalide_input_message);
        }
        List<String> stringList = matchResult.getGroupList();
        if (stringList == null || stringList.size() != 2) {
            return ResultEntity.ERROR(R.string.invalide_input_message);
        }
        String entryDes = stringList.get(0);
        String totalPriceString = stringList.get(1);
        if (StringUtil.isEmpty(entryDes) || StringUtil.isEmpty(totalPriceString)) {
            return ResultEntity.ERROR(R.string.invalide_input_message);
        }
        entryDes = entryDes.trim();
        totalPriceString = totalPriceString.trim();
        if (!StringUtil.isInteger(totalPriceString)) {
            return ResultEntity.ERROR(R.string.no_valide_price_message);
        }
        int totalPrice = Integer.parseInt(totalPriceString);
        String[] array = entryDes.split("\\s+");
        if (array.length == 0) {
            return ResultEntity.ERROR(R.string.invalide_input_message);
        }

        String entryName = null;
        StringBuilder romanString = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            RomanNumber romanNumber = unitDefine.getRomanBySymbol(array[i]);
            if (romanNumber != null) {
                romanString.append(romanNumber.name());
            } else if (i == array.length - 1) {
                entryName = array[i];
            } else {
                return ResultEntity.ERROR(R.string.invalide_input_message, array[i]);
            }
        }
        if (romanString.length() > 0 && !StringUtil.isEmpty(entryName)) {
            int entryCount = ConvertUtil.convertRomanToNumber(romanString.toString());
            double perPrice = (double) totalPrice / (double) entryCount;
            int actionType = savePrice(entryName, perPrice);
            ResultEntity<PriceEntity> resultEntity;
            PriceEntity priceEntity = new PriceEntity(entryName, perPrice);
            switch (actionType) {
                default:
                case ActionType.ACTION_ADD:
                    resultEntity = ResultEntity.ADD(R.string.added_successfully);
                    break;
                case ActionType.ACTION_UPDATE:
                    resultEntity = ResultEntity.ADD(R.string.updated_successfully);
                    break;
            }
            resultEntity.setData(priceEntity);
            return resultEntity;
        } else {
            return ResultEntity.ERROR(R.string.no_symbol_found_format, entryDes);
        }
    }

    @Override
    public double getPriceByEntry(String name) {
        Double price = priceMap.get(name);
        return price == null ? 0.0d : price;
    }

    @Override
    public int getEntryCount() {
        return priceMap.size();
    }

    @Override
    @ActionType
    public int savePrice(String name, double price) {
        int action = priceMap.get(name) != null ? ActionType.ACTION_UPDATE : ActionType.ACTION_ADD;
        priceMap.put(name, price);
        return action;
    }

    @Override
    public void clear() {
        priceMap.clear();
    }

    @Override
    public void remove(String name) {
        priceMap.remove(name);
    }
}
