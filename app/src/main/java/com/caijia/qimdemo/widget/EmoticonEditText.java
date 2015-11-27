package com.caijia.qimdemo.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以添加和删除emoticon的EditText
 * Created by cai.jia on 2015/11/27.
 */
public class EmoticonEditText extends AppCompatEditText implements TextWatcher {

    private List<ImageSpan> mImageSpanList;

    public EmoticonEditText(Context context) {
        super(context);
        init(context);
    }

    public EmoticonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EmoticonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mImageSpanList = new ArrayList<>();
        this.addTextChangedListener(this);
    }

    public void setEmoticon(@NonNull String emoticonName, @NonNull Drawable drawable) {
        int start = getSelectionStart();
        int end = getSelectionEnd();

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);

        Editable message = getEditableText();
        message.replace(start, end, emoticonName);
        message.setSpan(imageSpan, start, start + emoticonName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void deleteEmoticon() {
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    /**
     * (删除) -> count
     *
     * @param s     表示之前的文本内容
     * @param start 光标开始的位置
     * @param count 删除时 ->  添加的字符个数,添加时为0
     * @param after 添加时 ->  添加的字符个数,删除时为0
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count > 0) {
            //表示删除,找到删除中是否包含imageSpan
            Editable message = getEditableText();
            ImageSpan[] spans = message.getSpans(start, start + count, ImageSpan.class);
            if (spans != null && spans.length > 0) {
                for (ImageSpan span : spans) {
                    mImageSpanList.add(span);
                }
            }
        }
    }

    /**
     * @param s 当前文本内容
     */
    @Override
    public void afterTextChanged(Editable s) {
        Editable message = getEditableText();
        if (!mImageSpanList.isEmpty()) {
            for (ImageSpan span : mImageSpanList) {
                int start = message.getSpanStart(span);
                int end = message.getSpanEnd(span);
                message.removeSpan(span);
                if (start != end) {
                    message.delete(start, end);
                }
            }
            mImageSpanList.clear();
        }
    }

    /**
     * (添加) -> count
     *
     * @param s      表示当前的文本内容
     * @param start  光标开始的位置
     * @param before 删除时 -> 删除的字符个数,添加时为0
     * @param count  添加时 -> 添加的字符个数,删除时为0
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

}
