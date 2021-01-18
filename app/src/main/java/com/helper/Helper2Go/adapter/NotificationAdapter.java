package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.NotificationModel;
import com.helper.Helper2Go.utils.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.internal.ListenerClass;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {


    Context context;

    List<NotificationModel> notificationModelList = new ArrayList<>();

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList){
        this.context = context;
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_layout, parent, false);

        NotificationAdapter.MyViewHolder myViewHolder = new NotificationAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_notification.setText(notificationModelList.get(position).getMessage());
        holder.txt_time.setText(CommonMethods.getFormatedDateTime(notificationModelList.get(position).getCreated_at(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", "hh:mm aa").toUpperCase());
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_notification, txt_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_notification = itemView.findViewById(R.id.txt_notification);
            txt_time = itemView.findViewById(R.id.txt_time);
        }
    }

    public void setTaskList(List<NotificationModel> taskList) {
        this.notificationModelList = taskList;
        notifyDataSetChanged();
    }

}
