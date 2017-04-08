package com.orangex.care.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.orangex.care.common.Config;

public class CareService extends Service implements BDLocationListener {
    private static final String TAG = "CareService";
    private final IBinder mIBinder = new MyBinder();
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
        }
    };
    
    private ICareService mCallBack = null;
    private LocationClient mLocationClient = null;
    private LocationClientOption mClientOption = null;
    
    public CareService() {
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        //监听电源键
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mBroadcastReceiver, filter);
    
        initLocation();
        mLocationClient.start();
        
    }
    
    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(this);
        
        mClientOption = new LocationClientOption();
        mClientOption.setScanSpan(Config.LOC_FREQUENCY_HIGH);
        mClientOption.setCoorType("bd09ll");
        mClientOption.setNeedDeviceDirect(true);
        mClientOption.setIsNeedAddress(true);
        mClientOption.setOpenGps(true);
        mClientOption.setIsNeedLocationDescribe(true);
        mClientOption.setIsNeedLocationPoiList(true);
        mClientOption.setIgnoreKillProcess(true);
        mLocationClient.setLocOption(mClientOption);
        
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mIBinder;
    }
    
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCallBack != null) {
            mCallBack.showLocInMap(bdLocation);
        }
        
    }
    
    @Override
    public void onConnectHotSpotMessage(String s, int i) {
        
    }
    
    public ICareService getCallBack() {
        return mCallBack;
    }
    
    public void setCallBack(ICareService callBack) {
        mCallBack = callBack;
    }
    
    public void onActivityStart() {
        
    }
    
    public class MyBinder extends Binder {
        public CareService getService() {
            return CareService.this;
        }
    }
    
    public interface ICareService {
        void showLocInMap(BDLocation bdLocation);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
