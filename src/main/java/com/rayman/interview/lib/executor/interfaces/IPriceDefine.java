package com.rayman.interview.lib.executor.interfaces;

public interface IPriceDefine {

    double getPriceByEntry(String name);

    int getEntryCount();

    int savePrice(String name, double price);

    void clear();

    void remove(String name);
}
