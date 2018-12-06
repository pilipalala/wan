package com.wyj.wan.base;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * @author wangyujie
 * @date 2018/11/30.18:39
 * @describe 添加描述
 */
public abstract class BaseFragmentPresenter<T extends BaseFragment> {

    protected T mFragment;

    public void attachView(T fragment) {
        mFragment = fragment;
    }

    public <T> LifecycleTransformer<T> bindUntilDestroy() {
        return mFragment.bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }

    public void detachView() {
        if (mFragment != null) {
            mFragment = null;
        }
    }
}
