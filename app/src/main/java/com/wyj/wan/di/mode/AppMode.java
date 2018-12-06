package com.wyj.wan.di.mode;

import android.content.Context;

import com.wyj.wan.app.App;
import com.wyj.wan.di.qualifier.AppForContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author wangyujie
 * @date 2018/11/27.18:33
 * @describe 添加描述
 */
@Module
public class AppMode {

    private final App mApp;

    public AppMode(App context) {
        mApp = context;
    }

    @Provides
    @Singleton
    App providesApp() {
        return mApp;
    }

    @Provides
    @AppForContext
    @Singleton
    Context providesContext() {
        return mApp;
    }
}
