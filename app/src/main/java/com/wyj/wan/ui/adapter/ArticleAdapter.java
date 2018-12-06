package com.wyj.wan.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyj.wan.R;
import com.wyj.wan.model.ArticleEntity;
import com.wyj.wan.ui.activity.ArticleActivity;
import com.wyj.wan.utils.ClickBinder;

/**
 * @author wangyujie
 * @date 2018/12/4.11:35
 * @describe
 */
public class ArticleAdapter extends BaseQuickAdapter<ArticleEntity.DatasBean, BaseViewHolder> {

    private boolean mIsCollect;

    public ArticleAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleEntity.DatasBean item) {
        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_nice_date, item.getNiceDate());
        helper.setText(R.id.tv_title, item.getTitle());
        helper.getView(R.id.tv_super_chapter_name).setVisibility(TextUtils.isEmpty(item.getSuperChapterName()) ? View.GONE : View.VISIBLE);
        helper.setText(R.id.tv_super_chapter_name, item.getSuperChapterName() + "/");
        helper.setText(R.id.tv_chapter_name, item.getChapterName());

        if (item.getNiceDate().contains("分钟")
                || item.getNiceDate().contains("小时")) {
            helper.getView(R.id.tv_tag_gold).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.tv_tag_gold).setVisibility(View.GONE);
        }
        if (item.getTags() != null && item.getTags().size() > 0) {
            helper.getView(R.id.tv_tag_orangered).setVisibility(View.VISIBLE);
            for (ArticleEntity.DatasBean.TagsBean tagsBean : item.getTags()) {
                helper.setText(R.id.tv_tag_orangered, tagsBean.getName());
            }
        } else {
            helper.getView(R.id.tv_tag_orangered).setVisibility(View.VISIBLE);
        }
        ClickBinder.bind(helper.itemView, v -> ArticleActivity.startActivity(mContext, item.getLink(), item.getTitle()));
        helper.getView(R.id.iv_collect).setSelected(item.isCollect() || mIsCollect);
        helper.addOnClickListener(R.id.ll_chapter_name);
        helper.addOnClickListener(R.id.iv_collect);

    }


    /**
     * 在收藏页面设置
     *
     * @param isCollect
     */
    public void setCollect(boolean isCollect) {
        mIsCollect = isCollect;
        notifyDataSetChanged();
    }

}
