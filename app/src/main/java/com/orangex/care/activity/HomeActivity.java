package com.orangex.care.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.orangex.care.R;
import com.orangex.care.fragment.BaseFragment;
import com.orangex.care.fragment.GuardFragment;
import com.orangex.care.fragment.NearbyFragment;
import com.orangex.care.fragment.SosFragment;
import com.orangex.care.model.CareUser;
import com.orangex.care.model.Contact;
import com.orangex.care.service.CareService;
import com.orangex.care.utils.ObjectUtil;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class HomeActivity extends AppCompatActivity implements CareService.ICareService, SosFragment.ISosFragment {
    private static final String TAG = "HomeActivity";
    private static final String FRAGMENT_TAG_SOS = "sos";
    private static final String FRAGMENT_TAG_NEARBY = "nearby";
    private static final String FRAGMENT_TAG_GUARD = "guard";
    private static final String FRAGMENT_TAG_SETTING = "setting";
    @BindView(R.id.mapView)
    MapView mMapView;

    
    private BaiduMap mMap;
    private LocationClient mLocationClient = null;
    
    private CareService mService = null;
    private boolean mBound;
    
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagneticSensor;
    private double mZAngle;
    
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    

    
    
    private BDLocation mCurrentLocation;
    
    
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_sos:
                    switchFragment(FRAGMENT_TAG_SOS);
                    break;
                case R.id.navigation_nearby:
                    switchFragment(FRAGMENT_TAG_NEARBY);
                    break;
                case R.id.navigation_guard:
                    switchFragment(FRAGMENT_TAG_GUARD);
                    break;
                case R.id.navigation_setting:
                    switchFragment(FRAGMENT_TAG_SETTING);
                    break;
                default:
                    break;
            }
            return true;
        }
        
        
    };
    private SosFragment mSosFragment;
    private NearbyFragment mNearbyFragment;
    private GuardFragment mGuardFragment;
    
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate: ");
        
        Intent intent = new Intent(this, CareService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                CareService.MyBinder binder = (CareService.MyBinder) service;
                mService = binder.getService();
                mService.setCallBack(HomeActivity.this);
                mBound = true;
            }
            
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                mBound = false;
            }
        }, Context.BIND_AUTO_CREATE);
    
    
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setDefaultFragment();
        initMap();
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.fragment_container);
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    
    
    }
    
    private void switchFragment(String tag) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFragment switchTo = null;
        switch (tag) {
            case FRAGMENT_TAG_SOS:
                if (mSosFragment == null) {
                    mSosFragment = SosFragment.newInstance(SosFragment.class, tag);
                }
                switchTo = mSosFragment;
                break;
            case FRAGMENT_TAG_NEARBY:
                if (mNearbyFragment == null) {
                    mNearbyFragment = NearbyFragment.newInstance(NearbyFragment.class, tag);
                }
                switchTo = mNearbyFragment;
                break;
            case FRAGMENT_TAG_GUARD:
                if (mGuardFragment == null) {
                    mGuardFragment = GuardFragment.newInstance(GuardFragment.class, tag);
                }
                switchTo = mGuardFragment;
                break;
            case FRAGMENT_TAG_SETTING:
                break;
            default:
                break;
        }
        transaction.replace(R.id.fragment_container, switchTo);
        transaction.show(switchTo);
        transaction.commit();
    }
    
    private void setDefaultFragment() {
        switchFragment(FRAGMENT_TAG_SOS);
        
    }
    
    
    private void showNearbyInfo() {
       PoiSearch poiSearch = PoiSearch.newInstance();
       poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
           @Override
           public void onGetPoiResult(PoiResult poiResult) {
               for (PoiInfo poiInfo : poiResult.getAllPoi()) {
                   Log.i(TAG, "onGetPoiResult: " + poiInfo.name+poiInfo.address + poiInfo.phoneNum);
               }
           }
    
           @Override
           public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        
           }
    
           @Override
           public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        
           }
       });
//       poiSearch.searchInCity((new PoiCitySearchOption().city("北京").keyword("公共厕所").pageNum(10)));
       LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
       poiSearch.searchNearby(new PoiNearbySearchOption().location(latLng).radius(10000).keyword("医院").pageNum(10));
//               .city("北京")
//               .keyword("ktv")
//               .pageNum(10));
//       poiSearch.searchNearby(new PoiNearbySearchOption())
//               .
//               .location()
//       PoiNearbySearchOption option = new PoiNearbySearchOption().location(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())).keyword("医院");
//
//       poiSearch.searchNearby(option);
            
       
    }
    
    private void sendSosMessage() {
        CareUser currentUser = ObjectUtil.checkIsNull(BmobUser.getCurrentUser(CareUser.class));
    
        if (currentUser.getContactList() == null) {
            Toast.makeText(this, "您还没有联系人，快去设置中添加吧", Toast.LENGTH_SHORT).show();
            
        } else {
            for (Contact contact : currentUser.getContactList()
                    ) {
                SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sendTime = null;
                BmobSMS.requestSMS(contact.getPhoneNumber(), "Care提醒您!您的联系人" + currentUser.getMobilePhoneNumber() + "正遭遇紧急情况。此时他（她）正位于『" + "』,请您留意!", sendTime, new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e != null) {
                    
                            Log.e(TAG, "SMSdone: " + integer);
                        } else {
                    
                        }
                    }
                });
            }
        }
        
    }
    
    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mapView);
        mMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mMap.setIndoorEnable(true);
        mMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b) {
                    // TODO: 2017/3/14 进入室内则状态栏通知等等
                }
            }
        });
        
        
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mMap.setMyLocationConfigeration(config);
        
        //简易方向指针
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        
        
    }
    
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];
        
        
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values.clone();
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = event.values.clone();
            }
            //旋转矩阵
            float[] r = new float[9];
            //方向传感器结果
            float[] values = new float[3];
            SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticValues);
            SensorManager.getOrientation(r, values);
            mZAngle = Math.toDegrees(values[0]);
            
        }
        
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        mMap.setMyLocationEnabled(false);
        
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        mMap.setMyLocationEnabled(true);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
        if (mService != null) {
            mService.onActivityStart();
        }
        
        
    }
    
    @Override
    public void showLocInMap(final BDLocation location) {
        mCurrentLocation = location;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        mMap.setMyLocationData(locData);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mMap.setMapStatus(mapStatusUpdate);
    
    
    }
    
    
    public BDLocation getCurrentLocation() {
        return mCurrentLocation;
    }

    
}
