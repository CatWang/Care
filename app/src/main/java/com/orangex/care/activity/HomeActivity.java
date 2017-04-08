package com.orangex.care.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
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
import com.orangex.care.adapter.MessageCardsRcvAdapter;
import com.orangex.care.common.Config;
import com.orangex.care.model.CareUser;
import com.orangex.care.model.Contact;
import com.orangex.care.model.MessageCard;
import com.orangex.care.service.CareService;
import com.orangex.care.utils.ObjectUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class HomeActivity extends AppCompatActivity implements CareService.ICareService {
    private static final String TAG = "HomeActivity";
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
    
    @BindView(R.id.rcv_message_cards)
    RecyclerView mRecyclerView;
    
    @BindView(R.id.fab_sos)
    FloatingActionButton mFabSos;
    @BindView(R.id.fab_nearby)
    FloatingActionButton mFabNearby;
    @BindView(R.id.fab_guard)
    FloatingActionButton mFabGuard;
    @BindView(R.id.fab_setting)
    FloatingActionButton mFabSetting;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    
    private List<MessageCard> mCardDatas = new ArrayList<>();
    private MessageCard mSosCard = new MessageCard();
    private MessageCard mNearbyCard = new MessageCard();
    private MessageCard mGuardCard = new MessageCard();
    private MessageCard mSettingCard = new MessageCard();
    
    private MessageCardsRcvAdapter mAdapter;
    private BDLocation mCurrentLocation;
    
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
        
        
        mFabSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCard temp = mCardDatas.remove(mCardDatas.indexOf(mSosCard));
                mCardDatas.add(0, temp);
                mAdapter.notifyDataSetChanged();
            }
        });
        mFabNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCard temp = mCardDatas.remove(mCardDatas.indexOf(mNearbyCard));
                mCardDatas.add(0, temp);
                mAdapter.notifyDataSetChanged();
        
            }
        });
        mFabGuard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCard temp = mCardDatas.remove(mCardDatas.indexOf(mGuardCard));
                mCardDatas.add(0, temp);
                mAdapter.notifyDataSetChanged();
            
            }
        });
        mFabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCard temp = mCardDatas.remove(mCardDatas.indexOf(mSettingCard));
                mCardDatas.add(0, temp);
                mAdapter.notifyDataSetChanged();
            }
        });
        
        initMap();
        initSwipeCards();

        
        
    }
    
    private void initSwipeCards() {
        mSosCard.setContentText(getString(R.string.description_card_sos));
        mSosCard.setActionBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSosMessage();
            }
        });
        mCardDatas.add(mSosCard);
        mNearbyCard.setContentText(getString(R.string.description_card_nearby));
        mNearbyCard.setActionBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNearbyInfo();
            }
        });
        mCardDatas.add(mNearbyCard);
        mGuardCard.setContentText(getString(R.string.description_card_guard));
        mCardDatas.add(mGuardCard);


//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        


                
                
        mAdapter = new MessageCardsRcvAdapter(this, mCardDatas);
        mRecyclerView.setAdapter(mAdapter);
        
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CardItemTouchHelperCallback());
        mRecyclerView.setLayoutManager(new SwipeCardsLayoutManager(mRecyclerView, itemTouchHelper));
        
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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
       ViewStub cardBack = (ViewStub) findViewById(R.id.cards_item_back_viewstub);
       cardBack.setLayoutResource(R.layout.message_cards_item_nearby_back);
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
                BmobSMS.requestSMS(contact.getPhoneNumber(), "Care提醒您!您的联系人" + currentUser.getMobilePhoneNumber() + "正遭遇紧急情况。此时他（她）正位于『" + mTvLocation.getText().toString() + "』,请您留意!", sendTime, new QueryListener<Integer>() {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvLocation.setText(location.getAddress().province + location.getAddress().city + location.getAddress().district + location.getAddress().street + " " + location.getLocationDescribe());
            }
        });
    }
    
    
    private class SwipeCardsLayoutManager extends RecyclerView.LayoutManager {
        private RecyclerView mRecyclerView;
        private ItemTouchHelper mItemTouchHelper;
        
        
        private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mItemTouchHelper.startSwipe(childViewHolder);
                }
                return false;
            }
            
        };
        
        SwipeCardsLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
            mRecyclerView = ObjectUtil.checkIsNull(recyclerView);
            mItemTouchHelper = ObjectUtil.checkIsNull(itemTouchHelper);
        }
        
        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//            super.onLayoutChildren(recycler, state);
            detachAndScrapAttachedViews(recycler);
            int visibleMaxCount = getItemCount();

            for (int index = visibleMaxCount - 1; index >= 0; index--) {
                View view = recycler.getViewForPosition(index);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int decWidth = getDecoratedMeasuredWidth(view);
                int decHeight = getDecoratedMeasuredHeight(view);
                int widthSpace = getWidth() - decWidth;
                int heightSpace = getHeight() - decHeight;
                
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2, widthSpace / 2 + decWidth, heightSpace / 2 + decHeight);
                
                if (index > 0) {
                    view.setScaleX(1 - index * Config.CARD_SCALE_GAP);
                    view.setScaleY(1 - index * Config.CARD_SCALE_GAP);
                    view.setTranslationY(index * decHeight / Config.CARD_TRANSLATION_Y_GAP);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
        
    }
    
    
    private class CardItemTouchHelperCallback extends ItemTouchHelper.Callback {
        

    
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
        }
    
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }
    
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            viewHolder.itemView.setOnTouchListener(null);
    
            MessageCard temp = mCardDatas.get(0);
            mCardDatas.remove(0);
            mCardDatas.add(temp);
            
            mAdapter.notifyItemRemoved(0);

    
        }
    
        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }
    
        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    
    
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float ratio = dX / (getSwipeThreshold(viewHolder) * itemView.getWidth());
                if (ratio > 1) {
                    ratio = 1;
                }
                if (ratio < -1) {
                    ratio = -1;
                }
    
                itemView.setRotation(ratio * Config.CARD_ROTATE_DEGREE_GAP);
                int childCount = recyclerView.getChildCount();
                int temp;
                if (childCount > Config.CARD_MAX_AMOUNT_SHOW) {
                    temp = 1;
                } else {
                    temp = 0;
                }
    
    
                for (int position = temp; position < childCount - 1; position++) {
                    View view = recyclerView.getChildAt(position);
                    int factor = childCount - position - 1;
                    view.setScaleX(1 - factor * Config.CARD_SCALE_GAP + Math.abs(ratio) * Config.CARD_SCALE_GAP);
                    view.setScaleY(1 - factor * Config.CARD_SCALE_GAP + Math.abs(ratio) * Config.CARD_SCALE_GAP);
                    view.setTranslationY((factor - Math.abs(ratio)) * itemView.getHeight() / Config.CARD_TRANSLATION_Y_GAP);
                }
                
            }
    
        }
    
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setRotation(0f);
        }
    
    
    }

    
}
