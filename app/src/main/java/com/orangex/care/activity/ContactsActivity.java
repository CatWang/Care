package com.orangex.care.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orangex.care.R;
import com.orangex.care.adapter.BaseRecyclerViewAdapter;
import com.orangex.care.adapter.BaseViewHolder;
import com.orangex.care.model.Contact;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsActivity extends AppCompatActivity {
    private List<Contact> mContacts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContacts.add(new Contact(R.drawable.img_avatar_1, "大喵", "15527156011"));
        mContacts.add(new Contact(R.drawable.img_avatar_2, "大大喵", "13248376082"));
        mContacts.add(new Contact(R.drawable.img_avatar_3, "大大大喵", "189836563422"));
        setContentView(R.layout.activity_contacts);
        getSupportActionBar().setTitle("亲密人管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_contacts);
        mRecyclerView.setAdapter(new BaseRecyclerViewAdapter(this, mContacts) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ContactItemHolder(parent.getContext(), parent);
            }
            
        });
        mRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑")
                .setView(R.layout.dlg_edit_contact)
                .setPositiveButton("确定", null)
                .setNegativeButton("删除联系人", null)
                .create();
    }
    
    private class ContactItemHolder extends BaseViewHolder {
        
        private CircleImageView mAvatarImageView;
        private TextView mNameTextView;
        private TextView mPhoneTextView;
        
        public ContactItemHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.contacts_item);
        }
        
        @Override
        public void setData(Object o) {
            Contact contact = (Contact) o;
            mAvatarImageView.setImageResource(contact.getImageResID());
            mNameTextView.setText(contact.getName());
            mPhoneTextView.setText(contact.getPhoneNumber());
        }
    }
}
