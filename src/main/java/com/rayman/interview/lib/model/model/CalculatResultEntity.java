package com.rayman.interview.lib.model.model;

public class CalculatResultEntity {

    private String input;
    private String result;

    public CalculatResultEntity(String input, String result) {
        this.input = input;
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
