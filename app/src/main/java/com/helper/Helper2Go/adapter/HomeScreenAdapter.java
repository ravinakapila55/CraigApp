package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.ui.fragments.HomeFragment;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.ui.fragments.SearchFragment;
import com.helper.Helper2Go.utils.CommonMethods;
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

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.MyViewHolder> {

    HomeFragment homeFragment;
    SearchFragment searchFragment;
    MyJobsFragment myJobsFragment;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_card)
        CardView home_card;
        @BindView(R.id.iv_avtar)
        CircleImageView iv_avtar;
        @BindView(R.id.tv_about_heading)
        TextView tv_name;
        @BindView(R.id.tvt_des)
        TextView tvt_des;
        @BindView(R.id.tv_duration)
        TextView tv_duration;


        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    Context context;
    Integer[] mlist;
    List<JobModel> jobModelList = new ArrayList<>();

    String fromWhere;

    Integer[] color_list;


    public HomeScreenAdapter(Context context, Integer[] mlist, HomeFragment homeFragment, List<JobModel> jobModelList, String fromWhere) {
        this.context = context;
        this.mlist = mlist;
        this.homeFragment = homeFragment;
        this.jobModelList = jobModelList;
        this.fromWhere = fromWhere;
    }


    public HomeScreenAdapter(Context context, Integer[] mlist, SearchFragment searchFragment, List<JobModel> jobModelList, String fromWhere) {
        this.context = context;
        this.mlist = mlist;
        this.searchFragment = searchFragment;
        this.jobModelList = jobModelList;
        this.fromWhere = fromWhere;
    }

    public HomeScreenAdapter(Context context, Integer[] mlist, MyJobsFragment myJobsFragment, List<JobModel> jobModelList, String fromWhere) {
        this.context = context;
        this.mlist = mlist;
        this.myJobsFragment = myJobsFragment;
        this.jobModelList = jobModelList;
        this.fromWhere = fromWhere;
    }

    @Override
    public HomeScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_screen_item, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        HomeScreenAdapter.MyViewHolder myViewHolder = new HomeScreenAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final HomeScreenAdapter.MyViewHolder holder, final int listPosition)
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.home_card.setCardBackgroundColor(mlist[listPosition]);

        String date_posted = CommonMethods.getFormatedDateTime(jobModelList.get(listPosition).getCreated_at(),
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", "dd-MM-yyyy");

        holder.tv_duration.setText(date_posted);
        holder.tvt_des.setText(jobModelList.get(listPosition).getShort_desc());
        holder.tv_name.setText(jobModelList.get(listPosition).getName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_img);

        Glide.with(context).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL +
        jobModelList.get(listPosition).getImage()).into(holder.iv_avtar);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(fromWhere.equalsIgnoreCase("h"))
                {
                    homeFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("s"))
                {
                    searchFragment.openUserDetail(listPosition);
                }
                else if (fromWhere.equalsIgnoreCase("m"))
                {
                    myJobsFragment.openUserDetail(listPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobModelList.size();
    }
}

