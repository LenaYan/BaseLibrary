package com.rayman.interview.lib.executor.interfaces;

import com.rayman.interview.lib.state.ResultState;

import java.util.List;

public interface IPriceCalculate {

    ResultState calculate(List<String> stringList);

}
