package com.rayman.interview.lib.executor;

public interface IPriceDefine {

    double getPriceByEntry(String name);

    int getEntryCount();

    int savePrice(String name, double price);

    void clear();

    void remove(String name);
}
