package com.wyj.wan.ui.fragment;

import android.os.Bundle;

import com.wyj.wan.R;
import com.wyj.wan.base.BaseFragment;


public class KnowledgeHierarchyFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public KnowledgeHierarchyFragment() {
        // Required empty public constructor
    }

    public static KnowledgeHierarchyFragment newInstance() {
        KnowledgeHierarchyFragment fragment = new KnowledgeHierarchyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_knowledge_hierarchy;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initCompleted() {

    }

    public void jumpToTheTop() {

    }
}
