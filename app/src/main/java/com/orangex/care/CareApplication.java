package com.orangex.care;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by orangex on 2017/3/8.
 */

public class CareApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
