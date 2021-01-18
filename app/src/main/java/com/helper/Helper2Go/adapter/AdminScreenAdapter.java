package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.models.JobModelForUserAllJobs;
import com.helper.Helper2Go.ui.ChatActivity;
import com.helper.Helper2Go.ui.JobDetailActivity;
import com.helper.Helper2Go.ui.fragments.HomeFragment;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.ui.fragments.SearchFragment;
import com.helper.Helper2Go.utils.CommonMethods;
import com.helper.Helper2Go.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminScreenAdapter extends RecyclerView.Adapter<AdminScreenAdapter.MyViewHolder> {

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    MyJobsFragment myJobsFragment;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

     /*   @BindView(R.id.home_card)
        CardView home_card;
        @BindView(R.id.iv_avtar)
        CircleImageView iv_avtar;
        @BindView(R.id.tv_about_heading)
        TextView tv_name;
        @BindView(R.id.tvt_des)
        TextView tvt_des;
        @BindView(R.id.tv_duration)
        TextView tv_duration;*/

        @BindView(R.id.txt_job_title)
        TextView txt_job_title;

        @BindView(R.id.tvSeeDetails)
        TextView tvSeeDetails;

        @BindView(R.id.iv_chat)
        ImageView iv_chat;




        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            txt_job_title.setVisibility(View.VISIBLE);
            tvSeeDetails.setVisibility(View.GONE);
            iv_chat.setVisibility(View.VISIBLE);
        }
    }

    Context context;
    Integer[] mlist;
    List<JobModelForUserAllJobs> jobModelList = new ArrayList<>();

    String fromWhere;

    Integer[] color_list;


    public AdminScreenAdapter(Context context, Integer[] mlist, MyJobsFragment myJobsFragment,
                              List<JobModelForUserAllJobs> jobModelList, String fromWhere)
    {
        this.context = context;
        this.mlist = mlist;
        this.myJobsFragment = myJobsFragment;
        this.jobModelList = jobModelList;
        this.fromWhere = fromWhere;
    }

    @Override
    public AdminScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    /*    View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_screen_item, parent, false);*/

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_helper_jobs, parent, false);



        //view.setOnClickListener(MainActivity.myOnClickListener);

        AdminScreenAdapter.MyViewHolder myViewHolder = new AdminScreenAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdminScreenAdapter.MyViewHolder holder, final int listPosition) {
        Random rnd = new Random();
     /*   int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.home_card.setCardBackgroundColor(mlist[listPosition]);


        String date_posted = CommonMethods.getFormatedDateTime(jobModelList.get(listPosition).getCreated_at(),
                "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy");

        holder.tv_duration.setText(date_posted);
        holder.tvt_des.setText(jobModelList.get(listPosition).getShort_desc());
        holder.tv_name.setText(jobModelList.get(listPosition).getName());


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_img);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL + jobModelList.get(listPosition).getImage()).into(holder.iv_avtar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromWhere.equalsIgnoreCase("h")) {
                    homeFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("s")){
                    searchFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("m")){
                    myJobsFragment.openUserDetail(listPosition);
                }
            }
        });*/

        holder. txt_job_title.setVisibility(View.VISIBLE);
        holder. tvSeeDetails.setVisibility(View.GONE);
        holder. iv_chat.setVisibility(View.VISIBLE);
      Log.e("jobName ",jobModelList.get(listPosition).getName());
      holder.txt_job_title.setText(jobModelList.get(listPosition).getName());

      Log.e("jobNameAfter ",jobModelList.get(listPosition).getName());
      holder.iv_chat.setOnClickListener(new View.OnClickListener()
      {
            @Override
            public void onClick(View view)
            {
               /* if(fromWhere.equalsIgnoreCase("h"))
                {
                    homeFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("s")){
                    searchFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("m")){
                    myJobsFragment.openUserDetail(listPosition);
                }*/

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("senderId", MyApplication.getInstance().useString("user_id"));
                intent.putExtra("receiverId", jobModelList.get(listPosition).getUser_id() + "");
                intent.putExtra("receiver_job_id", jobModelList.get(listPosition).getId() + "");
                intent.putExtra("sender_job_id", jobModelList.get(listPosition).getApplicant_id() + "");
                intent.putExtra("receiver_name", jobModelList.get(listPosition).getUsername());
                intent.putExtra("budget",jobModelList.get(listPosition).getUser_cost());
                intent.putExtra("offer", jobModelList.get(listPosition).getUser_job_desc());
                intent.putExtra("other", jobModelList.get(listPosition).getOther_info());
                context.startActivity(intent);
            }
      });
    }

    @Override
    public int getItemCount()
    {
        return jobModelList.size();
    }
}


