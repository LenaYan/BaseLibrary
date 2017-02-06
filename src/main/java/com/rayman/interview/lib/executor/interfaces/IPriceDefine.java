package com.rayman.interview.lib.executor.interfaces;

import com.rayman.interview.lib.state.ResultState;

import java.util.List;

public interface IPriceDefine {

    ResultState define(List<String> stringList);

    double getPricePerEntry(String entryName);

    int getEntryCount();

}
