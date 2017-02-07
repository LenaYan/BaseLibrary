package com.rayman.interview.lib.executor;

import com.ray.mvvm.lib.widget.anotations.ActionType;
import com.rayman.interview.lib.executor.interfaces.IPriceDefine;

import java.util.HashMap;

public class SymbolPriceDefine implements IPriceDefine {

    private HashMap<String, Double> priceMap = new HashMap<>();

    public SymbolPriceDefine() {
    }

    @Override
    public double getPriceByEntry(String name) {
        Double price = priceMap.get(name);
        return price == null ? 0.0d : price;
    }

    @Override
    public int getEntryCount() {
        return priceMap.size();
    }

    @Override
    public int savePrice(String name, double price) {
        int action = priceMap.get(name) != null ? ActionType.ACTION_UPDATE : ActionType.ACTION_ADD;
        priceMap.put(name, price);
        return action;
    }

    @Override
    public void clear() {
        priceMap.clear();
    }

    @Override
    public void remove(String name) {
        priceMap.remove(name);
    }
}
