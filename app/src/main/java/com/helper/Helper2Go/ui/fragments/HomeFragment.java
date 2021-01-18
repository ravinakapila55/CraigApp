package com.helper.Helper2Go.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.HomeScreenAdapter;
import com.helper.Helper2Go.custom.MySpinner;
import com.helper.Helper2Go.models.JobFilterParam;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.models.LoginResponse;
import com.helper.Helper2Go.models.NotificationModel;
import com.helper.Helper2Go.ui.HomeActivity;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.ui.LoginActivity;
import com.helper.Helper2Go.ui.NotificationActivity;
import com.helper.Helper2Go.utils.CommonMethods;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    public static List<JobModel> jobModelList = new ArrayList<>();
    public static List<String> job_duration = new ArrayList<>();
    public static List<JobFilterParam.NameModel> skills = new ArrayList<>();
    public static List<JobFilterParam.NameModel> tools = new ArrayList<>();
    public static String distance = "";


    LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */

    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;

    Location mLocation;

    @BindView(R.id.txt_no_record)
    TextView txt_no_record;


    int[] arra = {
            Color.rgb(41, 155, 92), Color.rgb(41, 155, 92),
            Color.rgb(237, 182, 11), Color.rgb(237, 182, 11),
            Color.rgb(237, 48, 65), Color.rgb(237, 48, 65),
            Color.rgb(30, 59, 84), Color.rgb(30, 59, 84),
            Color.rgb(255, 165, 0), Color.rgb(255, 165, 0),
            Color.rgb(25, 144, 218), Color.rgb(25, 144, 218),
            Color.rgb(218, 25, 179), Color.rgb(218, 25, 179)
    };

    View mRootView;

    @BindView(R.id.recycler_view_home)
    RecyclerView recycler_view_home;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        //Log.e("kjdkjkjkjf", MyApplication.current_lat + ", " + MyApplication.current_lon);
        getJobs(false);

        //int arra[] = {R.color.card_green, R.color.card_green, R.color.card_yellow, R.color.card_yellow, R.color.card_red, R.color.card_red};
        return mRootView;
    }


    @OnClick(R.id.fab_but_home)
    public void fab_but_home() {
        replaceContentFragment(new HomeFragmentSecond());
    }

    private void replaceContentFragment(Fragment fragment) {
        if (!isAdded())
            return;
        ((HomeMainActivity) getActivity()).replaceFragment(fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        //onCreateAnimation(0, true, 0);
    }

    @OnClick(R.id.iv_notification)
    public void iv_notification() {
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        startActivity(intent);
    }

    public void openUserDetail(int position) {
        DialogFragment fragment = ViewPersonDetailFragment.newInstance(position, "h");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.show(fragmentManager, fragment.getClass().getSimpleName());
    }

    String typeFilter="";
    public void getJobs(boolean isFilter) {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_lat", MyApplication.current_lat);
        jsonObject.addProperty("user_lng", MyApplication.current_lon);
        jsonObject.addProperty("type",typeFilter);

        if (typeFilter.equalsIgnoreCase("1"))
        {
            jsonObject.addProperty("distance", HomeMainActivity.selected_distance);
            jsonObject.addProperty("max_distance", HomeMainActivity.selected_max_distance);
            jsonObject.addProperty("end_job_duration", HomeMainActivity.end_duration);
            jsonObject.addProperty("job_duration", HomeMainActivity.selected_duration);
        }
        else  if (typeFilter.equalsIgnoreCase("2"))
        {
            jsonObject.addProperty("end_job_duration", HomeMainActivity.end_duration);
            jsonObject.addProperty("job_duration", HomeMainActivity.selected_duration);
        }
        else  if (typeFilter.equalsIgnoreCase("3"))
        {
            jsonObject.addProperty("distance", HomeMainActivity.selected_distance);
            jsonObject.addProperty("max_distance", HomeMainActivity.selected_max_distance);
        }

        Log.e("requestParams....", String.valueOf(jsonObject));






        Observable<Response<ResponseBody>> api;

        if (isFilter) {
            api = apiInterface.getJobs(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        } else {
            api = apiInterface.getAllJobs("Bearer " + MyApplication.getInstance().useString("user_access_token"));
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

                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        if (jobModelList.size() != 0) {
                            jobModelList.clear();
                        }
                        jobModelList = generalResponse.getDataAsList("result", JobModel.class);
                        JobFilterParam jobFilterParam = generalResponse.getJSONObjectAs("params", JobFilterParam.class);
                        if (job_duration.size() != 0) {
                            job_duration.clear();
                        }
                        if (skills.size() != 0) {
                            skills.clear();
                        }
                        if (tools.size() != 0) {
                            tools.clear();
                        }

                        job_duration = jobFilterParam.getJob_duration();
                        skills = jobFilterParam.getSkills();
                        tools = jobFilterParam.getTools();

                        distance = jobFilterParam.getDistance();

                        List<Integer> myin = new ArrayList<>();

                        int count = 0;

                        for (int i = 0; i < jobModelList.size(); i++) {
                            if (jobModelList.size() > arra.length) {
                                if (count == arra.length) {
                                    count = 0;
                                }
                            }
                            myin.add(arra[count]);
                            count++;
                        }


                        Integer[] new_color_arr = myin.toArray(new Integer[myin.size()]);
                        recycler_view_home.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycler_view_home.setAdapter(new HomeScreenAdapter(getContext(), new_color_arr,
                                HomeFragment.this, jobModelList, "h"));



                        if(jobModelList.size() != 0){
                            recycler_view_home.setVisibility(View.VISIBLE);
                            txt_no_record.setVisibility(View.GONE);
                        }
                        else{
                            recycler_view_home.setVisibility(View.GONE);
                            txt_no_record.setVisibility(View.VISIBLE);
                        }

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
    int mYear, mMonth, mDay;

    String dates = "";
    String Startdates = "";


    public void showDialogForApplyFilter(Context activity) {
        final Dialog dialog = new Dialog(activity);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_filter);

        MySpinner spinner_job_duration = dialog.findViewById(R.id.spinner_job_duration);
        MySpinner spinner_skills = dialog.findViewById(R.id.spinner_skills);
        MySpinner spinner_select_tools = dialog.findViewById(R.id.spinner_select_tools);
        TextView txt_distance = dialog.findViewById(R.id.txt_distance);
        EditText edt_distance = dialog.findViewById(R.id.edt_distance);
        EditText edt_max_distance = dialog.findViewById(R.id.edt_max_distance);
        RelativeLayout rel_apply = dialog.findViewById(R.id.rel_apply);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);

//        txt_distance.setText(getResources().getString(R.string.enter_distane) + " (" + distance + ")");
//        edt_distance.setText(HomeMainActivity.selected_distance);
//        edt_distance.setSelection(edt_distance.getText().length());


        TextView edt_duration = dialog.findViewById(R.id.edt_duration);
        TextView edt_end_duration = dialog.findViewById(R.id.edt_end_duration);

     /*   edt_duration.setText(HomeMainActivity.selected_duration);
        edt_end_duration.setText(HomeMainActivity.end_duration);*/


        edt_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                if (edt_duration.getText().toString().equalsIgnoreCase("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {

                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-dd-MM");
                    String inputDateStr = edt_duration.getText().toString();
                    Date dates = null;
                    try {
                        dates = inputFormat.parse(inputDateStr);
                    } catch (ParseException e) {
                    }
                    String outputDateStr = outputFormat.format(dates);

                    String date[] = outputDateStr.split("-");
                    mYear = Integer.parseInt(date[0]);
                    mMonth = Integer.parseInt(date[2]) - 1;
                    mDay = Integer.parseInt(date[1]);
                }

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;

                                if (month < 10 && dayOfMonth < 10) {
                                    dates = year + "-" + String.format("0%s", dayOfMonth) + "-" + String.format("0%s", (month));
                                } else if (month < 10) {
                                    dates = year + "-" + dayOfMonth + "-" + String.format("0%s", (month));
                                } else if (dayOfMonth < 10) {
                                    dates = year + "-" + String.format("0%s", dayOfMonth) + "-" + (month);
                                } else {
                                    dates = year + "-" + dayOfMonth + "-" + (month);
                                }

                                // 1999-29-05

                                String date[] = dates.split("-");
                                String finalDate = date[0] + "-" + date[2] + "-" + date[1];

                                DateFormat inputFormat = new SimpleDateFormat("yyyy-dd-MM");
                                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String inputDateStr = dates;
                                Date dates = null;
                                try {
                                    dates = inputFormat.parse(inputDateStr);
                                } catch (ParseException e) {
                                }
                                String outputDateStr = outputFormat.format(dates);

                                Startdates=outputDateStr;
                                edt_duration.setText(outputDateStr);
                                HomeMainActivity.selected_duration = outputDateStr;

                            }
                        }, mYear, mMonth, mDay);
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }
        });


        edt_end_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                if (edt_end_duration.getText().toString().equalsIgnoreCase("")) {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                }
                else if (edt_duration.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(activity, "Please choose start date first", Toast.LENGTH_SHORT).show();
                }
                else {

                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-dd-MM");
                    String inputDateStr = edt_end_duration.getText().toString();
                    Date dates = null;
                    try {
                        dates = inputFormat.parse(inputDateStr);
                    } catch (ParseException e) {
                    }
                    String outputDateStr = outputFormat.format(dates);

                    String date[] = outputDateStr.split("-");
                    mYear = Integer.parseInt(date[0]);
                    mMonth = Integer.parseInt(date[2]) - 1;
                    mDay = Integer.parseInt(date[1]);
                }

                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;

                                if (month < 10 && dayOfMonth < 10) {
                                    dates = year + "-" + String.format("0%s", dayOfMonth) + "-" + String.format("0%s", (month));
                                } else if (month < 10) {
                                    dates = year + "-" + dayOfMonth + "-" + String.format("0%s", (month));
                                } else if (dayOfMonth < 10) {
                                    dates = year + "-" + String.format("0%s", dayOfMonth) + "-" + (month);
                                } else {
                                    dates = year + "-" + dayOfMonth + "-" + (month);
                                }

                                // 1999-29-05

                                String date[] = dates.split("-");
                                String finalDate = date[0] + "-" + date[2] + "-" + date[1];

                                DateFormat inputFormat = new SimpleDateFormat("yyyy-dd-MM");
                                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String inputDateStr = dates;
                                Date dates = null;
                                try {
                                    dates = inputFormat.parse(inputDateStr);
                                } catch (ParseException e) {
                                }
                                String outputDateStr = outputFormat.format(dates);

                                String compareDates="";
                                compareDates= CommonMethods.compareDates(Startdates,outputDateStr);


                                if (compareDates.equalsIgnoreCase("equal") || compareDates.equalsIgnoreCase("after"))
                                {
                                    Toast.makeText(getActivity(), "Please choose end duration after start duration", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    edt_end_duration.setText(outputDateStr);
                                    HomeMainActivity.end_duration = outputDateStr;
                                }
                            }
                        }, mYear, mMonth, mDay);
                //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.setTitle("");
                datePickerDialog.show();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final ArrayAdapter<String> spinnerArrayAdapterForJobDuration = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner, android.R.id.text1, job_duration);

        spinnerArrayAdapterForJobDuration.setDropDownViewResource(R.layout.spinner);
        spinner_job_duration.setAdapter(spinnerArrayAdapterForJobDuration);
        spinnerArrayAdapterForJobDuration.notifyDataSetChanged();
        //nice_spinner_device.setOnItemSelectedListener(this);

        spinner_job_duration.setSelection(spinnerArrayAdapterForJobDuration.getPosition(HomeMainActivity.selected_duration));

        spinner_job_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HomeMainActivity.selected_duration = job_duration.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> new_skills = new ArrayList<>();
        for(int i = 0; i < skills.size(); i++){
            new_skills.add(skills.get(i).getName());
        }

        final ArrayAdapter<String> spinnerArrayAdapterForSkills = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner, android.R.id.text1, new_skills);

        spinnerArrayAdapterForSkills.setDropDownViewResource(R.layout.spinner);
        spinner_skills.setAdapter(spinnerArrayAdapterForSkills);
        spinnerArrayAdapterForSkills.notifyDataSetChanged();
        //nice_spinner_device.setOnItemSelectedListener(this);

        spinner_skills.setSelection(spinnerArrayAdapterForSkills.getPosition(HomeMainActivity.selected_skill));

        spinner_skills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HomeMainActivity.selected_skill = String.valueOf(skills.get(position).getId());
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //setMeterListSpinner(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> new_tools = new ArrayList<>();
        for(int i = 0; i < tools.size(); i++){
            new_tools.add(tools.get(i).getName());
        }

        final ArrayAdapter<String> spinnerArrayAdapterForTools = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner, android.R.id.text1, new_tools);

        spinnerArrayAdapterForTools.setDropDownViewResource(R.layout.spinner);
        spinner_select_tools.setAdapter(spinnerArrayAdapterForTools);
        spinnerArrayAdapterForTools.notifyDataSetChanged();
        //nice_spinner_device.setOnItemSelectedListener(this);
        spinner_select_tools.setSelection(spinnerArrayAdapterForTools.getPosition(HomeMainActivity.selected_tool));
        spinner_select_tools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HomeMainActivity.selected_tool = String.valueOf(tools.get(position).getId());
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //setMeterListSpinner(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rel_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int startDistance=0;
                int maxDistance=0;



                if (edt_duration.getText().toString().equalsIgnoreCase("")&&  edt_distance.getText().toString().equalsIgnoreCase(""))
                {
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                            "Please enter either distance or duration ", getActivity());
                    return;
                }

                else if (!edt_duration.getText().toString().equalsIgnoreCase("") && edt_distance.getText().toString().trim().isEmpty())
                {
                   /* MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                            getResources().getString(R.string.empty_job_start_date), getActivity());*/

                    if (edt_end_duration.getText().toString().equalsIgnoreCase(""))
                    {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                                "Please enter end date job duration", getActivity());
                        return;
                    }


                    else
                    {

                        typeFilter="2";
                        HomeMainActivity.selected_distance = edt_distance.getText().toString();
                        HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                        dialog.dismiss();
                        getJobs(true);

                    }


                }

                else  if (!edt_distance.getText().toString().equalsIgnoreCase("") && edt_duration.getText().toString().trim().isEmpty())
                {
                   /* MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                            getResources().getString(R.string.please_enter_distance), getActivity());
                    return;*/
                    if (edt_max_distance.getText().toString().equalsIgnoreCase("")) {
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
                                "Please enter maximum distance", getActivity());
                        return;
                    }

                    startDistance=Integer.parseInt(edt_distance.getText().toString().trim());
                    maxDistance=Integer.parseInt(edt_max_distance.getText().toString().trim());

                    if (maxDistance<startDistance)
                    {
                        Toast.makeText(activity, "Maximum distance must be greater than start distance", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    typeFilter="3";


                    HomeMainActivity.selected_distance = edt_distance.getText().toString();
                    HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                    dialog.dismiss();
                    getJobs(true);

                }

                else if (!edt_duration.getText().toString().trim().isEmpty() && !edt_end_duration.getText().toString().trim().isEmpty()&&
                        !edt_distance.getText().toString().trim().isEmpty() && !edt_max_distance.getText().toString().trim().isEmpty())
                {
                    typeFilter="1";


                    HomeMainActivity.selected_distance = edt_distance.getText().toString();
                    HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                    dialog.dismiss();
                    getJobs(true);
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.iv_filter)
    public void iv_filter() {
        showDialogForApplyFilter(getActivity());
    }


    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    LocationManager locationManager;

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    protected void requestPermission(String permissionType,
                                     int requestCode) {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{permissionType}, requestCode
        );
    }



    @Override
    public void onPause() {
        //mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    double current_lat;
    double current_lon;


    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        current_lat = location.getLatitude();
        current_lon = location.getLongitude();

        MyApplication.current_lat = String.valueOf(current_lat);
        MyApplication.current_lon = String.valueOf(current_lon);

        Log.e("kjdkjkjkjf", MyApplication.current_lat + ", " + MyApplication.current_lon);
    }
}
