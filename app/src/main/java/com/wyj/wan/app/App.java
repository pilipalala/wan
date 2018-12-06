package com.wyj.wan.app;

import android.app.Application;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.wyj.wan.BuildConfig;
import com.wyj.wan.di.component.AppComponent;
import com.wyj.wan.di.component.DaggerAppComponent;
import com.wyj.wan.di.mode.AppMode;

/**
 * @author wangyujie
 * @date 2018/11/27.18:19
 * @describe
 */
public class App extends Application {
    private static App mApp;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        inject();
        initLogger();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)   // 是否显示线程信息。默认为true
                .tag("WAN")   // 日志的全局标记。默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void inject() {
        appComponent = DaggerAppComponent.builder().appMode(new AppMode(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * 获取全局的 Context
     */
    public static App getContext() {
        return mApp;
    }

}
