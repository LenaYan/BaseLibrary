package com.rayman.interview.lib.executor.interfaces;

import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.state.ResultState;

import java.util.List;

public interface IUnitDefine {

    ResultState define(List<String> stringList);

    RomanNumber getRomanBySymbol(String symbol);

    int getUnitcount();
}
