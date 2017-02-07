package com.rayman.interview.lib.model.model;

/**
 * Created by Android Studio.
 * ProjectName: MerchantGuideToGalaxy
 * Author:  Lena
 * Date: 07/02/2017
 * Time: 9:00 PM
 * \ --------------------------------------------
 * \| The only thing that is constant is change!  |
 * \ --------------------------------------------
 * \  \
 * \   \   \_\_    _/_/
 * \    \      \__/
 * \           (oo)\_______
 * \           (__)\       )\/\
 * \               ||----w |
 * \               ||     ||
 */
public class PriceEntity {

    private String name;
    private double price;

    public PriceEntity(String name, double price) {
        this.name = name;
        this.price = price;
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
