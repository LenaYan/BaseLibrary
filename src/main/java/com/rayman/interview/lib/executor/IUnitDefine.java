package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.model.model.RomanNumber;

public interface IUnitDefine {

    RomanNumber getRomanBySymbol(String symbol);

    int getUnitcount();

    int saveUnit(RomanNumber romanNumber, String symbol);

    void clear();

    void remove(RomanNumber romanNumber);
}
