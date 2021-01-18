package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.models.PaymentModel;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {


    Context context;

    List<PaymentModel> paymentModelList = new ArrayList<>();
    MyJobsFragment myJobsFragment;

    public PaymentAdapter(Context context, List<PaymentModel> paymentModelList, MyJobsFragment myJobsFragment){
        this.context = context;
        this.paymentModelList = paymentModelList;
        this.myJobsFragment = myJobsFragment;
    }

    @NonNull
    @Override
    public PaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_list_item, parent, false);

        PaymentAdapter.MyViewHolder myViewHolder = new PaymentAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.MyViewHolder holder, int position) {
        holder.txt_job_title.setText(paymentModelList.get(position).getJob_name());
        holder.txt_job_cost.setText("CHF "+paymentModelList.get(position).getUser_cost());
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_job_title, txt_job_cost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_job_title = itemView.findViewById(R.id.txt_job_title);
            txt_job_cost = itemView.findViewById(R.id.txt_job_cost);

        }
    }

}


