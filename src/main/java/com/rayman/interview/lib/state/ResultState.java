package com.rayman.interview.lib.state;

public class ResultState {

    public static final int ERROR = 0;
    public static final int SUCCESS = 1;

    private int state;

    private String message;

    public ResultState(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public ResultState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultState success() {
        return new ResultState(SUCCESS);
    }

    public static ResultState error(String msg) {
        return new ResultState(ERROR, msg);
    }

    public static ResultState error() {
        return new ResultState(ERROR);
    }

}
