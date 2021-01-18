package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModelForUserAllJobs;
import com.helper.Helper2Go.ui.fragments.HomeFragment;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.ui.fragments.SearchFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceJobsHelperAdapter extends RecyclerView.Adapter<AdminScreenAdapter.MyViewHolder>
{

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    MyJobsFragment myJobsFragment;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.txt_job_title)
        TextView txt_job_title;

        @BindView(R.id.tvSeeDetails)
        TextView tvSeeDetails;

        @BindView(R.id.iv_chat)
        ImageView iv_chat;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(this, itemView);

            txt_job_title.setVisibility(View.VISIBLE);
            tvSeeDetails.setVisibility(View.VISIBLE);
            iv_chat.setVisibility(View.GONE);
        }
    }

    Context context;
    Integer[] mlist;
    List<JobModelForUserAllJobs> jobModelList = new ArrayList<>();
    String fromWhere;
    Integer[] color_list;


    public InvoiceJobsHelperAdapter(Context context, Integer[] mlist, MyJobsFragment myJobsFragment,
                              List<JobModelForUserAllJobs> jobModelList, String fromWhere)
    {
        this.context = context;
        this.mlist = mlist;
        this.myJobsFragment = myJobsFragment;
        this.jobModelList = jobModelList;
        this.fromWhere = fromWhere;
    }

    @Override
    public AdminScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
    /*    View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_screen_item, parent, false);*/

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_helper_jobs, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        AdminScreenAdapter.MyViewHolder myViewHolder = new AdminScreenAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdminScreenAdapter.MyViewHolder holder, final int listPosition)
    {
        Random rnd = new Random();

       /* int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
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

      holder.txt_job_title.setVisibility(View.VISIBLE);
      holder.tvSeeDetails.setVisibility(View.VISIBLE);
      holder.iv_chat.setVisibility(View.GONE);
      Log.e("jobName ",jobModelList.get(listPosition).getName());
      holder.txt_job_title.setText(jobModelList.get(listPosition).getName());
      holder.tvSeeDetails.setText("CHF "+jobModelList.get(listPosition).getHelper_amount());
      Log.e("jobNameAfter ",jobModelList.get(listPosition).getName());
    }

    @Override
    public int getItemCount()
    {
        return jobModelList.size();
    }

}



