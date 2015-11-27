package com.caijia.qimdemo.util;

import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 每个Activity都对应一个FragmentService来管理Fragment
 * 实现fragment的隐藏和显示
 * Created by cai.jia on 2015/7/29.
 */
public class FragmentService {

    /**
     * 当调用addOrShow方法时会先隐藏所有的Fragment
     */
    public static final int HIDE_ALL = 0x1;

    /**
     * 当调用addOrShow方法时会隐藏在 加入fragment位置后的所有的fragment
     * 以免挡住加入的fragment显示,当返回时,前面的fragment还在
     */
    public static final int HIDE_AFTER = 0x2;

    /**
     * 一个显示Fragment的ViewGroup对应多个Fragment
     */
    private SparseArray<LinkedList<Fragment>> mFragmentArray;

    private int mEnter;

    private int mExit;

    public static FragmentService newInstance() {
        return new FragmentService();
    }

    private FragmentService() {
        mFragmentArray = new SparseArray<>();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIDE_ALL, HIDE_AFTER})
    public @interface FragmentHideType {

    }

    private Map<Integer,Fragment> mCurrentFragmentMap = new HashMap<>();

    /**
     * 设置fragent的动画
     *
     * @param enter fragment进入动画
     * @param exit  fragment退出动画
     */
    public void setCustomAnimations(int enter, int exit) {
        this.mEnter = enter;
        this.mExit = exit;
    }

    public void addOrShow(FragmentManager manager, int containerViewId, Fragment f) {
        addOrShow(manager,containerViewId,f,null,HIDE_ALL);
    }

    public void addOrShow(FragmentManager manager, int containerViewId, Fragment f, String tag,
                          @FragmentHideType int hideType) {
        if (manager == null || f == null) {
            return;
        }
        switch (hideType) {
            case HIDE_ALL:
                hideAll(manager, containerViewId);
                break;

            case HIDE_AFTER:
                hideAfterFragment(manager, f, containerViewId);
                break;
        }
        if (isAdded(containerViewId, f, tag)) {
            show(manager, f);
        } else {
            add(manager, containerViewId, f, tag);
        }
        mCurrentFragmentMap.put(containerViewId, f);
    }

    private void removeOrHide() {

    }

    public Fragment getCurrentFragment(int containerViewId) {
        return mCurrentFragmentMap.get(containerViewId);
    }

    /**
     * 判断fragment是否在容器里面
     *
     * @param containerViewId 容器id
     * @param f               页面
     * @return
     */
    private boolean isAdded(int containerViewId, Fragment f, String tag) {
        if (f == null || mFragmentArray == null) {
            return false;
        }
        LinkedList<Fragment> fragments = mFragmentArray.get(containerViewId);
        if (fragments == null || fragments.size() == 0) {
            return false;
        }
        for (Fragment fragment : fragments) {
            if (!TextUtils.isEmpty(fragment.getTag())
                    && fragment.getTag().equals(uniqueTag(containerViewId, f, tag))) {
                return true;
            }
        }
        return false;
    }

    private void add(FragmentManager manager, int containerViewId, Fragment f, String tag) {
        FragmentTransaction transaction = getFragmentTransaction(manager);
        String uniqueTag = uniqueTag(containerViewId, f, tag);
        transaction.add(containerViewId, f, uniqueTag).commit();
        if (mFragmentArray != null) {
            LinkedList<Fragment> fragments = mFragmentArray.get(containerViewId);
            if (fragments == null) {
                fragments = new LinkedList<>();
                mFragmentArray.put(containerViewId, fragments);
            }
            fragments.add(f);
        }
    }

    public void remove(FragmentManager manager, int containerViewId, Fragment f) {
        FragmentTransaction transaction = getFragmentTransaction(manager);
        transaction.remove(f).commit();
        if (mFragmentArray != null) {
            LinkedList<Fragment> fragments = mFragmentArray.get(containerViewId);
            if (fragments != null && fragments.contains(f)) {
                fragments.remove(f);
            }
        }
    }

    private FragmentTransaction getFragmentTransaction(FragmentManager manager) {
        return manager.beginTransaction().setCustomAnimations(mEnter, mExit);
    }

    public void show(FragmentManager manager, Fragment f) {
        FragmentTransaction transaction = getFragmentTransaction(manager);
        transaction.show(f).commit();
    }

    /**
     * 隐藏f后面的所有fragment
     *
     * @param f
     */
    private void hideAfterFragment(FragmentManager manager, Fragment f, int containerViewId) {
        if (mFragmentArray != null) {
            LinkedList<Fragment> fragments = mFragmentArray.get(containerViewId);
            if (fragments != null) {
                int index = fragments.indexOf(f);
                if (index != -1) {
                    int size = fragments.size();
                    for (int i = index + 1; i < size; i++) {
                        hide(manager, fragments.get(i));
                    }
                }
            }
        }
    }

    public void hideAll(FragmentManager manager, int containerViewId) {
        if (mFragmentArray != null) {
            LinkedList<Fragment> fragments = mFragmentArray.get(containerViewId);
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    hide(manager, fragment);
                }
            }
        }
    }

    public void hide(FragmentManager manager, Fragment f) {
        FragmentTransaction transaction = getFragmentTransaction(manager);
        transaction.hide(f).commit();
    }

    /**
     * 每个容器里面对应的唯一tag
     *
     * @param containerViewId 容器id
     * @param f               页面
     * @param tag             传进来的tag
     * @return
     */
    private String uniqueTag(int containerViewId, Fragment f, String tag) {
        return containerViewId + f.getClass().getName() + tag;
    }
}
