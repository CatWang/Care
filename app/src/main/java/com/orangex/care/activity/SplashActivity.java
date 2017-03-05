package com.orangex.care.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.orangex.care.common.PreferenceKey;
import com.orangex.care.utils.PreferencesManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        final PreferencesManager preferencesManager = PreferencesManager.getInstance(this);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (preferencesManager.getBoolean(PreferenceKey.IS_FIRST_LAUNCH, true)) {
                    preferencesManager.setBoolean(PreferenceKey.IS_FIRST_LAUNCH, false);
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
            }
        }, 500);

    }


}
