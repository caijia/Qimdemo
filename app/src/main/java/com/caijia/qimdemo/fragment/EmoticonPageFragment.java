package com.caijia.qimdemo.fragment;

import com.caijia.qimdemo.R;

/**
 * Created by cai.jia on 2015/11/27.
 */
public class EmoticonPageFragment extends GridFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emoticon_page;
    }

    @Override
    protected int getGridViewId() {
        return R.id.grid_view;
    }
}
