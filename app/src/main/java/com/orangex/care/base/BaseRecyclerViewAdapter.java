package com.orangex.care.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orangex on 2017/2/11.
 */

public abstract class BaseRecyclerViewAdapter<M, VH extends BaseViewHolder<M>> extends RecyclerView.Adapter<VH> {
    protected List<M> mDataList;
    protected Context mContext;
    protected onItemClickListener mOnItemClickListener;
    protected int mCurrentPos;

    public interface onItemClickListener {
        void onItemClick(int position, View view);
    }

    public BaseRecyclerViewAdapter(Context context, @Nullable List<M> list) {
        mContext = context;
        mDataList = list;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

    }


    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        holder.setData(mDataList.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position, holder.itemView);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        mCurrentPos = position;
        return position;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public List<M> getDataList() {
        return mDataList;
    }


}
