package com.wyj.wan.api;

import com.google.gson.annotations.SerializedName;

public class BaseHttpEntity<T> {
    /**
     * data : ...
     * errorCode : 0
     * errorMsg :
     */
    @SerializedName("data")
    private T mData;
    @SerializedName("errorCode")
    private int mErrorCode;
    @SerializedName("errorMsg")
    private String mErrorMsg;

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        mErrorMsg = errorMsg;
    }
}
