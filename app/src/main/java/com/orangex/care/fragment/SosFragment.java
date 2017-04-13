package com.orangex.care.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.orangex.care.R;

import butterknife.BindView;


/**
 * Created by orangex on 2017/4/9.
 */

public class SosFragment extends BaseFragment {
    @BindView(R.id.tv_location)
    TextView locationTextView;
    
    @Override
    protected void initData() {
    
    }
    
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        BDLocation location = ((IBaseFragment) mActivity).getCurrentLocation();
        String loc;
        if (location == null) {
            loc = getString(R.string.error_location_loss);
        } else {
            loc = location.getAddress().province + location.getAddress().city + location.getAddress().district + location.getAddress().street + " " + location.getLocationDescribe();
        }
        locationTextView.setText(loc);
        
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sos;
    }
    
    
}
