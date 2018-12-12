package com.hardkernel.voodik.odroidutility;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

public class CustomEditTextPreference extends EditTextPreference {
    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomEditTextPreference(Context context) {
        super(context);
    }
}