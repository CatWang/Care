package com.orangex.care.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orangex.care.R;

import butterknife.BindView;

/**
 * Created by orangex on 2017/4/9.
 */

public class NearbyFragment extends BaseFragment {
    @BindView(R.id.rcv_nearby)
    RecyclerView nearbyRcV;
    
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        nearbyRcV.setLayoutManager(new LinearLayoutManager(mActivity));
        
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_nearby;
    }
}
