package com.helper.Helper2Go.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.helper.Helper2Go.ui.HomeActivity;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.ui.NotificationActivity;
import com.helper.Helper2Go.utils.CommonMethods;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;
import com.labo.kaji.fragmentanimations.FlipAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.tristate.radarview.LatLongCs;
import com.tristate.radarview.ObjectModel;
import com.tristate.radarview.RadarViewC;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragmentSecond extends Fragment {
   View mRootView;

   int distance = 100;
   String dis = "";
    ArrayList<ObjectModel> mDataSet = new ArrayList<>();

    String[] arra = {
            "#299b5c", "#299b5c",
            "#edb60b", "#edb60b",
            "#ed3041", "#ed3041",
            "#1e3b54", "#1e3b54",
            "#FFA500", "#FFA500",
            "#1990da", "#1990da",
            "#da19b3", "#da19b3"
    };


    RadarViewC mRadarCustom;
    public HomeFragmentSecond() {
        // Required empty public constructor
    }

    public static HomeFragmentSecond newInstance(String param1, String param2) {
        HomeFragmentSecond fragment = new HomeFragmentSecond();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_home_fragment_second, container, false);
        tvDistanceSet=(TextView)mRootView.findViewById(R.id.tvDistanceSet);
        ButterKnife.bind(this, mRootView);

        if(mDataSet.size() != 0){
            mDataSet.clear();
        }

        animateRadar();

        mRadarCustom= mRootView.findViewById(R.id.mRadarCustom);

        LatLongCs latLongCs = new LatLongCs(30.7046, 76.7179);

        TextView mCenterView = new TextView(getActivity());
        mCenterView.setText("");

        List<String> myin=new ArrayList<>();

        int count = 0;

        for(int i = 0; i < HomeFragment.jobModelList.size(); i++){
            if(HomeFragment.jobModelList.size() > arra.length) {
                if (count == arra.length) {
                    count = 0;
                }
            }
            myin.add(arra[count]);
            count++;
        }

        String[] new_color_arr = myin.toArray(new String[myin.size()]);


        /*if(HomeFragment.jobModelList.size() > 15){
            for (int i = 0; i < 15; i++) {
                JobModel jobModel = HomeFragment.jobModelList.get(i);
                makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                        Double.parseDouble(jobModel.getLongitude()),
                        distance,
                        Color.RED,
                        new_color_arr[i]);
                distance = distance + 100;
            }
        }
        else {

            for (int i = 0; i < HomeFragment.jobModelList.size(); i++) {
                JobModel jobModel = HomeFragment.jobModelList.get(i);
                makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                        Double.parseDouble(jobModel.getLongitude()),
                        distance,
                        Color.RED,
                        new_color_arr[i]);
                distance = distance + 100;
            }
        }*/


        for (int i = 0; i < HomeFragment.jobModelList.size(); i++) {
            JobModel jobModel = HomeFragment.jobModelList.get(i);
            makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                    Double.parseDouble(jobModel.getLongitude()),
                    distance,
                    Color.RED,
                    new_color_arr[i]);
            distance = distance + 100;
        }
       /* makeRadarPoints(23.070390, 72.519176, 200, Color.RED, "#299b5c");
        makeRadarPoints(23.071559, 72.516494, 150, Color.BLACK, "#ed3041");
        makeRadarPoints(23.069906, 72.515504, 150, Color.BLUE, "#1e3b54");*/
        //makeRadarPoints(23.069608, 72.516477, 150, Color.GREEN);
       // makeRadarPoints(23.069213, 72.517739, 100, Color.CYAN);

        mRadarCustom.setupData(300000, mDataSet, latLongCs, mCenterView);

        mRadarCustom.setUpCallBack(new RadarViewC.IRadarCallBack() {
            @Override
            public void onViewClick(Object objectModel, View view) {

            }
        });
        return mRootView;
    }

    private void animateRadar() {
        ImageView mImgRadarBack = mRootView.findViewById(R.id.mImgRadarBack);
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        rotation.setFillAfter(true);
        mImgRadarBack.startAnimation(rotation);
    }

    @OnClick(R.id.fab_but_home)
    public void fab_but_home(){
        replaceContentFragment(new HomeTutorialFragment());
    }

    private void replaceContentFragment(Fragment fragment) {
        if (!isAdded())
            return;
        HomeActivity.flipAnimationFullCardFragment = true;

        ((HomeMainActivity) getActivity()).replaceFragmentFromBack(fragment);

    }

    @OnClick(R.id.iv_notification)
    public void iv_notification(){
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        startActivity(intent);
    }


    public void makeRadarPoints(double lat, double lng, double distance, int color, String hexcode){

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.radar_user_item, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.ivLocation);
        ImageView llLocation = customMarkerView.findViewById(R.id.llLocation);

        DrawableCompat.setTint(llLocation.getDrawable(), Color.parseColor(hexcode)); // set color from hexcode
        //DrawableCompat.setTint(llLocation.getDrawable(), color); // Set color using Color.RED
      /*  byte[] decodedStrings = Base64.decode(geoTagModelList.get(i).getTag_image(), Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedStrings, 0, decodedStrings.length);*/
        markerImageView.setImageResource(R.drawable.user_img);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageBitmap(returnedBitmap);

        mDataSet.add(new ObjectModel(lat, lng, distance, imageView));
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }



    public void showDialogForApplyFilter(Context activity){
        final Dialog dialog = new Dialog(activity);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_filter);

        MySpinner spinner_job_duration  = dialog.findViewById(R.id.spinner_job_duration);
        MySpinner spinner_skills  = dialog.findViewById(R.id.spinner_skills);
        MySpinner spinner_select_tools  = dialog.findViewById(R.id.spinner_select_tools);
        TextView txt_distance  = dialog.findViewById(R.id.txt_distance);
        EditText edt_distance = dialog.findViewById(R.id.edt_distance);
        EditText edt_max_distance = dialog.findViewById(R.id.edt_max_distance);
        RelativeLayout rel_apply = dialog.findViewById(R.id.rel_apply);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);

