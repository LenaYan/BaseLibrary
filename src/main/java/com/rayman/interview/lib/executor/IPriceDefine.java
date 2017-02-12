package com.rayman.interview.lib.executor;

import com.rayman.interview.lib.model.model.PriceEntity;
import com.rayman.interview.lib.model.model.ResultEntity;

public interface IPriceDefine {

    double getPriceByEntry(String name);

    ResultEntity<PriceEntity> handleInput(String input);

    int getEntryCount();

    int savePrice(String name, double price);

    void clear();

    void remove(String name);
}
