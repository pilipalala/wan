package com.wyj.wan.di.component;

import com.wyj.wan.ui.activity.LoginActivity;
import com.wyj.wan.ui.activity.MainActivity;
import com.wyj.wan.di.scope.PerActivity;

import dagger.Component;

/**
 * @author wangyujie
 * @date 2018/11/27.18:31
 * @describe
 */
@PerActivity
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);
}
