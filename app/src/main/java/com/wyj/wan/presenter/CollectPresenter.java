package com.wyj.wan.presenter;

import com.wyj.wan.api.BaseHttpEntity;
import com.wyj.wan.api.BaseObserver;
import com.wyj.wan.api.RxSchedulers;
import com.wyj.wan.api.ServiceApi;
import com.wyj.wan.base.BaseFragmentPresenter;
import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.ui.fragment.CollectFragment;

import javax.inject.Inject;

/**
 * @author wangyujie
 * @date 2018/12/5.16:04
 * @describe 添加描述
 */
public class CollectPresenter extends BaseFragmentPresenter<CollectFragment> {

    private final ServiceApi mApi;

    @Inject
    public CollectPresenter(ServiceApi api) {
        mApi = api;
    }

    public void getCollectList(int mPag) {
        mApi.getCollectList(mPag)
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity<ArticleEntity>>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity<ArticleEntity> response) {
                        mFragment.collectListonSuccess(response.getData());
                    }

                    @Override
                    protected void _onError(String message) {
                        mFragment.onLoadFinish(true, false);
                    }
                });
    }

    public void cancelCollectArticle(int id, int originId, int position) {
        mApi.cancelCollectArticle(id, originId)
                .compose(RxSchedulers.flowableToMain())
                .compose(bindUntilDestroy())
                .subscribe(new BaseObserver<BaseHttpEntity>() {
                    @Override
                    protected void onSuccess(BaseHttpEntity response) {
                        mFragment.showCancelCollectData(position);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });

    }
}
