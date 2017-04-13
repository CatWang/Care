package com.orangex.care.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.orangex.care.R;
import com.orangex.care.adapter.NearbyChildFragmentAdapter;

import butterknife.BindView;

/**
 * Created by orangex on 2017/4/9.
 */

public class NearbyFragment extends BaseFragment {
    private static final String TAG = "NearbyFragment";
    
    @BindView(R.id.viewpager_nearby)
    ViewPager mViewPager;
    
    @BindView(R.id.tab_nearby)
    TabLayout mTabLayout;
    
    
    @Override
    protected void initData() {
    
    
        //
        //        PoiSearch poiSearch = PoiSearch.newInstance();
        //        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
        //            @Override
        //            public void onGetPoiResult(PoiResult poiResult) {
        //                for (PoiInfo poiInfo : poiResult.getAllPoi()) {
        //
        //                    Log.i(TAG, "onGetPoiResult: " + poiInfo.name + " " + poiInfo.address + " " + poiInfo.phoneNum + " " + poiInfo.type);
        //                }
        //                Log.i(TAG, "总页数: " + poiResult.getTotalPageNum() + " 总个数： " + poiResult.getTotalPoiNum() + " 当前页数： " + poiResult.getCurrentPageNum() + " 当前容量： " + poiResult.getCurrentPageCapacity());
        //
        //            }
        //
        //            @Override
        //            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        //
        //            }
        //
        //            @Override
        //            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        //
        //            }
        //        });
        //        //       poiSearch.searchInCity((new PoiCitySearchOption().city("北京").keyword("公共厕所").pageNum(10)));
        //        BDLocation location = ((IBaseFragment) mActivity).getCurrentLocation();
        //        LatLng latLng = new LatLng((location.getLatitude()), location.getLongitude());
        //        poiSearch.searchNearby(new PoiNearbySearchOption().location(latLng).radius(10000).keyword("医院"));
    }
    
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    
    
        mViewPager.setAdapter(new NearbyChildFragmentAdapter(getFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager, true);
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_nearby;
    }
    
    
}
