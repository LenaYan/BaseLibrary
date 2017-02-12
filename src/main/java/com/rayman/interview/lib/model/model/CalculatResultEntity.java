package com.rayman.interview.lib.model.model;

public class CalculatResultEntity {

    private final String input;
    private String name;
    private String result;
    private int resultFormatRes;

    public CalculatResultEntity(String input) {
        this.input = input;
    }

    public CalculatResultEntity(String input, String name, String result, int resultFormatRes) {
        this.input = input;
        this.name = name;
        this.result = result;
        this.resultFormatRes = resultFormatRes;
    }

    public String getInput() {
        return input;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    public int getResultFormatRes() {
        return resultFormatRes;
    }
}
