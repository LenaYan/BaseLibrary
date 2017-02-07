package com.rayman.interview.lib.executor.interfaces;

import android.support.v4.util.Pair;

import com.rayman.interview.lib.model.model.RomanNumber;
import com.rayman.interview.lib.model.model.UnitEntity;
import com.rayman.interview.lib.state.ResultState;

import java.util.List;

public interface IUnitDefine {

    ResultState define(List<String> stringList);

    RomanNumber getRomanBySymbol(String symbol);

    Pair<Integer, UnitEntity> saveUnit(RomanNumber romanNumber, String symbol);

    int getUnitcount();

    boolean isContainsSymbol(String symbol);

    void clear();

    void remove(int key);
}
