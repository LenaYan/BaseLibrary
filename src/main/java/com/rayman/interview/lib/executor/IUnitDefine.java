package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.model.model.ResultEntity;
import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.model.model.UnitEntity;

public interface IUnitDefine {

    RomanNumber getRomanBySymbol(String symbol);

    ResultEntity<UnitEntity> handleInput(String input);

    int getUnitCount();

    void clear();

    void remove(RomanNumber romanNumber);
}
