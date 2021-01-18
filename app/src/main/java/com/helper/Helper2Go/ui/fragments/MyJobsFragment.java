package com.helper.Helper2Go.ui.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.AcceptedHelperJobAdapter;
import com.helper.Helper2Go.adapter.AdminScreenAdapter;
import com.helper.Helper2Go.adapter.ExpandableAdapter;
import com.helper.Helper2Go.adapter.InvoiceJobsHelperAdapter;
import com.helper.Helper2Go.adapter.MyJobsAdapter;
import com.helper.Helper2Go.adapter.PaymentAdapter;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.models.JobModelForUserAllJobs;
import com.helper.Helper2Go.models.PaymentModel;
import com.helper.Helper2Go.ui.AddJobActivity;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.ui.JobDetailActivity;
import com.helper.Helper2Go.ui.UserDetailActivity;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyJobsFragment extends Fragment
{

    public static List<JobModelForUserAllJobs> jobModelListForApplied = new ArrayList<>();
    public static List<JobModel> jobModelListForCompleted = new ArrayList<>();
    public static List<JobModelForUserAllJobs> jobModelForUserAllJobs = new ArrayList<>();

    View mRootView;

    @BindView(R.id.recycler_view_my_jobs)
    RecyclerView recycler_view_my_jobs;

    @BindView(R.id.txt_no_job)
    TextView txt_no_job;

    @BindView(R.id.txt_no_job_helper)
    TextView txt_no_job_helper;

    @BindView(R.id.recycler_view_client_my_jobs)
    RecyclerView recycler_view_client_my_jobs;

    @BindView(R.id.recycler_view_client_my_jobs_settings)
    RecyclerView recycler_view_client_my_jobs_settings;

    @BindView(R.id.txt_my_jobs_count)
    TextView txt_my_jobs_count;

    @BindView(R.id.txt_my_jobs_count_settings)
    TextView txt_my_jobs_count_settings;

    @BindView(R.id.iv_arrow_my_jobs)
    ImageView iv_arrow_my_jobs;

    @BindView(R.id.iv_arrow_my_jobs_settings)
    ImageView iv_arrow_my_jobs_settings;

    @BindView(R.id.rel_inqueries_outer)
    RelativeLayout rel_inqueries_outer;

    @BindView(R.id.recycler_view_client_inqueries)
    ExpandableListView recycler_view_client_inqueries;

    @BindView(R.id.txt_inqueries_count)
    TextView txt_inqueries_count;

    @BindView(R.id.tvGrandLabel)
    TextView tvGrandLabel;

    @BindView(R.id.tvTitalHelper)
    TextView tvTitalHelper;

    @BindView(R.id.tvGrandLabelClient)
    TextView tvGrandLabelClient;

    @BindView(R.id.tvTotalClient)
    TextView tvTotalClient;

    @BindView(R.id.iv_arrow_inqueries)
    ImageView iv_arrow_my_inqueries;

    @BindView(R.id.rel_helper_view)
    RelativeLayout rel_helper_view;

    @BindView(R.id.rel_helper_view_jobs)
    RelativeLayout rel_helper_view_jobs;

    @BindView(R.id.rel_my_jobs_helper)
    RelativeLayout rel_my_jobs_helper;

    @BindView(R.id.rel_inqueries_helper)
    RelativeLayout rel_inqueries_helper;

    @BindView(R.id.rel_sub_my_job_helper)
    RelativeLayout rel_sub_my_job_helper;

    @BindView(R.id.rel_payments_helper)
    RelativeLayout rel_payments_helper;

    @BindView(R.id.rel_sub_payment_helper)
    RelativeLayout rel_sub_payment_helper;

    @BindView(R.id.rel_sub_inqueries_helper)
    RelativeLayout rel_sub_inqueries_helper;

    @BindView(R.id.txt_my_jobs_count_helper)
    TextView txt_my_jobs_count_helper;

    @BindView(R.id.txt_message_payment_helper)
    TextView txt_message_payment_helper;

    @BindView(R.id.txt_payment_count_helper)
    TextView txt_payment_count_helper;

      @BindView(R.id.txt_inqueries_count_helper)
    TextView txt_inqueries_count_helper;

    @BindView(R.id.iv_arrow_my_jobs_helper)
    ImageView iv_arrow_my_jobs_helper;

    @BindView(R.id.iv_arrow_payment_helper)
    ImageView iv_arrow_payment_helper;

    @BindView(R.id.iv_arrow_inqueries_helper)
    ImageView iv_arrow_inqueries_helper;

    @BindView(R.id.recycler_view_client_my_jobs_helper)
    RecyclerView recycler_view_client_my_jobs_helper;

    @BindView(R.id.recycler_view_inqueires_helper)
    RecyclerView recycler_view_inqueires_helper;

    @BindView(R.id.recycler_view_client_payment_helper)
    RecyclerView recycler_view_client_payment_helper;

    @BindView(R.id.rel_client_view)
    RelativeLayout rel_client_view;

    List<JobModelForClient> listDataHeader;
    HashMap<JobModelForClient, List<JobModelForClient.Applicants>> listDataChild;

    @BindView(R.id.recycler_view_client_payment)
    RecyclerView recycler_view_client_payment;

    @BindView(R.id.txt_payment_count)
    TextView txt_payment_count;

    @BindView(R.id.txt_no_invoice_helper)
    TextView txt_no_invoice_helper;

    @BindView(R.id.iv_arrow_payment)
    ImageView iv_arrow_payment;

    List<PaymentModel> paymentModelList = new ArrayList<>();


    int[] arra = {
            Color.rgb(41, 155, 92), Color.rgb(41, 155, 92),
            Color.rgb(237, 182, 11), Color.rgb(237, 182, 11),
            Color.rgb(237, 48, 65), Color.rgb(237, 48, 65),
            Color.rgb(30, 59, 84), Color.rgb(30, 59, 84),
            Color.rgb(255, 165, 0), Color.rgb(255, 165, 0),
            Color.rgb(25, 144, 218), Color.rgb(25, 144, 218),
            Color.rgb(218, 25, 179), Color.rgb(218, 25, 179)
    };

    public static MyJobsFragment newInstance(String param1, String param2)
    {
        MyJobsFragment fragment = new MyJobsFragment();
        return fragment;
    }

    String type="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setNotiArgumentData(View view)
    {
        if (type.equalsIgnoreCase("apply"))
        {
            if(rel_client_view.getVisibility() == View.VISIBLE)
            {
                return;
            }
            changeButtonStyle(btn_client, R.color.white, R.drawable.yellow_button_without_radius, btn_helper,
                    R.color.black, R.drawable.grey_button_without_radius);
            rel_client_view.setVisibility(View.GONE);
            rel_helper_view_jobs.setVisibility(View.GONE);
            getMyJobs();
        }
        else if (type.equalsIgnoreCase("accept"))
        {
            if(rel_helper_view_jobs.getVisibility() == View.VISIBLE)
            {
                return ;
            }
            changeButtonStyle(btn_helper, R.color.white, R.drawable.yellow_button_without_radius, btn_client,
             R.color.black, R.drawable.grey_button_without_radius);
            rel_client_view.setVisibility(View.GONE);
//          rel_helper_view.setVisibility(View.GONE);
            rel_helper_view_jobs.setVisibility(View.GONE);
            getAppliedJobs();
        }

        else if (type.equalsIgnoreCase("reject"))
        {
            if(rel_helper_view_jobs.getVisibility() == View.VISIBLE)
            {
                return ;
            }
            changeButtonStyle(btn_helper, R.color.white, R.drawable.yellow_button_without_radius, btn_client, R.color.black, R.drawable.grey_button_without_radius);
            rel_client_view.setVisibility(View.GONE);
//        rel_helper_view.setVisibility(View.GONE);
            rel_helper_view_jobs.setVisibility(View.GONE);
            getAppliedJobs();

        }
        else if (type.equalsIgnoreCase("complete"))
        {
            if(rel_helper_view_jobs.getVisibility() == View.VISIBLE)
            {
                return ;
            }
            changeButtonStyle(btn_helper, R.color.white, R.drawable.yellow_button_without_radius, btn_client, R.color.black, R.drawable.grey_button_without_radius);
            rel_client_view.setVisibility(View.GONE);
//        rel_helper_view.setVisibility(View.GONE);
            rel_helper_view_jobs.setVisibility(View.GONE);
            getAppliedJobs();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        ButterKnife.bind(this, mRootView);


        //todo when notiifcation will come up
        if (getArguments()!=null)
        {
            Log.e("GetArgumnetInside ","yes");
            type = getArguments().getString("type");
            Log.e("type ",type);
            setNotiArgumentData(mRootView);
        }
        else
        {
            Log.e("GetArgumnetInside ","no");

            if(!HomeMainActivity.is_search) {
                getAppliedJobs();
            }
        }





        return mRootView;
    }

    @OnClick(R.id.btn_client)
    public void btn_client()
    {
        if(rel_client_view.getVisibility() == View.VISIBLE)
        {
            return;
        }
        changeButtonStyle(btn_client, R.color.white, R.drawable.yellow_button_without_radius, btn_helper,
                R.color.black, R.drawable.grey_button_without_radius);
        rel_client_view.setVisibility(View.GONE);
//      rel_helper_view.setVisibility(View.GONE);
        rel_helper_view_jobs.setVisibility(View.GONE);
        getMyJobs();
    }

    @OnClick(R.id.btn_helper)
    public void btn_helper()
    {
        if(rel_helper_view_jobs.getVisibility() == View.VISIBLE)
        {
            return;
        }

       /* if(rel_helper_view.getVisibility() == View.VISIBLE){
            return;
        }*/

        changeButtonStyle(btn_helper, R.color.white, R.drawable.yellow_button_without_radius, btn_client, R.color.black, R.drawable.grey_button_without_radius);
        rel_client_view.setVisibility(View.GONE);
//        rel_helper_view.setVisibility(View.GONE);
        rel_helper_view_jobs.setVisibility(View.GONE);
        getAppliedJobs();
    }

    public void getCompletedJobs() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.getCompletedJobs("Bearer " + MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        if (jobModelListForCompleted.size() != 0) {
                            jobModelListForCompleted.clear();
                        }
                        jobModelListForCompleted = generalResponse.getDataAsList("result", JobModel.class);
                        MyApplication.getInstance().hideProgress(getContext());

                        //setAdapterData(jobModelForUserAllJobs, "c");
                    } else {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });


    }


    public void getAppliedAndCompletedJobs()
    {
        if (!NetworkUtils.isNetworkAvailable(getActivity()))
        {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.
                getUserAllJobs("Bearer " + MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {

                        if (jobModelForUserAllJobs.size() != 0) {
                            jobModelForUserAllJobs.clear();
                        }
                        jobModelForUserAllJobs = generalResponse.getDataAsList("result", JobModelForUserAllJobs.class);

                        MyApplication.getInstance().hideProgress(getActivity());
                        //setAdapterData(jobModelForUserAllJobs, "a");
                    } else {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });
    }

    public void openUserDetail(int position)
    {
        Log.e("openUserDetail ",position+"");
        Intent intent = new Intent(getContext(), JobDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("from", "helper");
        startActivity(intent);
    }

    @BindView(R.id.btn_client)
    Button btn_client;

    @BindView(R.id.btn_helper)
    Button btn_helper;



    public void changeButtonStyle(Button selectedButton, int selectedButtoncolor, int SelectedButtondrawable, Button unSelectedButton, int unSelectedButtoncolor, int unSelectedButtondrawable) {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            selectedButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), SelectedButtondrawable));
            unSelectedButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), unSelectedButtondrawable));
        } else {
            selectedButton.setBackground(ContextCompat.getDrawable(getContext(), SelectedButtondrawable));
            unSelectedButton.setBackground(ContextCompat.getDrawable(getContext(), unSelectedButtondrawable));
        }

        selectedButton.setTextColor(getResources().getColor(selectedButtoncolor));
        unSelectedButton.setTextColor(getResources().getColor(unSelectedButtoncolor));
    }


    public void getAppliedJobs()
    {
        if (!NetworkUtils.isNetworkAvailable(getActivity()))
        {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).
                    findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.getAppliedJobs("Bearer " +
                MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(Response<ResponseBody> response)
            {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus())
                    {
                        if (jobModelListForApplied.size() != 0)
                        {
                            jobModelListForApplied.clear();
                        }
                        jobModelListForApplied = generalResponse.getDataAsList("result", JobModelForUserAllJobs.class);
                        MyApplication.getInstance().hideProgress(getContext());

                     /*   if (jobModelListForApplied.size() == 0) {
                            iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_down_arrow);
//                            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_down_arrow);
                        } else {
                            iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_arrow_up);
//                            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_arrow_up);
                        }
*/
                        setAdapterData(jobModelListForApplied);
//                        rel_helper_view.setVisibility(View.VISIBLE);
                        rel_helper_view_jobs.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                                generalResponse.getMessage(), getActivity());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e)
            {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete()
            {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });
    }

    public static List<JobModelForClient> jobModelForClientList = new ArrayList<>();
    List<JobModelForClient> jobModelForClientListInqueries = new ArrayList<>();

    public void getMyJobs() {
        if (!NetworkUtils.isNetworkAvailable(getActivity()))
        {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.getMyJobs("Bearer " + MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));

                try
                {
                    if (generalResponse.checkStatus())
                    {
                        if (jobModelForClientList.size() != 0) {
                            jobModelForClientList.clear();
                        }
                        jobModelForClientList = generalResponse.getDataAsList("result", JobModelForClient.class);

                        if (jobModelForClientListInqueries.size() != 0) {
                            jobModelForClientListInqueries.clear();
                        }
                        for (int i = 0; i < jobModelForClientList.size(); i++) {
                            if (jobModelForClientList.get(i).getApplicants().size() != 0) {
                                jobModelForClientListInqueries.add(jobModelForClientList.get(i));
                            }
                        }

                        if (jobModelForClientList.size() == 0) {
                            iv_arrow_my_jobs.setBackgroundResource(R.drawable.ic_down_arrow);
                            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_down_arrow);
                        } else {
                            iv_arrow_my_jobs.setBackgroundResource(R.drawable.ic_arrow_up);
                            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_arrow_up);
                        }

                        txt_my_jobs_count.setText(jobModelForClientList.size() + "");
                        txt_my_jobs_count_settings.setText(jobModelForClientList.size() + "");
                        recycler_view_client_my_jobs.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycler_view_client_my_jobs.setAdapter(new MyJobsAdapter(getContext(), jobModelForClientList, MyJobsFragment.this));


                        recycler_view_client_my_jobs_settings.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycler_view_client_my_jobs_settings.setAdapter(new MyJobsAdapter(getContext(), jobModelForClientList, MyJobsFragment.this));


                        if (jobModelForClientListInqueries.size() == 0)
                        {
                            iv_arrow_my_inqueries.setBackgroundResource(R.drawable.ic_down_arrow);
                        }
                        else
                        {
                            iv_arrow_my_inqueries.setBackgroundResource(R.drawable.ic_arrow_up);
                        }

                        txt_inqueries_count.setText(jobModelForClientListInqueries.size() + "");
                        listDataHeader = new ArrayList<JobModelForClient>();
                        listDataChild = new HashMap<JobModelForClient, List<JobModelForClient.Applicants>>();

                        if(jobModelForClientListInqueries.size() != 0)
                        {
                            for (int i = 0; i < jobModelForClientListInqueries.size(); i++)
                            {
                                listDataHeader.add(jobModelForClientListInqueries.get(i));
                            }

                            for (int i = 0; i < jobModelForClientListInqueries.size(); i++)
                            {
                                if (jobModelForClientListInqueries.get(i).getApplicants().size() != 0)
                                {
                                    listDataChild.put(listDataHeader.get(i), jobModelForClientListInqueries.get(i).getApplicants());
                                }
                                else
                                {
                                    listDataChild.put(listDataHeader.get(i), new ArrayList<JobModelForClient.Applicants>());
                                }
                            }

                            ExpandableAdapter listAdapter = new ExpandableAdapter(getActivity(),
                            listDataHeader, listDataChild, MyJobsFragment.this);
                            recycler_view_client_inqueries.setAdapter(listAdapter);
                            recycler_view_client_inqueries.expandGroup(0);
                        }

                        MyApplication.getInstance().hideProgress(getContext());
                        rel_client_view.setVisibility(View.VISIBLE);

                        if(jobModelForClientList.size() != 0)
                        {
                            recycler_view_client_my_jobs.setVisibility(View.VISIBLE);
                            recycler_view_client_my_jobs_settings.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            recycler_view_client_my_jobs.setVisibility(View.GONE);
                            recycler_view_client_my_jobs_settings.setVisibility(View.GONE);
                        }

                        if(jobModelForClientListInqueries.size() != 0)
                        {
                            recycler_view_client_inqueries.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            recycler_view_client_inqueries.setVisibility(View.GONE);
                        }

                        if(paymentModelList.size() != 0)
                        {
                            paymentModelList.clear();
                        }

                        for(int i = 0; i < jobModelForClientList.size(); i++)
                        {
                            for(int j = 0; j < jobModelForClientList.get(i).getApplicants().size(); j++)
                            {

                                if(jobModelForClientList.get(i).getApplicants().get(j).getAdmin_approval()==3)
                                {
                                    if(jobModelForClientList.get(i).getApplicants().get(j).getPayment_status() == (null)||
                                            jobModelForClientList.get(i).getApplicants().get(j).getPayment_status().equalsIgnoreCase("null"))
                                    {

                                    }
                                    else {
                                        PaymentModel paymentModel = new PaymentModel();
                                        paymentModel.setJob_name(jobModelForClientList.get(i).getName());
                                        paymentModel.setUser_cost(jobModelForClientList.get(i).getApplicants().get(j).getPayment_amount());
                                        paymentModelList.add(paymentModel);
                                    }
                                }

                            }
                        }

                        if(jobModelForClientListInqueries.size() != 0)
                        {
                            rel_inqueries_outer.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            rel_inqueries_outer.setVisibility(View.GONE);
                        }

                        if(paymentModelList.size() != 0)
                        {
                            recycler_view_client_payment.setVisibility(View.VISIBLE);

                            tvGrandLabelClient.setVisibility(View.VISIBLE);
                            tvTotalClient.setVisibility(View.VISIBLE);
                            //2801.55
                            double totalAmount=0;
                            double actualAmount=0;

                            for (int z = 0; z <paymentModelList.size() ; z++)
                            {
                                Log.e("indexPrice ",paymentModelList.get(z).getUser_cost());
                                actualAmount= Double.parseDouble(paymentModelList.get(z).getUser_cost());
                                Log.e("actualAmount ",actualAmount+"");
                                totalAmount=totalAmount+actualAmount;
                                Log.e("totalAmount ",totalAmount+"");
                            }

                            Log.e("totalAmountOutside ",totalAmount+"");
                            tvTotalClient.setText("CHF "+String.valueOf(totalAmount));
                        }
                        else
                        {
                            tvGrandLabelClient.setVisibility(View.GONE);
                            tvTotalClient.setVisibility(View.GONE);
                            recycler_view_client_payment.setVisibility(View.GONE);
                        }

                        if (paymentModelList.size() == 0)
                        {
                            iv_arrow_payment.setBackgroundResource(R.drawable.ic_down_arrow);
                        }
                        else
                        {
                            iv_arrow_payment.setBackgroundResource(R.drawable.ic_arrow_up);
                        }

                        txt_payment_count.setText(paymentModelList.size() + "");
                        recycler_view_client_payment.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycler_view_client_payment.setAdapter(new PaymentAdapter(getActivity(), paymentModelList,
                                MyJobsFragment.this));
                    }
                    else
                    {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                                generalResponse.getMessage(), getActivity());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e)
            {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete()
            {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });
    }

    ArrayList<JobModelForUserAllJobs> myAppliedJobs;
    ArrayList<JobModelForUserAllJobs> myAcceptedJobs;
    ArrayList<JobModelForUserAllJobs> myInvoiceJobs;

    private void setAdapterData(List<JobModelForUserAllJobs> jobModels)
    {
        List<Integer> myin = new ArrayList<>();
        myAppliedJobs=new ArrayList<>();
        myAcceptedJobs=new ArrayList<>();
        myInvoiceJobs=new ArrayList<>();
        myAppliedJobs.clear();
        myAcceptedJobs.clear();
        myInvoiceJobs.clear();
        int count = 0;

        for (int i = 0; i < jobModels.size(); i++)
        {
            if (jobModels.size() > arra.length)
            {
                if (count == arra.length)
                {
                   count = 0;
                }
            }

            myin.add(arra[count]);

            count++;

            Log.e("name ",jobModels.get(i).getName());
            Log.e("StatusAccepted ",jobModels.get(i).getStatus_accepted());
            if (jobModels.get(i).getStatus_accepted().equalsIgnoreCase("1")||jobModels.get(i).getStatus_accepted().equalsIgnoreCase("3")  )
            {
                myAcceptedJobs.add(jobModels.get(i));
            }
            else {
                myAppliedJobs.add(jobModels.get(i));
            }

            if ((jobModels.get(i).getHelper_amount()==(null))|| (jobModels.get(i).getHelper_amount().equalsIgnoreCase("null")))
            {

            }
            else
            {
                myInvoiceJobs.add(jobModels.get(i));
            }
        }

        Log.e("AcceptedJobList ",myAcceptedJobs.size()+"");
        Log.e("appliedJobs ",myAppliedJobs.size()+"");
        Log.e("myInvoiceJobs ",myInvoiceJobs.size()+"");

        for (int k = 0; k < myAcceptedJobs.size(); k++)
        {
            Log.e("AcceptedJobListData ",myAcceptedJobs.get(k).getName()+"");
            Log.e("AcceptedJobListDataShort ",myAcceptedJobs.get(k).getShort_desc()+"");
            Log.e("AcceptedJobListDataID ",myAcceptedJobs.get(k).getId()+"");
        }

        for (int n = 0; n <myAppliedJobs.size() ; n++)
        {
            Log.e("myAppliedJobsData ",myAppliedJobs.get(n).getName()+"");
        }

        if (myAppliedJobs.size() == 0) {
            iv_arrow_inqueries_helper.setBackgroundResource(R.drawable.ic_down_arrow);
        } else {
            iv_arrow_inqueries_helper.setBackgroundResource(R.drawable.ic_arrow_up);
        }

         if (myAcceptedJobs.size() == 0) {
             iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_down_arrow);
        } else {
             iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_arrow_up);
        }


       if (myInvoiceJobs.size() == 0) {
                 iv_arrow_payment_helper.setBackgroundResource(R.drawable.ic_down_arrow);
        } else {
                 iv_arrow_payment_helper.setBackgroundResource(R.drawable.ic_arrow_up);
        }

        for (int n = 0; n <myInvoiceJobs.size() ; n++)
        {
            Log.e("myInvoiceJobsData ",myInvoiceJobs.get(n).getName()+"");
        }


        if (myAcceptedJobs.size() > 0)
        {
            txt_no_job.setVisibility(View.GONE);
            recycler_view_client_my_jobs_helper.setVisibility(View.VISIBLE);

//          Integer[] new_color_arr = myin.toArray(new Integer[myin.size()]);
            recycler_view_client_my_jobs_helper.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view_client_my_jobs_helper.setAdapter(new AcceptedHelperJobAdapter(getContext(),
                    MyJobsFragment.this, myAcceptedJobs, "m"));

   /* recycler_view_client_my_jobs_helper.setAdapter(new AcceptedHelperJobAdapter(getContext(), new_color_arr,
      MyJobsFragment.this, myAcceptedJobs, "m"));
   */
        }

        else
        {
            recycler_view_client_my_jobs_helper.setVisibility(View.GONE);
            txt_no_job.setVisibility(View.VISIBLE);
        }

        if (myAppliedJobs.size() > 0)
        {
            txt_no_job_helper.setVisibility(View.GONE);
            recycler_view_inqueires_helper.setVisibility(View.VISIBLE);
//            txt_inqueries_count_helper.setText(jobModels.size());

            Integer[] new_color_arr1 = myin.toArray(new Integer[myin.size()]);
            recycler_view_inqueires_helper.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view_inqueires_helper.setAdapter(new AdminScreenAdapter(getContext(), new_color_arr1,
                    MyJobsFragment.this, myAppliedJobs, "m"));
        }
        else
        {
            recycler_view_inqueires_helper.setVisibility(View.GONE);
            txt_no_job_helper.setVisibility(View.VISIBLE);
        }

        if (myInvoiceJobs.size() > 0)
        {
            txt_no_invoice_helper.setVisibility(View.GONE);
            recycler_view_client_payment_helper.setVisibility(View.VISIBLE);


            Integer[] new_color_arr1 = myin.toArray(new Integer[myin.size()]);
            recycler_view_client_payment_helper.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view_client_payment_helper.setAdapter(new InvoiceJobsHelperAdapter(getContext(), new_color_arr1,
                    MyJobsFragment.this, myInvoiceJobs, "m"));

            tvGrandLabel.setVisibility(View.VISIBLE);
            tvTitalHelper.setVisibility(View.VISIBLE);
            //2801.55
            double totalAmount=0;
            double actualAmount=0;
            for (int z = 0; z <myInvoiceJobs.size() ; z++) {

                Log.e("indexPrice ",myInvoiceJobs.get(z).getPayment_amount());
                actualAmount= Double.parseDouble(myInvoiceJobs.get(z).getHelper_amount());
                Log.e("actualAmount ",actualAmount+"");
                totalAmount=totalAmount+actualAmount;
                Log.e("totalAmount ",totalAmount+"");
            }

            Log.e("totalAmountOutside ",totalAmount+"");

            tvTitalHelper.setText("CHF "+String.valueOf(totalAmount));

        }

        else
        {
            recycler_view_client_payment_helper.setVisibility(View.GONE);
            txt_no_invoice_helper.setVisibility(View.VISIBLE);
            tvGrandLabel.setVisibility(View.GONE);
            tvTitalHelper.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_arrow_my_jobs)
    public void iv_arrow_my_jobs()
    {
        if (jobModelForClientList.size() == 0)
        {
            return;
        }
        if (recycler_view_client_my_jobs.getVisibility() == View.VISIBLE)
        {
            iv_arrow_my_jobs.setBackgroundResource(R.drawable.ic_down_arrow);
            collapseMyJobs();
        } else
        {
            iv_arrow_my_jobs.setBackgroundResource(R.drawable.ic_arrow_up);
            expandMyJobs();
        }


    }

    @OnClick(R.id.iv_arrow_my_jobs_helper)
    public void iv_arrow_my_jobs_helper()
    {
        if (myAcceptedJobs.size() == 0)
        {
            return;
        }
        if (recycler_view_client_my_jobs_helper.getVisibility() == View.VISIBLE)
        {
            iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_down_arrow);
            collapseMyJobsHelper();
        } else
        {
            iv_arrow_my_jobs_helper.setBackgroundResource(R.drawable.ic_arrow_up);
            expandMyJobsHelper();
        }


    }

    @OnClick(R.id.iv_arrow_inqueries_helper)
    public void iv_arrow_inqueries_helper()
    {
        if (myAppliedJobs.size() == 0)
        {
            return;
        }
        if (recycler_view_inqueires_helper.getVisibility() == View.VISIBLE)
        {
            iv_arrow_inqueries_helper.setBackgroundResource(R.drawable.ic_down_arrow);
            collapseMyJobsHelperInqueries();
        } else
        {
            iv_arrow_inqueries_helper.setBackgroundResource(R.drawable.ic_arrow_up);
            expandMyJobsHelperInqueries();
        }


    }

    @OnClick(R.id.iv_arrow_payment_helper)
    public void iv_arrow_payment_helper()
    {
        if (myInvoiceJobs.size() == 0)
        {
            return;
        }
        if (recycler_view_client_payment_helper.getVisibility() == View.VISIBLE)
        {
            iv_arrow_payment_helper.setBackgroundResource(R.drawable.ic_down_arrow);
            collapseMyJobsHelperPayment();
        } else
        {
            iv_arrow_payment_helper.setBackgroundResource(R.drawable.ic_arrow_up);
            expandMyJobsHelperPayemnt();
        }


    }

    @OnClick(R.id.iv_arrow_my_jobs_settings)
    public void iv_arrow_my_jobs_settings()
    {
        if (jobModelForClientList.size() == 0)
        {
            return;
        }

        if (recycler_view_client_my_jobs_settings.getVisibility() == View.VISIBLE)
        {
            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_down_arrow);
            collapseMyJobs();
        }

        else
        {
            iv_arrow_my_jobs_settings.setBackgroundResource(R.drawable.ic_arrow_up);
            expandMyJobs();
        }
    }

    private void expandMyJobs()
    {
        //set Visible
        recycler_view_client_my_jobs.setVisibility(View.VISIBLE);
        recycler_view_client_my_jobs_settings.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_client_my_jobs.measure(widthSpec, heightSpec);
        recycler_view_client_my_jobs_settings.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorMyJobs(0, recycler_view_client_my_jobs.getMeasuredHeight());
        ValueAnimator mAnimator1 = slideAnimatorMyJobs(0, recycler_view_client_my_jobs_settings.getMeasuredHeight());
        mAnimator.start();
        mAnimator1.start();
    }

    private void expandMyJobsHelper()
    {
        //set Visible
        recycler_view_client_my_jobs_helper.setVisibility(View.VISIBLE);
//        recycler_view_client_my_jobs_settings.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_client_my_jobs_helper.measure(widthSpec, heightSpec);
//        recycler_view_client_my_jobs_settings.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorMyJobs(0, recycler_view_client_my_jobs_helper.getMeasuredHeight());
//        ValueAnimator mAnimator1 = slideAnimatorMyJobs(0, recycler_view_client_my_jobs_settings.getMeasuredHeight());
        mAnimator.start();
//        mAnimator1.start();
    }

    private void expandMyJobsHelperInqueries()
    {
        //set Visible
        recycler_view_inqueires_helper.setVisibility(View.VISIBLE);
//        recycler_view_client_my_jobs_settings.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_inqueires_helper.measure(widthSpec, heightSpec);
//        recycler_view_client_my_jobs_settings.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorMyJobs(0, recycler_view_inqueires_helper.getMeasuredHeight());
//        ValueAnimator mAnimator1 = slideAnimatorMyJobs(0, recycler_view_client_my_jobs_settings.getMeasuredHeight());
        mAnimator.start();
//        mAnimator1.start();
    }

    private void expandMyJobsHelperPayemnt()
    {
        //set Visible
        recycler_view_client_payment_helper.setVisibility(View.VISIBLE);
//        recycler_view_client_my_jobs_settings.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_client_payment_helper.measure(widthSpec, heightSpec);
//        recycler_view_client_my_jobs_settings.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorMyJobs(0, recycler_view_client_payment_helper.getMeasuredHeight());
//        ValueAnimator mAnimator1 = slideAnimatorMyJobs(0, recycler_view_client_my_jobs_settings.getMeasuredHeight());
        mAnimator.start();
//        mAnimator1.start();
    }


    private ValueAnimator slideAnimatorMyJobs(int start, int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = recycler_view_client_my_jobs.getLayoutParams();
                layoutParams.height = value;
                recycler_view_client_my_jobs.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }

    private void collapseMyJobs()
    {
        int finalHeight = recycler_view_client_my_jobs.getHeight();
        int finalHeight1 = recycler_view_client_my_jobs_settings.getHeight();

        ValueAnimator mAnimator = slideAnimatorMyJobs(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //Height=0, but it set visibility to GONE
                recycler_view_client_my_jobs.setVisibility(View.GONE);
                recycler_view_client_my_jobs_settings.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private void collapseMyJobsHelper()
    {
       int finalHeight = recycler_view_client_my_jobs_helper.getHeight();
//     int finalHeight1 = recycler_view_client_my_jobs_settings.getHeight();

        ValueAnimator mAnimator = slideAnimatorMyJobs(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                // Height=0, but it set visibility to GONE
                recycler_view_client_my_jobs_helper.setVisibility(View.GONE);
//              recycler_view_client_my_jobs_settings.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        mAnimator.start();
    }

    private void collapseMyJobsHelperInqueries() {
        int finalHeight = recycler_view_inqueires_helper.getHeight();
//        int finalHeight1 = recycler_view_client_my_jobs_settings.getHeight();

        ValueAnimator mAnimator = slideAnimatorMyJobs(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //Height=0, but it set visibility to GONE
                recycler_view_inqueires_helper.setVisibility(View.GONE);
//                recycler_view_client_my_jobs_settings.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }


    private void collapseMyJobsHelperPayment() {
        int finalHeight = recycler_view_client_payment_helper.getHeight();
//        int finalHeight1 = recycler_view_client_my_jobs_settings.getHeight();

        ValueAnimator mAnimator = slideAnimatorMyJobs(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                //Height=0, but it set visibility to GONE
                recycler_view_client_payment_helper.setVisibility(View.GONE);
//                recycler_view_client_my_jobs_settings.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }



    @OnClick(R.id.iv_arrow_inqueries)
    public void setIv_arrow_my_inqueries() {
        if (jobModelForClientListInqueries.size() == 0) {
            return;
        }
        if (rel_inqueries_outer.getVisibility() == View.VISIBLE) {
            iv_arrow_my_inqueries.setBackgroundResource(R.drawable.ic_down_arrow);
            rel_inqueries_outer.setVisibility(View.GONE);
            //collapseInquries();
        } else {
            iv_arrow_my_inqueries.setBackgroundResource(R.drawable.ic_arrow_up);
            rel_inqueries_outer.setVisibility(View.VISIBLE);

            //expandInquries();
        }
    }

    private void expandInquries() {
        //set Visible
        recycler_view_client_inqueries.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_client_inqueries.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorInquries(0, recycler_view_client_inqueries.getMeasuredHeight());
        mAnimator.start();
    }


    private ValueAnimator slideAnimatorInquries(int start, int end)
    {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = recycler_view_client_inqueries.getLayoutParams();
                layoutParams.height = value;
                recycler_view_client_inqueries.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void collapseInquries()
    {
        int finalHeight = recycler_view_client_inqueries.getHeight();
        ValueAnimator mAnimator = slideAnimatorInquries(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                recycler_view_client_inqueries.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    @OnClick(R.id.iv_arrow_payment)
    public void iv_arrow_payment()
    {
        if (paymentModelList.size() == 0)
        {
            return;
        }
        if (recycler_view_client_payment.getVisibility() == View.VISIBLE)
        {
            iv_arrow_payment.setBackgroundResource(R.drawable.ic_down_arrow);
            collapsePayments();
        }
        else
        {
            iv_arrow_payment.setBackgroundResource(R.drawable.ic_arrow_up);
            expandPayments();
        }
    }

    private void expandPayments()
    {
        //set Visible
        recycler_view_client_payment.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        recycler_view_client_payment.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimatorPayments(0, recycler_view_client_payment.getMeasuredHeight());
        mAnimator.start();
    }


    private ValueAnimator slideAnimatorPayments(int start, int end)
    {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = recycler_view_client_payment.getLayoutParams();
                layoutParams.height = value;
                recycler_view_client_payment.setLayoutParams(layoutParams);
                ExpandableAdapter listAdapter = new ExpandableAdapter(getActivity(), listDataHeader, listDataChild, MyJobsFragment.this);
                recycler_view_client_inqueries.setAdapter(listAdapter);
                recycler_view_client_inqueries.expandGroup(0);
            }
        });
        return animator;
    }

    private void collapsePayments()
    {
        int finalHeight = recycler_view_client_payment.getHeight();

        ValueAnimator mAnimator = slideAnimatorPayments(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                recycler_view_client_payment.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }


    @OnClick(R.id.ll_add_job)
    public void ll_add_job() {
        Intent intent = new Intent(getContext(), AddJobActivity.class);
        intent.putExtra("message", getResources().getString(R.string.add_job));
        intent.putExtra("job_id", "");
        intent.putExtra("position", "");
        startActivity(intent);
    }


    public void deleteJob(int position) {
        showDialogAlert(position, getResources().getString(R.string.delete_job), getResources().getString(R.string.delete_job_message));
    }

    private void showDialogAlert(int position, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.laucher_icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callDeleteJob(position);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
        });
        dialog.show();
    }

    public void callDeleteJob(int position){
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getContext(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job_id", jobModelForClientList.get(position).getId());
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.deleteJob(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        MyApplication.getInstance().hideProgress(getContext());
                        getMyJobs();
                        //MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    } else {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(getContext(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getContext());
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getContext());
            }
        });
    }

    public void editJob(int position) {
        Intent intent = new Intent(getContext(), AddJobActivity.class);
        intent.putExtra("message", getResources().getString(R.string.edit_job));
        intent.putExtra("job_id", jobModelForClientList.get(position).getId() + "");
        intent.putExtra("position", position + "");
        intent.putExtra("from", "list");
        startActivity(intent);
    }

    public void seeJobDetails(int position) {
        Intent intent = new Intent(getContext(), JobDetailActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("from", "client");
        startActivity(intent);
    }

    public static JobModelForClient.Applicants applicant;

    public  void seeDetails(JobModelForClient.Applicants applicants, String job_id) {
        applicant = applicants;
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra("job_id", job_id);
        startActivity(intent);
    }

    public void acceptRejectJob(String action, JobModelForClient.Applicants applicants)
    {
        if(action.equalsIgnoreCase("a"))
        {
            showDialogAlert(applicants, action, getResources().getString(R.string.accept_job),
                    getResources().getString(R.string.accept_job_message));
        }else if(action.equalsIgnoreCase("c"))
        {
            showDialogAlert(applicants, action, getResources().getString(R.string.complete_job),
                    getResources().getString(R.string.complete_job_message));
        }
        else
        {
            showDialogAlert(applicants, action, getResources().getString(R.string.reject_job),
                    getResources().getString(R.string.reject_job_message));
        }
    }
    public void completeJobsss(String action,JobModelForClient.Applicants applicants)
    {
        showDialogAlertForCompleteJob(applicants, getResources().getString(R.string.complete_job),action,
                getResources().getString(R.string.complete_job_message));
    }

    private void showDialogAlert(JobModelForClient.Applicants applicants, String action, String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.laucher_icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callAcceptRejectJob(applicants, action);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
        });
        dialog.show();
    }


    private void showDialogAlertForCompleteJob(JobModelForClient.Applicants applicants, String action,  String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.laucher_icon);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callCompleteJob(applicants,action);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
        });
        dialog.show();
    }


    public void callCompleteJob(JobModelForClient.Applicants applicants,String action) {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).
                    findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        String admin_app = "";
        if(action.equalsIgnoreCase("a")){
            admin_app = "1";
        }
        else{
            admin_app = "2";
        }
        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_job_id", applicants.getApplicant_id());
        jsonObject.addProperty("admin_approval", admin_app);
        Log.e("request....", String.valueOf(jsonObject));


        Observable<Response<ResponseBody>> api;

        api = apiInterface.completeJob(jsonObject,
                "Bearer " + MyApplication.getInstance().useString("user_access_token"));

        Observable<Response<ResponseBody>> observeApi = api
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {
                MyApplication.getInstance().hideProgress(getActivity());
                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        getMyJobs();
                    } else {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });

    }


    private void callAcceptRejectJob(JobModelForClient.Applicants applicants, String action) {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        String admin_app = "";
        if(action.equalsIgnoreCase("a"))
        {
            admin_app = "1";//for accept
        }else if(action.equalsIgnoreCase("c"))
        {
            admin_app = "3";//for complete
        }
        else
        {
            admin_app = "2";// for reject
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_job_id", applicants.getApplicant_id());
        jsonObject.addProperty("admin_approval", admin_app);
        Log.e("request....", String.valueOf(jsonObject));


        Observable<Response<ResponseBody>> api;

        if (action.equalsIgnoreCase("a")) {
            api = apiInterface.acceptJobOffer(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        } else {
            api = apiInterface.rejectJobOffer(jsonObject,"Bearer " + MyApplication.getInstance().useString("user_access_token"));
        }

        Observable<Response<ResponseBody>> observeApi = api
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ResponseBody> response) {
                MyApplication.getInstance().hideProgress(getActivity());
                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        getMyJobs();
                    } else {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(getActivity(), e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(getActivity());
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if(MyApplication.getInstance().useString("is_updated").equalsIgnoreCase("true")){
            MyApplication.getInstance().saveString("is_updated", "false");
            getMyJobs();
        }
    }
}
