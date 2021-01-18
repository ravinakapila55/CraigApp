package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.models.SkillsModel;
import com.helper.Helper2Go.ui.AddJobActivity;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyJobsAdapter extends RecyclerView.Adapter<MyJobsAdapter.MyViewHolder> {


    Context context;

    List<JobModelForClient> jobModelForClientList = new ArrayList<>();
    MyJobsFragment myJobsFragment;

    public MyJobsAdapter(Context context, List<JobModelForClient> jobModelForClients, MyJobsFragment myJobsFragment){
        this.context = context;
        this.jobModelForClientList = jobModelForClients;
        this.myJobsFragment = myJobsFragment;
    }

    @NonNull
    @Override
    public MyJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_jobs_list, parent, false);

        MyJobsAdapter.MyViewHolder myViewHolder = new MyJobsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyJobsAdapter.MyViewHolder holder, int position) {
        holder.iv_delete_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobsFragment.deleteJob(position);
            }
        });

        holder.iv_edit_job.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                myJobsFragment.editJob(position);
            }
        });

        holder.ll_see_job_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myJobsFragment.seeJobDetails(position);
            }
        });

        holder.txt_job_title.setText(jobModelForClientList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return jobModelForClientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView txt_job_title;
        ImageView iv_edit_job, iv_delete_job;
        LinearLayout ll_see_job_details;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_job_title = itemView.findViewById(R.id.txt_job_title);
            iv_edit_job = itemView.findViewById(R.id.iv_edit_job);
            iv_delete_job = itemView.findViewById(R.id.iv_delete_job);
            ll_see_job_details = itemView.findViewById(R.id.ll_see_job_details);
        }
    }

}


