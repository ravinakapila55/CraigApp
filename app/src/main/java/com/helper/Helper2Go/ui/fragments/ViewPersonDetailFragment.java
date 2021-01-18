package com.helper.Helper2Go.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.SkillAdapter;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.ui.ApplyAsHelperActivity;
import com.helper.Helper2Go.utils.CommonMethods;
import com.helper.Helper2Go.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ViewPersonDetailFragment extends DialogFragment {

    View mRootView;

    Activity activity;

    @BindView(R.id.recycler_view_skills)
    RecyclerView recycler_view_skills;

    @BindView(R.id.recycler_view_tools)
    RecyclerView recycler_view_tools;


    @BindView(R.id.img_profile)
    ImageView img_profile;

    @BindView(R.id.txt_name)
    TextView txt_name;

    @BindView(R.id.txt_long_des)
    TextView txt_long_des;




   @BindView(R.id.tvExperienceLabel)
    TextView tvExperienceLabel;


   @BindView(R.id.tvToolsLbel)
    TextView tvToolsLbel;

   @BindView(R.id.tvBudget)
    TextView tvBudget;

   @BindView(R.id.tvDays)
    TextView tvDays;


    @BindView(R.id.edt_price)
    EditText edt_price;


    List<String> checkBoxListForSkills = new ArrayList<>();
    List<String> checkBoxListForTools = new ArrayList<>();

    public static int position = 0;
    List<JobModel> jobModelList = new ArrayList<>();

    public ViewPersonDetailFragment() {
        // Required empty public constructor
    }

    static String fromWhere = "";

    public static ViewPersonDetailFragment newInstance(int pos, String fromWheres) {
        position = pos;
        fromWhere = fromWheres;
        ViewPersonDetailFragment fragment = new ViewPersonDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if (getDialog() != null && getDialog().getWindow() != null)
        {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCancelable(false);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        activity = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_view_person_detail, container, false);
        ButterKnife.bind(this, mRootView);

        if(fromWhere.equalsIgnoreCase("h"))
        {
            if(jobModelList.size() != 0)
            {
                jobModelList.clear();
            }
            jobModelList = HomeFragment.jobModelList;
        }
        else if(fromWhere.equalsIgnoreCase("s"))
        {
            if(jobModelList.size() != 0)
            {
                jobModelList.clear();
            }
            jobModelList = SearchFragment.jobModelList;
        }

        try
        {
            JSONArray jsonArraySkills = new JSONArray(jobModelList.get(position).getSkills_required());
            Log.e("SkilssArrayLength ",jsonArraySkills.length()+"");
            for(int i = 0; i < jsonArraySkills.length(); i++)
            {
                checkBoxListForSkills.add(jsonArraySkills.getString(i));
            }

            JSONArray jsonArrayTools = new JSONArray(jobModelList.get(position).getTools_needed());
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
            recycler_view_skills.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view_skills.setAdapter(new SkillAdapter(getContext(), checkBoxListForSkills));
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
            recycler_view_tools.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view_tools.setAdapter(new SkillAdapter(getContext(), checkBoxListForTools));
        }

        txt_long_des.setText(jobModelList.get(position).getLong_desc());
        txt_name.setText(jobModelList.get(position).getName());

        //2020-09-30
        String currentDate="";
        currentDate= CommonMethods.getCurrentDate();
        Log.e("CurrentDate ",currentDate);


        long differentDate;

        if (jobModelList.get(position).getEnd_date().equalsIgnoreCase("null"))
        {
            tvDays.setVisibility(View.GONE);
        }
        else {

            String endDate=jobModelList.get(position).getEnd_date();
            Log.e("endDate ",endDate);
            differentDate=CommonMethods.difference2Dates(endDate,currentDate);
            Log.e("differentDate ",differentDate+"");

            tvDays.setVisibility(View.VISIBLE);

            tvDays.setText(differentDate+ " days left");

        }


        /* if (jobModelList.get(position).getCost().equalsIgnoreCase("null"))
        {
            tvBudget.setText("Budget: NA");
        }
        else {
            tvBudget.setText("Budget: "+jobModelList.get(position).getCost());
        }*/
        tvBudget.setText("Budget:"+jobModelList.get(position).getCost());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_img);
        Glide.with(getContext()).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL + jobModelList.get(position).getImage()).into(img_profile);

        return mRootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState)

    {
        super.onActivityCreated(savedInstanceState);

        if (getDialog() != null && getDialog().getWindow() != null)
        {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            getDialog().setCancelable(false);
        }


    }


    public static int job_id;
    public static String job_name;
    public static String job_price;
    @OnClick(R.id.rel_apply_as_helper)
    public void rel_apply_as_helper() {




            Intent intent = new Intent(getContext(), ApplyAsHelperActivity.class);
            job_id = jobModelList.get(position).getId();
            job_name = jobModelList.get(position).getName();
//            job_price = "200";
            //intent.putExtra("job_id", jobModelList.get(position).getId());
            startActivity(intent);
            getDialog().dismiss();




      /*  if(edt_price.getText().toString().equalsIgnoreCase("")){
            MyApplication.getInstance().displayMessageNew(getDialog().getWindow().getDecorView(), getResources().getString(R.string.please_enter_price), getActivity());
        }
        else {
            Intent intent = new Intent(getContext(), ApplyAsHelperActivity.class);
            job_id = jobModelList.get(position).getId();
            job_name = jobModelList.get(position).getName();
            job_price = edt_price.getText().toString();
            //intent.putExtra("job_id", jobModelList.get(position).getId());
            startActivity(intent);
            getDialog().dismiss();
        }*/
    }

    @OnClick(R.id.iv_close)
    public void iv_close() {
        getDialog().dismiss();
    }
}