//        txt_distance.setText(getResources().getString(R.string.enter_distane) + " (" + dis + ")");
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
                }   else if (edt_duration.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(activity, "Please choose start date first", Toast.LENGTH_SHORT).show();
                } else {

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
                getActivity(), R.layout.spinner, android.R.id.text1, HomeFragment.job_duration);

        spinnerArrayAdapterForJobDuration.setDropDownViewResource(R.layout.spinner);
        spinner_job_duration.setAdapter(spinnerArrayAdapterForJobDuration);
        spinnerArrayAdapterForJobDuration.notifyDataSetChanged();
        //nice_spinner_device.setOnItemSelectedListener(this);

        spinner_job_duration.setSelection(spinnerArrayAdapterForJobDuration.getPosition(HomeMainActivity.selected_duration));

        spinner_job_duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HomeMainActivity.selected_duration = HomeFragment.job_duration.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List<String> new_skills = new ArrayList<>();
        for(int i = 0; i < HomeFragment.skills.size(); i++){
            new_skills.add(HomeFragment.skills.get(i).getName());
        }

        final ArrayAdapter<String> spinnerArrayAdapterForSkills = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner, android.R.id.text1, new_skills);

        spinnerArrayAdapterForSkills.setDropDownViewResource(R.layout.spinner);
        spinner_skills.setAdapter(spinnerArrayAdapterForSkills);
        spinnerArrayAdapterForSkills.notifyDataSetChanged();
        //nice_spinner_device.setOnItemSelectedListener(this);

        spinner_skills.setSelection(spinnerArrayAdapterForSkills.getPosition(HomeMainActivity.selected_skill));

        spinner_skills.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                HomeMainActivity.selected_skill = String.valueOf(HomeFragment.skills.get(position).getId());
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //setMeterListSpinner(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> new_tools = new ArrayList<>();
        for(int i = 0; i < HomeFragment.tools.size(); i++){
            new_tools.add(HomeFragment.tools.get(i).getName());
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
                HomeMainActivity.selected_tool = String.valueOf(HomeFragment.tools.get(position).getId());
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
                        tvDistanceSet.setVisibility(View.GONE);
                        HomeMainActivity.selected_distance = edt_distance.getText().toString();
                        HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                        dialog.dismiss();
                        getJobs();

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
                    tvDistanceSet.setVisibility(View.VISIBLE);
                    tvDistanceSet.setText(edt_distance.getText().toString().trim()+"-"+edt_max_distance.getText().toString().trim()+" Km");

                    HomeMainActivity.selected_distance = edt_distance.getText().toString();
                    HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                    dialog.dismiss();
                    getJobs();

                }

                else if (!edt_duration.getText().toString().trim().isEmpty() && !edt_end_duration.getText().toString().trim().isEmpty()&&
                        !edt_distance.getText().toString().trim().isEmpty() && !edt_max_distance.getText().toString().trim().isEmpty())
                {
                    typeFilter="1";
                    tvDistanceSet.setVisibility(View.VISIBLE);
                    tvDistanceSet.setText(edt_distance.getText().toString().trim()+"-"+edt_max_distance.getText().toString().trim()+" Km");

                    HomeMainActivity.selected_distance = edt_distance.getText().toString();
                    HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                    dialog.dismiss();
                    getJobs();
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

    int mYear, mMonth, mDay;
    String dates = "";
    String Startdates = "";

    @OnClick(R.id.iv_filter)
    public void iv_filter()
    {
        showDialogForApplyFilter(getActivity());
    }

    String typeFilter = "";
    TextView tvDistanceSet;

    public void getJobs(){
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

        Observable<Response<ResponseBody>> observeApi = apiInterface.getJobs(jsonObject, "Bearer " +
                MyApplication.getInstance().useString("user_access_token"))
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
                        distance = 100;
                        if(mDataSet.size() != 0){
                            mDataSet.clear();
                        }
                        if(HomeFragment.jobModelList.size() != 0){
                            HomeFragment.jobModelList.clear();
                        }
                        HomeFragment.jobModelList = generalResponse.getDataAsList("result", JobModel.class);
                        JobFilterParam jobFilterParam = generalResponse.getJSONObjectAs("params", JobFilterParam.class);
                        if(HomeFragment.job_duration.size() != 0){
                            HomeFragment.job_duration.clear();
                        }
                        if(HomeFragment.skills.size() != 0){
                            HomeFragment.skills.clear();
                        }
                        if(HomeFragment.tools.size() != 0){
                            HomeFragment.tools.clear();
                        }
                        dis = jobFilterParam.getDistance();

                        HomeFragment.job_duration = jobFilterParam.getJob_duration();
                        HomeFragment.skills = jobFilterParam.getSkills();
                        HomeFragment.tools = jobFilterParam.getTools();

                        LatLongCs latLongCs = new LatLongCs(30.7046, 76.7179);

                        TextView mCenterView = new TextView(getActivity());
                        mCenterView.setText("");


                        List<String> myin=new ArrayList<>();

                        int count = 0;

                        for(int i = 0; i < HomeFragment.jobModelList.size(); i++){
                            if(HomeFragment.jobModelList.size() > arra.length) {
                                if (count == arra.length) {
                                    count = 0;
                                }
                            }
                            myin.add(arra[count]);
                            count++;
                        }

                        String[] new_color_arr = myin.toArray(new String[myin.size()]);


                        for(int i = 0; i < HomeFragment.jobModelList.size(); i++){
                            JobModel jobModel = HomeFragment.jobModelList.get(i);
                            makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                                    Double.parseDouble(jobModel.getLongitude()),
                                    distance,
                                    Color.RED,
                                    new_color_arr[i]);
                            distance = distance + 100;
                        }

       /* makeRadarPoints(23.070390, 72.519176, 200, Color.RED, "#299b5c");
        makeRadarPoints(23.071559, 72.516494, 150, Color.BLACK, "#ed3041");
        makeRadarPoints(23.069906, 72.515504, 150, Color.BLUE, "#1e3b54");*/
                        //makeRadarPoints(23.069608, 72.516477, 150, Color.GREEN);
                        // makeRadarPoints(23.069213, 72.517739, 100, Color.CYAN);

                        mRadarCustom.removeAllViews();

                        mRadarCustom.setupData(300000, mDataSet, latLongCs, mCenterView);


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

}
