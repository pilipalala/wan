package com.wyj.wan.api;

import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.model.BannerEntity;
import com.wyj.wan.model.UserEntity;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author wangyujie
 * @date 2018/11/27.18:02
 * @describe
 */
public interface ServiceApi {


    //首页banner http://www.wanandroid.com/banner/json
    @GET("banner/json")
    Flowable<BaseHttpEntity<List<BannerEntity>>> getBannerData();

    //登录 http://www.wanandroid.com/user/login
    @POST("user/login")
    @FormUrlEncoded
    Flowable<BaseHttpEntity<UserEntity>> getLoginData(@Field("username") String username,
                                                      @Field("password") String password);

    //首页文章列表 http://www.wanandroid.com/article/list/0/json
    @GET("article/list/{num}/json")
    Flowable<BaseHttpEntity<ArticleEntity>> getArticleList(@Path("num") int num);

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     *
     * @param id article id
     * @return 收藏站内文章数据
     */
    @POST("lg/collect/{id}/json")
    Flowable<BaseHttpEntity> addCollectArticle(@Path("id") int id);

    /**
     * 文章列表  取消收藏
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     *
     * @param id article id
     */
    @POST("lg/uncollect_originId/{id}/json")
    Flowable<BaseHttpEntity> cancelCollectPagArticle(@Path("id") int id);


    /**
     * 我的收藏页面（该页面包含自己录入的内容） 取消收藏
     * http://www.wanandroid.com/lg/uncollect/2805/json
     *
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Flowable<BaseHttpEntity> cancelCollectArticle(@Path("id") int id, @Field("originId") int originId);

    /**
     * 收藏文章列表
     * http://www.wanandroid.com/lg/collect/list/0/json
     *
     * @param id article id
     */
    @GET("lg/collect/list/{id}/json")
    Flowable<BaseHttpEntity<ArticleEntity>> getCollectList(@Path("id") int id);

}
