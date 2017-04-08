package com.orangex.care.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orangex.care.R;
import com.orangex.care.base.BaseRecyclerViewAdapter;
import com.orangex.care.base.BaseViewHolder;
import com.orangex.care.model.MessageCard;

import java.util.List;

import butterknife.BindView;

/**
 * Created by orangex on 2017/2/11.
 */

public class MessageCardsRcvAdapter extends BaseRecyclerViewAdapter {
    public MessageCardsRcvAdapter(Context context, @Nullable List list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageCardsHolder(parent.getContext(), parent);
    }


    public class MessageCardsHolder extends BaseViewHolder {
    
        @BindView(R.id.tv_card_content)
        TextView contentTextView;
    
        @BindView(R.id.btn_card_action)
        Button actionBtn;
        public MessageCardsHolder(Context context, ViewGroup root) {
            
            super(context, root, R.layout.message_cards_item);
        }

        @Override
        public void setData(Object o) {
            MessageCard messageCard = (MessageCard) o;
            contentTextView.setText(messageCard.getContentText());
            actionBtn.setOnClickListener(messageCard.getActionBtnListener());
            
//            this.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(getAdapterPosition(), v);
//                }
//            });
            
        }
    }
}
