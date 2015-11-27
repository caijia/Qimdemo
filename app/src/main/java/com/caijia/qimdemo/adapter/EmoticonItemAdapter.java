package com.caijia.qimdemo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.caijia.qimdemo.R;
import com.caijia.qimdemo.entities.Emoticon;
import com.caijia.qimdemo.util.ViewHolder;

import java.util.List;

/**
 * Created by cai.jia on 2015/8/27.
 */
public class EmoticonItemAdapter extends ArrayAdapter<Emoticon> {

    public EmoticonItemAdapter(Context context, List<Emoticon> emoticonList) {
        super(context, 0, emoticonList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_emoticon_item, parent, false);
        }
        ImageView vEmoticon = ViewHolder.get(convertView, R.id.iv_emoticon_item);
        Emoticon emoticon = getItem(position);
        if (emoticon != null) {
            vEmoticon.setImageDrawable(emoticon.getDrawable());
        }
        return convertView;
    }
}
