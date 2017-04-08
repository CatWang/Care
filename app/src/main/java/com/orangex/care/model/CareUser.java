package com.orangex.care.model;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by orangex on 2017/2/17.
 */

public class CareUser extends BmobUser {
    private List<Contact> contactList;
    
    public List<Contact> getContactList() {
        return contactList;
    }
    
    public void setContactList(List<Contact> contactList) {
        contactList = contactList;
    }
}
