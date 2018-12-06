package com.wyj.wan.base;


import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author wangyujie
 * @date 2018/11/27.17:44
 * @describe 添加描述
 */
public abstract class BaseActivityPresenter<T extends BaseActivity> {

    protected T mActivity;

    public void attachView(T activity) {
        mActivity = activity;
    }

    public <E> LifecycleTransformer<E> bindUntilDestroy() {
        return mActivity.bindUntilEvent(ActivityEvent.DESTROY);
    }

    public void detachView() {
        if (mActivity != null) {
            mActivity = null;
        }
    }
}
