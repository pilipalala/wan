package com.wyj.wan.presenter;

import android.support.annotation.NonNull;

import com.wyj.wan.api.BaseHttpEntity;
import com.wyj.wan.api.BaseObserver;
import com.wyj.wan.api.RxSchedulers;
import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.app.AppConfig;
import com.wyj.wan.app.Constants;
import com.wyj.wan.base.BaseFragmentPresenter;
import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.model.BannerEntity;
import com.wyj.wan.ui.fragment.HomeFragment;
import com.wyj.wan.utils.CommonUtils;
import com.wyj.wan.utils.LogUtil;
import com.wyj.wan.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * @author wangyujie
 * @date 2018/12/3.17:18
 * @describe 添加描述
 */
public class HomePresenter extends BaseFragmentPresenter<HomeFragment> {
    private ServiceApi mApi;

    @Inject
    public HomePresenter(ServiceApi api) {
        mApi = api;
    }

    public void getBanner() {
        mApi.getBannerData()
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity<List<BannerEntity>>>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity<List<BannerEntity>> response) {
                        LogUtil.d("onSuccess " + Thread.currentThread().getName());
                        mFragment.showBannerData(response.getData());
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }

    public void loadMainPagerData() {
        Flowable<BaseHttpEntity<List<BannerEntity>>> bannerData = mApi.getBannerData();
        Flowable<BaseHttpEntity<ArticleEntity>> articleList = mApi.getArticleList(AppConfig.FIRST_PAGE);
        Flowable.zip(bannerData, articleList, (bannerResponse, articleListResponse) -> {
            return HomePresenter.this.createResponseMap(bannerResponse, articleListResponse);
        }).compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<HashMap<String, Object>>() {
                    @Override
                    protected void onSuccess(HashMap<String, Object> map) {
                        BaseHttpEntity<List<BannerEntity>> bannerResponse = CommonUtils.cast(map.get(Constants.BANNER_DATA));
                        if (bannerResponse != null) {
                            mFragment.showBannerData(bannerResponse.getData());
                        }
                        BaseHttpEntity<ArticleEntity> feedArticleListResponse = CommonUtils.cast(map.get(Constants.ARTICLE_DATA));
                        if (feedArticleListResponse != null) {
                            mFragment.showArticleList(feedArticleListResponse.getData());
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtil.e("_onError:" + message);
                    }
                });
    }

    public void getArticleList(int index) {
        mApi.getArticleList(index)
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity<ArticleEntity>>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity<ArticleEntity> response) {
                        mFragment.showArticleList(response.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        mFragment.onLoadFinish(true, false);
                    }
                });
    }

    @NonNull
    private HashMap<String, Object> createResponseMap(BaseHttpEntity<List<BannerEntity>> bannerResponse,
                                                      BaseHttpEntity<ArticleEntity> articleListResponse) {
        HashMap<String, Object> map = new HashMap<>(2);
//        map.put(Constants.LOGIN_DATA, loginResponse);
        map.put(Constants.BANNER_DATA, bannerResponse);
        map.put(Constants.ARTICLE_DATA, articleListResponse);
        return map;
    }

    public void addCollectArticle(int position, ArticleEntity.DatasBean dataBean) {
        mApi.addCollectArticle(dataBean.getId())
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity response) {
                        LogUtil.e("onSuccess");
                        mFragment.collectArticleSuccess(position);
                    }

                    @Override
                    protected void _onError(String message) {
                        LogUtil.e("_onError" + message);
                        ToastUtil.show("收藏失败");
                    }
                });
    }

    public void cancelCollectArticle(int position, ArticleEntity.DatasBean dataBean) {
        mApi.cancelCollectPagArticle(dataBean.getId())
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity response) {
                        LogUtil.e("onSuccess");
                        mFragment.cancelCollectArticleSuccess(position);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtil.show("取消收藏失败");
                    }
                });
    }
}
