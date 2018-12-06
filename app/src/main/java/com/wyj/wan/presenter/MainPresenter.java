package com.wyj.wan.presenter;

import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.base.BaseActivityPresenter;
import com.wyj.wan.ui.activity.MainActivity;
import com.wyj.wan.utils.SPUtils;

import javax.inject.Inject;

import static com.wyj.wan.app.Constants.MY_SHARED_PREFERENCE;

/**
 * @author wangyujie
 * @date 2018/11/29.15:48
 * @describe 添加描述
 */
public class MainPresenter extends BaseActivityPresenter<MainActivity> {

    private ServiceApi mApi;

    @Inject
    public MainPresenter(ServiceApi api) {
        mApi = api;
    }
    public boolean getLoginStatus() {
        boolean loginStatus = SPUtils.getInstance(MY_SHARED_PREFERENCE).getBoolean("login", false);
        return loginStatus;
    }
}
