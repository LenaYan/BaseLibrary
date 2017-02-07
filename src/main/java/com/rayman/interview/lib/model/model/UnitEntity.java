package com.rayman.interview.lib.model.model;

import android.support.annotation.NonNull;

/**
 * Created by Android Studio.
 * ProjectName: MerchantGuideToGalaxy
 * Author:  Lena
 * Date: 07/02/2017
 * Time: 10:03 AM
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
public class UnitEntity {

    private RomanNumber romanNumber;
    private String smybol;

    public UnitEntity(@NonNull RomanNumber romanNumber, @NonNull String smybol) {
        this.romanNumber = romanNumber;
        this.smybol = smybol;
    }

    public RomanNumber getRomanNumber() {
        return romanNumber;
    }

    public void setRomanNumber(@NonNull RomanNumber romanNumber) {
        this.romanNumber = romanNumber;
    }

    public String getSmybol() {
        return smybol;
    }

    public void setSmybol(@NonNull String smybol) {
        this.smybol = smybol;
    }
}
