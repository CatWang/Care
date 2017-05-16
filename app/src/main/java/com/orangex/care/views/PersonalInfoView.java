package com.orangex.care.views;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orangex.care.R;
import com.orangex.care.utils.PreferencesManager;

/**
 * Created by CatWong on 17/5/16.
 */

public class PersonalInfoView extends EditTextPreference{

    private View mPersonalInfoView;
    private TextView settingPhone;
    private TextView settingName;
    private ImageView settingAvatar;

    public PersonalInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PersonalInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonalInfoView(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        mPersonalInfoView = LayoutInflater.from(getContext()).inflate(
                R.layout.person_info, parent, false
        );
        initView(mPersonalInfoView);

        return super.onCreateView(parent);
    }

    private void initView(View view) {
        return;
    }
}
