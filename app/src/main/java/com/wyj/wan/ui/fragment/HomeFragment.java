package com.wyj.wan.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wyj.wan.R;
import com.wyj.wan.app.AppConfig;
import com.wyj.wan.base.BaseFragment;
import com.wyj.wan.di.component.FragmentComponent;
import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.model.BannerEntity;
import com.wyj.wan.presenter.HomePresenter;
import com.wyj.wan.ui.adapter.ArticleAdapter;
import com.wyj.wan.utils.LogUtil;
import com.wyj.wan.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment<HomePresenter> {
    Banner mBanner;
    View mBannerView;
    @BindView(R.id.rv_article)
    RecyclerView mRvArticle;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private int mPage = AppConfig.FIRST_PAGE;
    private ArrayList<String> mBannerTitleList, mBannerUrlList;
    private ArticleAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mRvArticle.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ArticleAdapter(R.layout.adapter_article_item);
        mRvArticle.setAdapter(mAdapter);

        initHeaderView();


        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);//设置下拉圆圈上的颜色
        mRefreshLayout.setDistanceToTriggerSync(400);//设置手指在屏幕下拉多少距离会触发下拉刷新
        mRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);// 设定下拉圆圈的背景
        mRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);// 设置圆圈的大小
    }

    private void initHeaderView() {
        mBannerView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_header, (ViewGroup) mRvArticle.getParent(), false);
        mBanner = ButterKnife.findById(mBannerView, R.id.banner_view);
        mAdapter.addHeaderView(mBanner);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.setEnableLoadMore(false);
            mPresenter.getArticleList(AppConfig.FIRST_PAGE);
        });
        mAdapter.setOnLoadMoreListener(() -> {
            mRefreshLayout.setEnabled(false);
            mPresenter.getArticleList(mPage + 1);
        }, mRvArticle);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                clickChildEvent(view, position);
            }
        });
    }

    private void clickChildEvent(View view, int position) {
        switch (view.getId()) {
            case R.id.ll_chapter_name:
                LogUtil.d("onChapterClick");
                break;
            case R.id.iv_collect:
                LogUtil.d("onCollectClick" + position);
//                if (!mPresenter.getLoginStatus()) {
//                    startActivity(new Intent(_mActivity, LoginActivity.class));
//                    CommonUtils.showMessage(_mActivity, getString(R.string.login_tint));
//                    return;
//                }
                if (mAdapter.getData().get(position).isCollect()) {
                    mPresenter.cancelCollectArticle(position, mAdapter.getData().get(position));
                } else {
                    mPresenter.addCollectArticle(position, mAdapter.getData().get(position));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initCompleted() {
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEnableLoadMore(false);
        mPresenter.loadMainPagerData();
    }


    public void showBannerData(List<BannerEntity> data) {
        mBannerTitleList = new ArrayList<>();
        List<String> bannerImageList = new ArrayList<>();
        mBannerUrlList = new ArrayList<>();
        for (BannerEntity bannerEntity : data) {
            mBannerTitleList.add(bannerEntity.getTitle());
            bannerImageList.add(bannerEntity.getImagePath());
            mBannerUrlList.add(bannerEntity.getUrl());
        }
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerImageList);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(mBannerTitleList);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(data.size() * 400);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    public void showArticleList(ArticleEntity data) {
        mPage = data.getCurPage() - 1;
        if (mPage == AppConfig.FIRST_PAGE) {
            mAdapter.setNewData(data.getDatas());
        } else {
            mAdapter.addData(data.getDatas());
        }
        onLoadFinish(false, data.getDatas().size() != AppConfig.DEFAULT_PAGE_SIZE);
    }

    public void onLoadFinish(boolean withErr, boolean isNoMore) {
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        if (withErr) {
            mAdapter.loadMoreFail();
        } else {
            if (isNoMore) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    public void collectArticleSuccess(int position) {
        if (mAdapter != null && mAdapter.getData().size() > position) {
            mAdapter.getData().get(position).setCollect(true);
            mAdapter.setData(position, mAdapter.getData().get(position));
            ToastUtil.show("收藏成功");
        }
    }

    public void cancelCollectArticleSuccess(int position) {
        if (mAdapter != null && mAdapter.getData().size() > position) {
            mAdapter.getData().get(position).setCollect(false);
            mAdapter.setData(position, mAdapter.getData().get(position));
            ToastUtil.show("取消收藏成功");
        }
    }

    public void jumpToTheTop() {
        if (mRvArticle != null) {
            mRvArticle.smoothScrollToPosition(0);
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object o, ImageView imageView) {
            Glide.with(context).load(o).into(imageView);
        }
    }
}
