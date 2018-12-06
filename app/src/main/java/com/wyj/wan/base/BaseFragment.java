package com.wyj.wan.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.wyj.wan.app.App;
import com.wyj.wan.di.component.DaggerFragmentComponent;
import com.wyj.wan.di.component.FragmentComponent;
import com.wyj.wan.utils.LogUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author wangyujie
 * @date 2018/11/30.11:14
 * @describe BaseFragment
 */
public abstract class BaseFragment<P extends BaseFragmentPresenter> extends SupportFragment implements LifecycleProvider<FragmentEvent> {
    BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected Context mContext;
    protected Activity mActivity;
    private Unbinder unbinder;
    @Inject
    protected P mPresenter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        mActivity = getActivity();
        mContext = getActivity();
        inject(getFragmentComponent());
        initPresenter();
        initData();
    }

    protected abstract int getContentViewID();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initCompleted();

    protected void setStatusBar() {

    }

    //当Fragment对用户可见时回调
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        LogUtil.e("onSupportVisible");
        setStatusBar();
    }

    private void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected void inject(FragmentComponent fragmentComponent) {

    }

    public FragmentComponent getFragmentComponent() {
        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .appComponent(App.getContext().getAppComponent()).build();
        return fragmentComponent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewID() != 0) {
            return inflater.inflate(getContentViewID(), container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        LogUtil.e("onLazyInitView");
        initView();
        initListener();
        initCompleted();
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(FragmentEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(FragmentEvent.DETACH);
    }

    @NonNull
    @Override
    public Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }
}
