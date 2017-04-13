package com.orangex.care.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.orangex.care.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by orangex on 2017/4/11.
 */

public class NearbyRcvAdapter extends BaseRecyclerViewAdapter {
    
    public NearbyRcvAdapter(Context context, @Nullable List list) {
        super(context, list);
    }
    
    
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NearbyItemHolder(parent.getContext(), parent);
    }
    
    
    public class NearbyItemHolder extends BaseViewHolder {
        
        
        @BindView(R.id.tv_poi_title)
        TextView poi_title;
        @BindView(R.id.tv_poi_location)
        TextView poi_location;
        
        public NearbyItemHolder(Context context, ViewGroup root) {
            
            super(context, root, R.layout.nearby_cards_item);
        }
        
        @Override
        public void setData(Object o) {
            PoiInfo poiInfo = (PoiInfo) o;
            poi_title.setText(poiInfo.name);
            poi_location.setText(poiInfo.address);
            
        }
    }
}

