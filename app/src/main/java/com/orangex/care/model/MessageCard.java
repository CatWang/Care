package com.orangex.care.model;

import android.view.View;

/**
 * Created by orangex on 2017/3/17.
 */

public class MessageCard {
    public static final String TYPE_SOS = "sos";
    
    private String mType;

    private String mContentText;
    private View.OnClickListener mActionBtnListener;
    
    public MessageCard() {
        
    }
    public String getType() {
        return mType;
    }
    
    
    public String getContentText() {
        return mContentText;
    }
    
    public void setContentText(String contentText) {
        mContentText = contentText;
    }
    
    
    public View.OnClickListener getActionBtnListener() {
        return mActionBtnListener;
    }
    
    public void setActionBtnListener(View.OnClickListener actionBtnListener) {
        mActionBtnListener = actionBtnListener;
    }
}
