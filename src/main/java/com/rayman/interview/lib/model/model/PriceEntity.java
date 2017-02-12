package com.rayman.interview.lib.model.model;

public class PriceEntity {

    private long id = System.nanoTime();
    private String name;
    private double price;

    public PriceEntity(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
