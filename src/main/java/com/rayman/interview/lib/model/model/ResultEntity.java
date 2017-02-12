package com.rayman.interview.lib.model.model;

import com.ray.mvvm.lib.widget.anotations.ActionType;

/**
 * Created by Android Studio.
 * ProjectName: MerchantGuideToGalaxy
 * Author:  Rayman
 * Date: 11/02/2017
 * Time: 9:55 PM
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
public class ResultEntity<T> {

    private int result = ActionType.ACTION_NONE;
    private int msgRes;
    private Object[] msgPara;
    private T data;

    public static <T> ResultEntity<T> ERROR(int msgRes, String... msgPara) {
        return new ResultEntity<>(ActionType.ACTION_NONE, msgRes, msgPara);
    }

    public static <T> ResultEntity<T> ADD(int msgRes, String... msgPara) {
        return new ResultEntity<>(ActionType.ACTION_ADD, msgRes, msgPara);
    }

    public static <T> ResultEntity<T> ADD() {
        return new ResultEntity<>(ActionType.ACTION_ADD);
    }

    public static <T> ResultEntity<T> UPDATE(int msgRes, String... msgPara) {
        return new ResultEntity<>(ActionType.ACTION_UPDATE, msgRes, msgPara);
    }

    public static <T> ResultEntity<T> UPDATE() {
        return new ResultEntity<>(ActionType.ACTION_UPDATE);
    }

    private ResultEntity(int result) {
        this.result = result;
    }

    private ResultEntity(@ActionType int result, int msgRes, String... msgPara) {
        this.result = result;
        this.msgRes = msgRes;
        this.msgPara = msgPara;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public int getMsgRes() {
        return msgRes;
    }

    public Object[] getMsgPara() {
        return msgPara;
    }

    public void setMsgRes(int msgRes) {
        this.msgRes = msgRes;
    }
}
