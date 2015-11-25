package com.caijia.qimdemo;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

public class ChatActivity extends AppCompatActivity {

    private View mFooterView;

    MeasureKeyboardLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFooterView = findViewById(R.id.view);

        ImageButton img = (ImageButton) findViewById(R.id.emoticon);


        layout = (MeasureKeyboardLayout) findViewById(R.id.keyboard_layout);
        layout.setSoftKeyboardChangeListener(new MeasureKeyboardLayout.OnSoftKeyboardStateChangeListener() {
            @Override
            public void onSoftKeyboardStateChange(boolean open, final int softKeyboardHeight) {
                System.out.println("open=" + open + "---height=" + softKeyboardHeight);
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        setFooterViewHeight(softKeyboardHeight);
                    }
                });
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.isOpen()) {
                    setFooterViewHeight(layout.getSoftKbHeight());
                }
                Utils.closeSoftKeyboard(ChatActivity.this);
            }
        });
    }

    private void setFooterViewHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mFooterView.getLayoutParams();
        layoutParams.height = height;
        mFooterView.setLayoutParams(layoutParams);
    }

}
