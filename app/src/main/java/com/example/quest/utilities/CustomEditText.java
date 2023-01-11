package com.example.quest.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.google.android.material.textfield.TextInputEditText;

public class CustomEditText extends TextInputEditText {

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            this.findFocus().clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public CustomEditText(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

    }
}
