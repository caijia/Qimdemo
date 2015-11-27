package com.caijia.qimdemo.entities;

import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cai.jia on 2015/11/27.
 */
public class Emoticon {

    public static final int NORMAL = 0x1;

    public static final int DELETE =0x2;

    private String name;

    private Drawable drawable;

    private int type;

    public Emoticon(Drawable drawable, String name, int type) {
        this.drawable = drawable;
        this.name = name;
        this.type = type;
    }

    public Emoticon(Drawable drawable, int type) {
        this.drawable = drawable;
        this.type = type;
    }

    @IntDef({NORMAL,DELETE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface EmoticonType{

    }

    public int getType() {
        return type;
    }

    public void setType(@EmoticonType int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
