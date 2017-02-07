package com.rayman.interview.lib.executor.interfaces;

import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;

import java.util.List;

public interface IUnitDefine {

    ResultState define(List<String> stringList);

    RomanNumber getRomanBySymbol(String symbol);

    void saveUnit(RomanNumber romanNumber, String symbol);

    int getUnitcount();

    boolean isContainsSymbol(String symbol);
}
