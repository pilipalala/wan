package com.wyj.wan.di.mode;


import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wyj.wan.BuildConfig;
import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.app.App;
import com.wyj.wan.app.AppConfig;
import com.wyj.wan.utils.NetworkUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wangyujie
 * @date 2018/11/27.17:59
 * @describe
 */
@Module
public class ApiMode {
    @Provides
    @Singleton
    ServiceApi provideServiceApi(Retrofit retrofit) {
        return retrofit.create(ServiceApi.class);
    }

    @Provides
    @Singleton
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConfig.Api)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    OkHttpClient getOkHttpClient(Cache cache) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(authTokenInterceptor())
                .addInterceptor(getLogInterceptor()) // 添加日志拦截器
                .addInterceptor(buildCacheInterceptor())
                .cache(cache) // 设置缓存文件
                .retryOnConnectionFailure(true) // 自动重连
                .connectTimeout(15, TimeUnit.SECONDS) // 15秒连接超时
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                //cookie认证
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getContext())))
//                .cookieJar(new CookieJarImpl(new PersistentCookieStore(App.getContext())))
                .build();
        return okHttpClient;
    }

    /**
     * 获取缓存对象
     *
     * @return Cache
     */
    @Provides
    @Singleton
    public Cache getCache(App application) {
        // 获取缓存目标,SD卡
        File cacheFile = new File(application.getCacheDir(), "networkCache");
        // 创建缓存对象,最大缓存50m
        return new Cache(cacheFile, 1024 * 1024 * 50);
    }

    @NonNull
    private HttpLoggingInterceptor getLogInterceptor() {
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w("HttpLog", message);
            }
        });
        //日志显示级别
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return loggingInterceptor;
    }

    /**
     * 构建缓存拦截器
     *
     * @return Interceptor
     */
    private Interceptor buildCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            // 无网络连接时请求从缓存中读取
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            // 响应内容处理
            // 在线时缓存5分钟
            // 离线时缓存4周
            Response response = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                int maxAge = 300;
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        };
    }
}
