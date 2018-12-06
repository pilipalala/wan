package com.wyj.wan.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.wyj.wan.app.App;
import com.wyj.wan.app.AppManager;
import com.wyj.wan.di.component.ActivityComponent;
import com.wyj.wan.di.component.AppComponent;
import com.wyj.wan.di.component.DaggerActivityComponent;
import com.wyj.wan.utils.StatusBarUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author wangyujie
 * @date 2018/11/27.17:11
 * @describe
 */
public abstract class BaseActivity<P extends BaseActivityPresenter> extends SupportActivity implements LifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    private Context mContext;
    @Inject
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        setContentView(getContentViewID());
        ButterKnife.bind(this);

        setStatusBar();
        inject(getActivityComponent());
        initPresenter();
        initData();
        initView(savedInstanceState);
        initListener();
        initCompleted();
    }

    private ActivityComponent getActivityComponent() {
        AppComponent appComponent = App.getContext().getAppComponent();
        ActivityComponent activityComponent = DaggerActivityComponent.builder().appComponent(appComponent).build();
        return activityComponent;
    }

    protected void inject(ActivityComponent activityComponent) {
        // dagger注入
    }

    protected abstract int getContentViewID();

    protected abstract void initData();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void initCompleted();

    private void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this, 0);
    }


    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        AppManager.getAppManager().finishActivity(this);
    }

    @NonNull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }
}
