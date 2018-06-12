package com.demo.icarbox.blereceiver;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.method.NumberKeyListener;

import com.guiying.module.common.http.DataType;

public class NumberKeys extends NumberKeyListener {
    char[] numberChars={'0','1','2','3','4','5','6','7','8','9'};
    @NonNull
    @Override
    protected char[] getAcceptedChars() {
        return numberChars;
    }

    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_NUMBER;
    }
}
