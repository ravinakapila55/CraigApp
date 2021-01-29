package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.SkillAdapter;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.models.JobModelForUserAllJobs;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JobDetailActivity extends AppCompatActivity
{

    int position;

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.txt_short_des)
    TextView txt_short_des;

    @BindView(R.id.txt_long_des)
    TextView txt_long_des;

    @BindView(R.id.cc)
    ConstraintLayout cc;

    @BindView(R.id.tvBudget)
    TextView tvBudget;

    @BindView(R.id.tvApplicant)
    TextView tvApplicant;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.txt_duration)
    TextView txt_duration;

   /* @BindView(R.id.iv_phone)6
    ImageView iv_phone;*/

    @BindView(R.id.iv_chat)
    ImageView iv_chat;

    @BindView(R.id.img_profile)
    ImageView img_profile;

    JobModelForClient jobModelForUserAllJobs;
    JobModelForUserAllJobs forUserAllJobs;

    @BindView(R.id.tvExperienceLabel)
    TextView tvExperienceLabel;

    @BindView(R.id.tvToolsLbel)
    TextView tvToolsLbel;

    List<String> checkBoxListForSkills = new ArrayList<>();
    List<String> checkBoxListForTools = new ArrayList<>();

    @BindView(R.id.recycler_view_skills)
    RecyclerView recycler_view_skills;

    @BindView(R.id.recycler_view_tools)
    RecyclerView recycler_view_tools;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);

        txt_title.setText(getResources().getString(R.string.job_details));

        Intent intent = getIntent();
        if (intent != null)
        {
            position = intent.getIntExtra("position", 0);
            Log.e("position ",position+"");
            from = intent.getStringExtra("from");
            Log.e("from ",from+"");
        }

        if(from.equalsIgnoreCase("client"))
        {
            jobModelForUserAllJobs = MyJobsFragment.jobModelForClientList.get(position);
            iv_chat.setVisibility(View.VISIBLE);
            cc.setVisibility(View.VISIBLE);
            iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.edit));

            txt_duration.setText(jobModelForUserAllJobs.getStart_date() + " to " + jobModelForUserAllJobs.getEnd_date());
            txt_name.setText(jobModelForUserAllJobs.getName());
            txt_short_des.setText(jobModelForUserAllJobs.getShort_desc());
            txt_long_des.setText(jobModelForUserAllJobs.getLong_desc());
            tvBudget.setText(jobModelForUserAllJobs.getCost()+"-"+jobModelForUserAllJobs.getMax_price());
            tvApplicant.setText(jobModelForUserAllJobs.getApplicants_required());
            tvAddress.setText(jobModelForUserAllJobs.getLocation());

            try
            {
                JSONArray jsonArraySkills = new JSONArray(jobModelForUserAllJobs.getSkills_required());
                Log.e("SkilssArrayLength ",jsonArraySkills.length()+"");
                for(int i = 0; i < jsonArraySkills.length(); i++)
                {
                    checkBoxListForSkills.add(jsonArraySkills.getString(i));
                }
                JSONArray jsonArrayTools = new JSONArray(jobModelForUserAllJobs.getTools_needed());
                Log.e("toolsArrayLength ",jsonArrayTools.length()+"");

                for(int i = 0; i < jsonArrayTools.length(); i++)
                {
                    checkBoxListForTools.add(jsonArrayTools.getString(i));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (checkBoxListForSkills.size()==0)
            {
                tvExperienceLabel.setVisibility(View.GONE);
                recycler_view_skills.setVisibility(View.GONE);
            }
            else
            {
                tvExperienceLabel.setVisibility(View.VISIBLE);
                recycler_view_skills.setVisibility(View.VISIBLE);
                recycler_view_skills.setLayoutManager(new LinearLayoutManager(this));
                recycler_view_skills.setAdapter(new SkillAdapter(this, checkBoxListForSkills));
            }

            if (checkBoxListForTools.size()==0)
            {
                tvToolsLbel.setVisibility(View.GONE);
                recycler_view_tools.setVisibility(View.GONE);
            }
            else
            {
                tvToolsLbel.setVisibility(View.VISIBLE);
                recycler_view_tools.setVisibility(View.VISIBLE);
                recycler_view_tools.setLayoutManager(new LinearLayoutManager(this));
                recycler_view_tools.setAdapter(new SkillAdapter(this, checkBoxListForTools));
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user_img);
            Glide.with(JobDetailActivity.this).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL +
                    jobModelForUserAllJobs.getImage()).into(img_profile);
        }
        //txt_name
        else {
            Log.e("iddd ",MyJobsFragment.jobModelListForApplied.get(position).getId()+"");
            forUserAllJobs = MyJobsFragment.jobModelListForApplied.get(position);
            iv_chat.setVisibility(View.VISIBLE);
            cc.setVisibility(View.GONE);
            iv_chat.setImageDrawable(getResources().getDrawable(R.drawable.ic_message));

            Log.e("GetName ",forUserAllJobs.getName());
            Log.e("short ",forUserAllJobs.getShort_desc());
            Log.e("id ",forUserAllJobs.getId()+"");
            txt_duration.setText(forUserAllJobs.getStart_date() + " to " + forUserAllJobs.getEnd_date());
            txt_name.setText(forUserAllJobs.getName());
            txt_short_des.setText(forUserAllJobs.getShort_desc());
            txt_long_des.setText(forUserAllJobs.getLong_desc());

            try
            {
                JSONArray jsonArraySkills = new JSONArray(forUserAllJobs.getSkills_required());
                Log.e("SkilssArrayLength ",jsonArraySkills.length()+"");

                for(int i = 0; i < jsonArraySkills.length(); i++)
                {
                    checkBoxListForSkills.add(jsonArraySkills.getString(i));
                }

                JSONArray jsonArrayTools = new JSONArray(forUserAllJobs.getTools_needed());
                Log.e("toolsArrayLength ",jsonArrayTools.length()+"");

                for(int i = 0; i < jsonArrayTools.length(); i++)
                {
                    checkBoxListForTools.add(jsonArrayTools.getString(i));
                }
            }

            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (checkBoxListForSkills.size()==0)
            {
                tvExperienceLabel.setVisibility(View.GONE);
                recycler_view_skills.setVisibility(View.GONE);
            }
            else
            {
                tvExperienceLabel.setVisibility(View.VISIBLE);
                recycler_view_skills.setVisibility(View.VISIBLE);
                recycler_view_skills.setLayoutManager(new LinearLayoutManager(this));
                recycler_view_skills.setAdapter(new SkillAdapter(this, checkBoxListForSkills));
            }

            if (checkBoxListForTools.size()==0)
            {
                tvToolsLbel.setVisibility(View.GONE);
                recycler_view_tools.setVisibility(View.GONE);
            }
            else
            {
                tvToolsLbel.setVisibility(View.VISIBLE);
                recycler_view_tools.setVisibility(View.VISIBLE);
                recycler_view_tools.setLayoutManager(new LinearLayoutManager(this));
                recycler_view_tools.setAdapter(new SkillAdapter(this, checkBoxListForTools));
            }

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user_img);
            Glide.with(JobDetailActivity.this).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL +
                    forUserAllJobs.getImage()).into(img_profile);

        }
    }

    @OnClick(R.id.iv_back)
    public void iv_back()
    {
        finish();
    }


   /* @OnClick(R.id.iv_phone)
    public void iv_phone() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + "9869869869"));//change the number.
        startActivity(callIntent);
    }*/

    @OnClick(R.id.iv_chat)
    public void iv_chat()
    {
        if(from.equalsIgnoreCase("client"))
        {
            Intent intent = new Intent(this, AddJobActivity.class);
            intent.putExtra("message", getResources().getString(R.string.edit_job));
            intent.putExtra("job_id", jobModelForUserAllJobs.getId() + "");
            intent.putExtra("position", position + "");
            intent.putExtra("from", "detail");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        else {
            Intent intent = new Intent(JobDetailActivity.this, ChatActivity.class);
            intent.putExtra("senderId", MyApplication.getInstance().useString("user_id"));
            intent.putExtra("receiverId", forUserAllJobs.getUser_id() + "");
            intent.putExtra("receiver_job_id", forUserAllJobs.getId() + "");
            intent.putExtra("sender_job_id", forUserAllJobs.getApplicant_id() + "");
            intent.putExtra("receiver_name", forUserAllJobs.getUsername());
            intent.putExtra("budget",forUserAllJobs.getUser_cost());
            intent.putExtra("offer", forUserAllJobs.getUser_job_desc());
            intent.putExtra("other", forUserAllJobs.getOther_info());

            startActivity(intent);
        }

    }
}
