package com.orangex.care.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.orangex.care.R;

import java.util.List;

/**
 * Created by orangex on 2017/2/11.
 */

public class ContactsRecyclerViewAdapter extends BaseRecyclerViewAdapter{
    public ContactsRecyclerViewAdapter(Context context, @Nullable List list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(parent.getContext(), parent);
    }


    public class ContactViewHolder extends BaseViewHolder {

        public ContactViewHolder(Context context,ViewGroup root) {
            super(context, root, R.layout.contact_list_item);
        }

        @Override
        public void setData(Object o) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }
}
