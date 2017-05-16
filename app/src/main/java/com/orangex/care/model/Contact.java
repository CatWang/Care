package com.orangex.care.model;

/**
 * Created by orangex on 2017/2/12.
 */

public class Contact {
    private int mImageResID;
    private String mPhoneNumber;
    private String mName;
    
    public Contact(int imageResID, String phoneNumber, String name) {
        mImageResID = imageResID;
        mPhoneNumber = phoneNumber;
        mName = name;
    }
    
    
    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    
    public String getName() {
        return mName;
    }
    
    public int getImageResID() {
        return mImageResID;
    }
    
    
}
