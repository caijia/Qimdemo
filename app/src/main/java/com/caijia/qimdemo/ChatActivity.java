package com.caijia.qimdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.keyboard_layout)
    MeasureKeyboardLayout mKeyboardLayout;

    @Bind(R.id.chat_tool_bar)
    ChatToolbar mChatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        mChatToolbar.setKeyboardLayout(mKeyboardLayout);

    }
}
