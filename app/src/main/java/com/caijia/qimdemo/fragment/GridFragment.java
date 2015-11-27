package com.caijia.qimdemo.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by cai.jia on 2015/11/27.
 */
public abstract class GridFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mGridView;

    private ListAdapter mAdapter;

    private AdapterView.OnItemClickListener mOnItemClickLister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null || mAdapter == null) {
            return;
        }
        mGridView = (GridView) view.findViewById(getGridViewId());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
    }

    public GridView getGridView(){
        return mGridView;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickLister != null) {
            mOnItemClickLister.onItemClick(parent, view, position, id);
        }
    }

    public void setOnItemClickLister(AdapterView.OnItemClickListener clickLister) {
        mOnItemClickLister = clickLister;
    }

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract
    @IdRes
    int getGridViewId();
}
