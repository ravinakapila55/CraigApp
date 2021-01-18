package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helper.Helper2Go.ApiUtils.ListType;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.MessageData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter {

    private List<MessageData> dataSet;
    Context mContext;

    public ChatListAdapter(List<MessageData> dataSet, Context context) {
        this.dataSet = dataSet;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        Log.i("viewType", String.valueOf(viewType));
        boolean isCommentEnabled = false;
        if (viewType == ListType.SENDER_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_chat_view_whatsapp, parent, false);
            holder = new SendMessageViewHolder(view);
        } else if (viewType == ListType.RECEIVER_VIEW_TYPE) {
            //setMargins(mContext, 0,0,0,0);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_chat_view_whatsapp, parent, false);
            holder = new ReceivedMessageViewHolder(view);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataSet.get(position).getType()) {
            case 21:
                return ListType.SENDER_VIEW_TYPE;
            case 22:
                return ListType.RECEIVER_VIEW_TYPE;
            default:
                return -1;
        }
    }

    private void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof SendMessageViewHolder) {

            MessageData model = dataSet.get(position);
            ((SendMessageViewHolder) holder).bind(model, position);
        }

        if (holder instanceof ReceivedMessageViewHolder) {

            MessageData model = dataSet.get(position);
            ((ReceivedMessageViewHolder) holder).bind(model, position);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItems(List<MessageData> items){
        dataSet.addAll(items);
        notifyDataSetChanged();;
    }

    public void addListToTop(List<MessageData> messageDataList) {
        notifyDataSetChanged();
       /* for(int i=0;i<messageDataList.size();i++)
        {
            this.dataSet.add(i,messageDataList.get(i));
            notifyItemInserted(i);
        }*/
    }

}