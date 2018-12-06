package com.wyj.wan.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wyj.wan.R;
import com.wyj.wan.app.AppConfig;
import com.wyj.wan.base.BaseFragment;
import com.wyj.wan.di.component.FragmentComponent;
import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.presenter.CollectPresenter;
import com.wyj.wan.ui.adapter.ArticleAdapter;
import com.wyj.wan.utils.LogUtil;
import com.wyj.wan.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author wangyujie
 * @date 2018/12/5.15:38
 * @describe 添加描述
 */
public class CollectFragment extends BaseFragment<CollectPresenter> {
    @BindView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private ArticleAdapter mAdapter;
    private int mPag = AppConfig.FIRST_PAGE;

    public CollectFragment() {
    }

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);//设置下拉圆圈上的颜色
        mRefreshLayout.setDistanceToTriggerSync(400);//设置手指在屏幕下拉多少距离会触发下拉刷新
        mRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);// 设定下拉圆圈的背景
        mRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);// 设置圆圈的大小

        mRvCollect.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ArticleAdapter(R.layout.adapter_article_item);
        mAdapter.setCollect(true);
        mRvCollect.setAdapter(mAdapter);


        View mEnptyView = LayoutInflater.from(mContext).inflate(R.layout.layout_empty, (ViewGroup) mRvCollect.getParent(), false);
        mAdapter.setEmptyView(mEnptyView);
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(() -> {
            mPag = AppConfig.FIRST_PAGE;
            mPresenter.getCollectList(mPag);
        });

        mAdapter.setOnLoadMoreListener(() -> {
            mPresenter.getCollectList(++mPag);
        }, mRvCollect);

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
                mPresenter.cancelCollectArticle(mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getOriginId(), position);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initCompleted() {
        mPresenter.getCollectList(mPag);
    }

    public void collectListonSuccess(ArticleEntity data) {
        if (mPag == AppConfig.FIRST_PAGE) {
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

    public void jumpToTheTop() {
        if (mRvCollect != null) {
            mRvCollect.smoothScrollToPosition(0);
        }
    }

    public void showCancelCollectData(int position) {
        mAdapter.remove(position);
        ToastUtil.show("取消收藏成功");
    }
}
