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

    public ResultEntity(int msgRes, String... msgPara) {
        this.msgRes = msgRes;
        this.msgPara = msgPara;
    }

    public ResultEntity(@ActionType int result, int msgRes, String... msgPara) {
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
}
