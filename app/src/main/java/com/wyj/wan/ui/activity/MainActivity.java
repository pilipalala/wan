package com.wyj.wan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyj.wan.R;
import com.wyj.wan.base.BaseActivity;
import com.wyj.wan.base.BaseFragment;
import com.wyj.wan.di.component.ActivityComponent;
import com.wyj.wan.presenter.MainPresenter;
import com.wyj.wan.ui.fragment.CollectFragment;
import com.wyj.wan.ui.fragment.HomeFragment;
import com.wyj.wan.ui.fragment.KnowledgeHierarchyFragment;
import com.wyj.wan.utils.ClickBinder;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> {
    private static final int HOME = 0, KNOWLEDGE = 1, COLLECT = 2;

    private static final String SAVED_TAB_INDEX = "savedTabIndex";

    private int mCurrentTab = HOME;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private BaseFragment[] mFragments;
    private TextView mName, mEmail;
    private ImageView mHead;

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;

                //设置左边菜单滑动后的占据屏幕大小
                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                //设置菜单透明度
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));

                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate();
                //设置右边菜单滑动后的占据屏幕大小
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);
            }
        };
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mFragments = new BaseFragment[3];
        if (savedInstanceState == null) {
            mFragments[HOME] = HomeFragment.newInstance();
            mFragments[KNOWLEDGE] = KnowledgeHierarchyFragment.newInstance();
            mFragments[COLLECT] = CollectFragment.newInstance();
//            mFragments[MINE] = WxArticleFragment.newInstance();
//            mFragments[MINE] = NavigationFragment.newInstance();
//            mFragments[MINE] = ProjectFragment.newInstance();
//            mFragments[MINE] = CollectFragment.newInstance();
//            mFragments[MINE] = SettingFragment.newInstance();
            loadMultipleRootFragment(R.id.container, HOME, mFragments);
        } else {
            mFragments[HOME] = findFragment(HomeFragment.class);
            mFragments[KNOWLEDGE] = findFragment(KnowledgeHierarchyFragment.class);
            mFragments[COLLECT] = findFragment(CollectFragment.class);
//            mFragments[MINE] = findFragment(WxArticleFragment.class);
//            mFragments[MINE] = findFragment(NavigationFragment.class);
//            mFragments[MINE] = findFragment(ProjectFragment.class);
//            mFragments[MINE] = findFragment(CollectFragment.class);
//            mFragments[MINE] = findFragment(SettingFragment.class);
            mCurrentTab = savedInstanceState.getInt(SAVED_TAB_INDEX, HOME);
        }
        mNavigationView.setCheckedItem(R.id.nav_wan);
    }

    private void initBottomNavigation() {
        mBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_item_wan:
                    if (mCurrentTab == HOME) {
                        // TODO: 2018/12/3 刷新 并 滚动到顶部
                        return true;
                    }
                    switchFragment(HOME);
                    break;
                case R.id.nav_item_mine:
                    switchFragment(KNOWLEDGE);
                    break;
            }
            return true;
        });
    }

    private void switchFragment(int position) {
        showHideFragment(mFragments[position], mFragments[mCurrentTab]);
        mCurrentTab = position;
    }

    private void initNavView() {
        mHead = mNavigationView.getHeaderView(0).findViewById(R.id.iv_head);
        mName = mNavigationView.getHeaderView(0).findViewById(R.id.tv_name);
        mEmail = mNavigationView.getHeaderView(0).findViewById(R.id.tv_email);

        if (mPresenter.getLoginStatus()) {
            showLoginView();
        } else {
            showLogoutView();
        }

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_wan:
                    mBottomNavigation.setSelectedItemId(R.id.nav_item_wan);
                    mBottomNavigation.setVisibility(View.VISIBLE);
                    mToolbar.setTitle("Wan");
                    break;
                case R.id.nav_collect:
                    switchFragment(COLLECT);
                    mBottomNavigation.setVisibility(View.GONE);
                    mToolbar.setTitle("收藏的文章");
                    break;
                case R.id.nav_login_out:
                    break;
                case R.id.nav_setting:
                    break;
                case R.id.nav_bus:
                    break;
                case R.id.nav_zhihu:
                    break;
                case R.id.nav_share:
                    break;
            }
            mNavigationView.setCheckedItem(item.getItemId());
            mDrawerLayout.closeDrawers();
            return false;
        });
    }

    private void showLogoutView() {
        mName.setText("Login");
        mName.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        if (mNavigationView == null) {
            return;
        }
        mNavigationView.getMenu().findItem(R.id.nav_login_out).setVisible(false);
    }

    private void showLoginView() {
        if (mNavigationView == null) {
            return;
        }
        mName.setText("登陆成功");
        mName.setOnClickListener(null);
        mNavigationView.getMenu().findItem(R.id.nav_login_out).setVisible(true);
    }

    @Override
    protected void initListener() {
        initNavView();
        initBottomNavigation();
        ClickBinder.bind(mFab, v -> jumpToTheTop());
    }

    private void jumpToTheTop() {
        switch (mCurrentTab) {
            case HOME:
                findFragment(HomeFragment.class).jumpToTheTop();
                break;
            case KNOWLEDGE:
                findFragment(KnowledgeHierarchyFragment.class).jumpToTheTop();
                break;
            case COLLECT:
                findFragment(CollectFragment.class).jumpToTheTop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initCompleted() {
    }

}