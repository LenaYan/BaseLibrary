package com.rayman.interview.lib.calculator;

import com.rayman.interview.lib.executor.interfaces.IConvertor;
import com.rayman.interview.lib.executor.interfaces.IPriceCalculate;
import com.rayman.interview.lib.executor.interfaces.IPriceDefine;
import com.rayman.interview.lib.executor.interfaces.IUnitDefine;
import com.rayman.interview.lib.model.model.MatchResult;
import com.rayman.interview.lib.state.ResultState;
import com.rayman.interview.lib.tools.StringUtil;

import javax.inject.Inject;

public class CurrencyCalculator {

    private IUnitDefine unitDefine;
    private IPriceDefine priceDefine;
    private IConvertor convertor;
    private IPriceCalculate priceCalculate;

    @Inject
    public CurrencyCalculator(IUnitDefine unitDefine, IPriceDefine priceDefine, IConvertor convertor, IPriceCalculate priceCalculate) {
        this.unitDefine = unitDefine;
        this.priceDefine = priceDefine;
        this.convertor = convertor;
        this.priceCalculate = priceCalculate;
    }

    public IUnitDefine getUnitDefine() {
        return unitDefine;
    }

    public IPriceDefine getPriceDefine() {
        return priceDefine;
    }

    public ResultState handleInput(String input) {
        if (StringUtil.isEmpty(input))
            return ResultState.error("Input line is Empty...");
        MatchResult matchResult;
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_UNIT_DEFINE, 2);
        if (matchResult.isMatch()) {
            return unitDefine.define(matchResult.getGroupList());
        }
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_PRICE_DEFINE, 2);
        if (matchResult.isMatch()) {
            return priceDefine.define(matchResult.getGroupList());
        }
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_ROMAN_CONVERT, 1);
        if (matchResult.isMatch()) {
            return convertor.convert(matchResult.getGroupList());
        }
        matchResult = StringUtil.matchString(input, StringUtil.REGEX_PRICE_CALCULATE, 1);
        if (matchResult.isMatch()) {
            return priceCalculate.calculate(matchResult.getGroupList());
        }
        return ResultState.error("I have no idea what you are talking about~");
    }

}
