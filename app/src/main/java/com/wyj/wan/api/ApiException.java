package com.wyj.wan.api;

/**
 * @author wangyujie
 * @date 2018/9/19.15:49
 * @describe 添加描述
 */
public class ApiException extends RuntimeException {

    private int code;
    private String msg;
    private boolean isNeedTip;
    private int tipStrId;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isNeedTip() {
        return isNeedTip;
    }

    public void setNeedTip(boolean needTip) {
        isNeedTip = needTip;
    }

    public int getTipStrId() {
        return tipStrId;
    }

    public void setTipStrId(int tipStrId) {
        this.tipStrId = tipStrId;
    }
}
