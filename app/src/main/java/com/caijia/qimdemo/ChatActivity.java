package com.caijia.qimdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.caijia.qimdemo.entities.Emoticon;
import com.caijia.qimdemo.fragment.EmoticonFragment;
import com.caijia.qimdemo.util.FragmentService;
import com.caijia.qimdemo.widget.ChatToolbar;
import com.caijia.qimdemo.widget.EmoticonEditText;
import com.caijia.qimdemo.widget.MeasureKeyboardLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.keyboard_layout)
    MeasureKeyboardLayout mKeyboardLayout;

    @Bind(R.id.chat_tool_bar)
    ChatToolbar mChatToolbar;

    private FragmentService mFragmentService;

    private EmoticonFragment mEmoticonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mFragmentService = FragmentService.newInstance();

        mChatToolbar.setKeyboardLayout(mKeyboardLayout);

        mEmoticonFragment = EmoticonFragment.newInstance();
        mChatToolbar.setOnActionListener(new ChatToolbar.OnActionListener() {
            @Override
            public void onSelectedEmoticon() {
                mFragmentService.addOrShow(getSupportFragmentManager(), mChatToolbar.getBottomLayout().getId(), mEmoticonFragment);
            }

            @Override
            public void onSelectedMore() {

            }
        });

        mEmoticonFragment.setOnItemClickLister(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emoticon emoticon = (Emoticon) parent.getAdapter().getItem(position);
                if (emoticon != null) {
                    int type = emoticon.getType();
                    EmoticonEditText editText = mChatToolbar.getEmoticonEditText();
                    switch (type) {

                        case Emoticon.NORMAL:
                            editText.setEmoticon(emoticon.getName(),emoticon.getDrawable());
                            break;

                        case Emoticon.DELETE:
                            editText.deleteEmoticon();
                            break;
                    }
                }
            }
        });
    }

}
