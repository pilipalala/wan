package com.wyj.wan.api;

import com.wyj.wan.R;
import com.wyj.wan.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * @author wangyujie
 * @date 2018/11/29.17:16
 * @describe RxJava订阅者Subscriber封装扩展
 */
public abstract class BaseObserver<T> extends ResourceSubscriber<T> {
    @Override
    public void onNext(T t) {
        if (t instanceof BaseHttpEntity) {
            BaseHttpEntity result = (BaseHttpEntity) t;
            if (result.getErrorCode() != ApiCode.CODE_SUCCESS) {
                ApiException exception;
                if (ApiCode.sApiCodeMap.get(result.getErrorCode()) != null) {
                    exception = new ApiException(result.getErrorCode(), result.getErrorMsg());
                    exception.setNeedTip(ApiCode.sApiCodeMap.get(result.getErrorCode()).isNeedTip);
                    if (exception.isNeedTip()) {
                        exception.setTipStrId(ApiCode.sApiCodeMap.get(result.getErrorCode()).tipStringId);
                    }
                } else {
                    exception = new ApiException(result.getErrorCode(), result.getErrorMsg());
                    exception.setNeedTip(false);
                }
                onError(exception);
            } else {
                onSuccess(t);
            }
        } else if (t instanceof HashMap){
            onSuccess(t);
        }
    }

    /**
     * 请求成功回调
     *
     * @param response 最终响应结果
     */
    protected abstract void onSuccess(T response);

    /**
     * 请求失败回调
     *
     * @param message 错误信息
     */
    protected abstract void _onError(String message);

    @Override
    public void onError(Throwable e) {
        onFinish();
        if (e instanceof HttpException) {
            ToastUtil.show(R.string.networkErr500);
        } else if (e instanceof ConnectException) {
            ToastUtil.show((R.string.networkErr));//网络连接异常
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.show((R.string.networkErr));//网络连接超时
        } else if (e instanceof TimeoutException) {
            ToastUtil.show((R.string.networkErr));//网络连接超时
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (apiException.isNeedTip()) {
                _onError(e.getMessage());
            }
        } else {
            _onError("请求失败，请稍后再试..." + e.getMessage());
        }
    }

    protected void onFinish() {

    }

    @Override
    public void onComplete() {
        onFinish();
    }
}
