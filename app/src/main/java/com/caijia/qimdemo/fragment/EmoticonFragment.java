package com.caijia.qimdemo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.caijia.qimdemo.R;
import com.caijia.qimdemo.adapter.EmoticonItemAdapter;
import com.caijia.qimdemo.adapter.RestoreFragmentPagerAdapter;
import com.caijia.qimdemo.entities.Emoticon;
import com.caijia.qimdemo.util.ResourceUtil;
import com.caijia.qimdemo.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cai.jia on 2015/11/27.
 */
public class EmoticonFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ViewPager mPager;

    private CircleIndicator mIndicator;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mEmoticonList;

    public static final int ROW = 3;

    public static final int COLUMN = 7;

    public static EmoticonFragment newInstance() {
        return new EmoticonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoticon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }
        mPager = (ViewPager) view.findViewById(R.id.view_pager);
        mIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        processLogic();
    }

    private void processLogic() {
        String[] array = getResources().getStringArray(R.array.common_emoticon);
        if (array != null) {
            mEmoticonList = Arrays.asList(array);
        }

        int totalPage = getTotalPage();
        for (int i = 0; i < totalPage; i++) {
            List<Emoticon> list = getEmoticonList(totalPage, i);
            EmoticonPageFragment f = new EmoticonPageFragment();
            f.setOnItemClickLister(this);
            f.setAdapter(new EmoticonItemAdapter(getActivity(), list));
            mFragments.add(f);
        }

        mPager.setAdapter(new EmoticonPageAdapter(getChildFragmentManager()));
        mIndicator.setViewPager(mPager);
    }

    /**
     * 得到emoticon的总页数
     *
     * @return
     */
    private int getTotalPage() {
        final List<String> list = mEmoticonList;
        if (list == null || list.isEmpty()) {
            return 0;
        }

        int size = list.size();
        int perPageNum = ROW * COLUMN - 1; //一页的数量,最后一个为删除按钮
        return size / perPageNum + (size % perPageNum == 0 ? 0 : 1);
    }

    /**
     * 得到哪一页的所有emoticon的数量
     *
     * @param page
     * @return
     */
    private List<Emoticon> getEmoticonList(int totalPage, int page) {
        final List<String> list = mEmoticonList;

        if (list == null || list.isEmpty() || totalPage == 0) {
            return null;
        }

        int perPageNum = ROW * COLUMN - 1; //一页的数量,最后一个为删除按钮
        List<String> subEmoticon;
        if (page < totalPage - 1) {
            subEmoticon = list.subList(page * perPageNum, perPageNum * (page + 1));
        } else {
            subEmoticon = list.subList((totalPage - 1) * perPageNum, list.size());
        }

        List<Emoticon> emoticons = new ArrayList<>();
        if (!subEmoticon.isEmpty()) {
            for (String name : subEmoticon) {
                Drawable drawable = ResourceUtil.getDrawable(getActivity(), name);
                Emoticon emoticon = new Emoticon(drawable, name, Emoticon.NORMAL);
                emoticons.add(emoticon);
            }
        }

        //每一页最后加一个删除的drawable
        emoticons.add(new Emoticon(ResourceUtil.getDrawable(getActivity(), R.mipmap.icon_del), Emoticon.DELETE));
        return emoticons;
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

    private AdapterView.OnItemClickListener mOnItemClickLister;

    private class EmoticonPageAdapter extends RestoreFragmentPagerAdapter {

        public EmoticonPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
