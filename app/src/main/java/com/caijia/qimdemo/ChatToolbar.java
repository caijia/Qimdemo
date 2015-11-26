package com.caijia.qimdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cai.jia on 2015/11/26.
 */
public class ChatToolbar extends RelativeLayout implements MeasureKeyboardLayout.OnSoftKeyboardStateChangeListener {

    @Bind(R.id.tv_voice_keyboard)
    TextView mTvVoiceKeyboard;

    @Bind(R.id.keyboard_layout)
    RelativeLayout mKbLayout;

    @Bind(R.id.view_edit_bg)
    View mEditBg;

    @Bind(R.id.et_chat_message)
    EditText mEtMessage;

    @Bind(R.id.tv_chat_emoticon)
    TextView mTvEmoticon;

    @Bind(R.id.btn_voice)
    Button mBtnVoice;

    @Bind(R.id.btn_chat_send)
    TextView mSend;

    @Bind(R.id.bottom_layout)
    FrameLayout mBottomLayout;

    /**
     * 打开软键盘
     */
    public static final int OPEN_SOFT_KEYBOARD = 0x1;

    /**
     * 打开表情列表
     */
    public static final int OPEN_EMOTICON = 0x2;

    /**
     * 打开更多功能列表
     */
    public static final int OPEN_MORE_FUNCTION = 0x3;

    /**
     * 打开语音
     */
    public static final int OPEN_VOICE = 0x4;

    @IntDef({OPEN_SOFT_KEYBOARD, OPEN_EMOTICON, OPEN_MORE_FUNCTION, OPEN_VOICE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UiStyle {

    }

    private MeasureKeyboardLayout mKeyboardLayout;

    public ChatToolbar(Context context) {
        super(context);
        init(context);
    }

    public ChatToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setKeyboardLayout(MeasureKeyboardLayout keyboardLayout) {
        mKeyboardLayout = keyboardLayout;
        mKeyboardLayout.setSoftKeyboardChangeListener(this);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_chat_tool_bar, this);
        ButterKnife.bind(this);
        logic();
    }

    private void logic() {

        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String message = s.toString();
                boolean isSend = !TextUtils.isEmpty(message) && message.length() > 0;
                mSend.setVisibility(isSend ? VISIBLE : GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.btn_chat_more_function)
    void moreFunction() {
        setStyle(OPEN_MORE_FUNCTION);
    }

    @OnClick(R.id.tv_chat_emoticon)
    void showEmoticon() {
        mTvEmoticon.setSelected(!mTvEmoticon.isSelected());
        setStyle(OPEN_EMOTICON);
    }

    @OnClick(R.id.tv_voice_keyboard)
    void switchVoiceAndKeyboard() {
        mTvVoiceKeyboard.setSelected(!mTvVoiceKeyboard.isSelected());
        setStyle(OPEN_VOICE);
    }

    @Override
    public void onSoftKeyboardStateChange(boolean open, int softKeyboardHeight) {
        if (open) {
            setStyle(OPEN_SOFT_KEYBOARD);
        }
    }

    private void setStyle(@UiStyle int type) {
        switch (type) {
            case OPEN_SOFT_KEYBOARD:
                setKeyboardStyle();
                mTvEmoticon.setSelected(false);
                mEditBg.setSelected(true);
                break;

            case OPEN_EMOTICON: {
                boolean popToolbar = isPopToolbar();
                setKeyboardStyle();
                mEditBg.setSelected(true);
                if (popToolbar) {
                    Utils.toggleSoftKeyboard(mEtMessage);
                }
                break;
            }

            case OPEN_MORE_FUNCTION: {
                boolean openSoftKb = mKeyboardLayout.isOpenSoftKb();
                boolean popToolbar = isPopToolbar();

                if (popToolbar && !openSoftKb) { //弹出,并且关闭了软键盘 -> 不弹出模式
                    setBottomLayoutHeight(0);

                } else {  //弹出模式
                    setKeyboardStyle();
                    Utils.closeSoftKeyboard(mEtMessage);
                }
                mEditBg.setSelected(false);
                mTvEmoticon.setSelected(false);

                break;
            }

            case OPEN_VOICE:
                boolean isVoiceType = mBtnVoice.isShown();

                setBottomLayoutHeight(isVoiceType ? mKeyboardLayout.getSoftKbHeight() : 0);
                mKbLayout.setVisibility(isVoiceType ? VISIBLE : GONE);
                mBtnVoice.setVisibility(isVoiceType ? GONE : VISIBLE);
                mBtnVoice.setText("按住 说话");

                if (isVoiceType) {
                    Utils.openSoftKeyboard(mEtMessage);
                }else{
                    Utils.closeSoftKeyboard(mEtMessage);
                }

                break;
        }
    }

    private void setKeyboardStyle() {
        mKbLayout.setVisibility(VISIBLE);
        mBtnVoice.setVisibility(GONE);
        setBottomLayoutHeight(mKeyboardLayout.getSoftKbHeight());
        mTvVoiceKeyboard.setSelected(false);
    }

    /**
     * 设置底部layout的高度
     *
     * @param height
     */
    private void setBottomLayoutHeight(final int height) {
        this.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mBottomLayout.getLayoutParams();
                layoutParams.height = height;
                mBottomLayout.setLayoutParams(layoutParams);
            }
        });
    }

    /**
     * 是否弹出底部表情，更多功能
     *
     * @return
     */
    private boolean isPopToolbar() {
        return mBottomLayout.getHeight() != 0;
    }
}
