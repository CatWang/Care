package com.orangex.care.fragment;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.orangex.care.R;
import com.orangex.care.adapter.NearbyRcvAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by orangex on 2017/4/12.
 */

public class NearbyChildFragment extends BaseFragment {
    @BindView(R.id.rcv_nearby_child)
    RecyclerView mRecyclerView;
    List<PoiInfo> mPoiList = new ArrayList<>();
    private NearbyRcvAdapter mAdapter;
    
    @Override
    protected void initData() {
        PoiSearch poiSearch = PoiSearch.newInstance();
        
        
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                mPoiList.addAll(poiResult.getAllPoi());
                mAdapter.notifyDataSetChanged();
            }
            
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                
            }
            
            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
                
            }
        });
        BDLocation location = ((IBaseFragment) mActivity).getCurrentLocation();
        LatLng latLng = new LatLng((location.getLatitude()), location.getLongitude());
        poiSearch.searchNearby(new PoiNearbySearchOption().location(latLng).radius(10000).keyword(getArguments().getString(ARG_PARAM1)).pageNum(0).pageCapacity(10));
        
        
    }
    
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mAdapter = new NearbyRcvAdapter(mActivity, mPoiList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
            
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = 6;
                }
            }
        });
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_nearby_child;
    }
    
}
