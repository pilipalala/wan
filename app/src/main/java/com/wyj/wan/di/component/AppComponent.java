package com.wyj.wan.di.component;

import android.content.Context;

import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.app.App;
import com.wyj.wan.di.mode.ApiMode;
import com.wyj.wan.di.mode.AppMode;
import com.wyj.wan.di.qualifier.AppForContext;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author wangyujie
 * @date 2018/11/27.18:34
 * @describe 添加描述
 */
@Singleton
@Component(modules = {AppMode.class, ApiMode.class})
public interface AppComponent {
    @AppForContext
    Context context();

    App application();

    ServiceApi serviceApi();
}
