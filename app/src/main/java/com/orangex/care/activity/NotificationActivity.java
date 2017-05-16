package com.orangex.care.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.orangex.care.R;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("通知");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
    }
    
    
}
