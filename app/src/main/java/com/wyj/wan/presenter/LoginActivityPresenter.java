package com.wyj.wan.presenter;

import com.wyj.wan.api.BaseHttpEntity;
import com.wyj.wan.api.BaseObserver;
import com.wyj.wan.api.RxSchedulers;
import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.base.BaseActivityPresenter;
import com.wyj.wan.model.UserEntity;
import com.wyj.wan.ui.activity.LoginActivity;

import javax.inject.Inject;

/**
 * @author wangyujie
 * @date 2018/12/3.16:07
 * @describe 添加描述
 */
public class LoginActivityPresenter extends BaseActivityPresenter<LoginActivity> {
    private ServiceApi mApi;

    @Inject
    public LoginActivityPresenter(ServiceApi api) {
        mApi = api;
    }

    public void login(String username, String password) {
        mApi.getLoginData(username,password)
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity<UserEntity>>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onSuccess(BaseHttpEntity<UserEntity> response) {
                        mActivity.loginSuccess(response.getData());
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
