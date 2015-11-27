package com.caijia.qimdemo.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by cai.jia on 2015/8/27.
 */
public class ResourceUtil {

    public static int getDrawableId(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    public static int getMipmapId(Context context, String mipmapName) {
        return context.getResources().getIdentifier(mipmapName, "mipmap", context.getPackageName());
    }

    public static int getViewId(Context context, String viewName) {
        return context.getResources().getIdentifier(viewName, "id", context.getPackageName());
    }

    public static int getLayoutId(Context context, String layoutName) {
        return context.getResources().getIdentifier(layoutName, "layout", context.getPackageName());
    }

    public static Drawable getDrawable(Context context,String drawableName){
        int drawableId = getMipmapId(context, drawableName);
        return getDrawable(context,drawableId);
    }

    public static Drawable getDrawable(Context context,int drawableId) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(drawableId, context.getTheme());
        }else {
            return context.getResources().getDrawable(drawableId);
        }
    }
}
