package com.rayman.interview.lib.model.model;

import android.support.annotation.NonNull;

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
