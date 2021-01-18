package com.helper.Helper2Go.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
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

public class SearchFragmentSecond extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

    LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 20 sec */
    GoogleApiClient mGoogleApiClient;
    LocationManager mLocationManager;
    Location mLocation;
    boolean isFirst = false;
    int distance = 100;
    String dis = "";
    ArrayList<ObjectModel> mDataSet = new ArrayList<>();
    TextView tvDistanceSet;

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

    View mRootView;

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3*1000; //Delay for 3 seconds.  One second = 1000 milliseconds.


    public SearchFragmentSecond()
    {
        // Required empty public constructor
    }

    public static SearchFragmentSecond newInstance(String param1, String param2)
    {
        SearchFragmentSecond fragment = new SearchFragmentSecond();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mDataSet.size() != 0){
            mDataSet.clear();
        }

        animateRadar();

        mRadarCustom= mRootView.findViewById(R.id.mRadarCustom);

        getJobs(false);


        mRadarCustom.setUpCallBack(new RadarViewC.IRadarCallBack() {
            @Override
            public void onViewClick(Object objectModel, View view) {

            }
        });

      /*  handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                getJobs(false);
                handler.postDelayed(runnable, delay);
            }
        }, delay);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_search_fragment_second, container, false);
        ButterKnife.bind(this, mRootView);
        tvDistanceSet=(TextView)mRootView.findViewById(R.id.tvDistanceSet);
        tvDistanceSet.setVisibility(View.GONE);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        checkLocation();

     /*   if(mDataSet.size() != 0){
            mDataSet.clear();
        }

        animateRadar();

        mRadarCustom= mRootView.findViewById(R.id.mRadarCustom);

        getJobs(false);

      *//*  handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                getJobs(false);
                handler.postDelayed(runnable, delay);
            }
        }, delay);*/

        /*LatLongCs latLongCs = new LatLongCs(30.7046, 76.7179);

        TextView mCenterView = new TextView(getActivity());
        mCenterView.setText("");

        List<String> myin=new ArrayList<>();

        int count = 0;

        for(int i = 0; i < SearchFragment.jobModelList.size(); i++){
            if(SearchFragment.jobModelList.size() > arra.length) {
                if (count == arra.length) {
                    count = 0;
                }
            }
            myin.add(arra[count]);
            count++;
        }

        String[] new_color_arr = myin.toArray(new String[myin.size()]);

        for (int i = 0; i < SearchFragment.jobModelList.size(); i++) {
            JobModel jobModel = SearchFragment.jobModelList.get(i);
            makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                    Double.parseDouble(jobModel.getLongitude()),
                    distance,
                    Color.RED,
                    new_color_arr[i]);
            distance = distance + 100;
        }*/
       /* makeRadarPoints(23.070390, 72.519176, 200, Color.RED, "#299b5c");
        makeRadarPoints(23.071559, 72.516494, 150, Color.BLACK, "#ed3041");
        makeRadarPoints(23.069906, 72.515504, 150, Color.BLUE, "#1e3b54");*/
        //makeRadarPoints(23.069608, 72.516477, 150, Color.GREEN);
        // makeRadarPoints(23.069213, 72.517739, 100, Color.CYAN);

        //mRadarCustom.setupData(300000, mDataSet, latLongCs, mCenterView);

   /*     mRadarCustom.setUpCallBack(new RadarViewC.IRadarCallBack() {
            @Override
            public void onViewClick(Object objectModel, View view) {

            }
        });*/

        return mRootView;
    }

    private void animateRadar()
    {
        ImageView mImgRadarBack = mRootView.findViewById(R.id.mImgRadarBack);
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        rotation.setFillAfter(true);
        mImgRadarBack.startAnimation(rotation);
    }

    @OnClick(R.id.fab_but_home)
    public void fab_but_home()
    {
        HomeMainActivity.is_search = true;
        replaceContentFragment(new SearchFragment());
    }

    private void replaceContentFragment(Fragment fragment)
    {
        if (!isAdded())
            return;
        HomeActivity.flipAnimationFullCardFragment = true;

        ((HomeMainActivity) getActivity()).replaceFragmentFromBack(fragment);

    }

    @OnClick(R.id.iv_notification)
    public void iv_notification()
    {
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        startActivity(intent);
    }


    public void makeRadarPoints(double lat, double lng, double distance, int color, String hexcode)
    {
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

        double dis = distance(Double.parseDouble(MyApplication.current_lat), Double.parseDouble(MyApplication.current_lon),lat, lng);
        Log.e("insidemrkerRadarClass ",dis+"");
        mDataSet.add(new ObjectModel(lat, lng, dis, imageView));
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
        handler.removeCallbacks(runnable);
    }

    int mYear, mMonth, mDay;

    String dates = "";
    String Startdates = "";
    String distSet = "";
    String typeFilter = "";

    public void showDialogForApplyFilter(Context activity){
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

        TextView edt_duration = dialog.findViewById(R.id.edt_duration);
        TextView edt_end_duration = dialog.findViewById(R.id.edt_end_duration);

//        edt_duration.setText(HomeMainActivity.selected_duration);

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
            public void onClick(View view)
            {
                final Calendar c = Calendar.getInstance();

                if (edt_end_duration.getText().toString().equalsIgnoreCase(""))
                {
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

                    try
                    {
                        dates = inputFormat.parse(inputDateStr);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
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


/*        txt_distance.setText(getResources().getString(R.string.enter_distane) + " (" + distance + ")");
        edt_distance.setText(HomeMainActivity.selected_distance);
        edt_distance.setSelection(edt_distance.getText().length());*/

        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                    tvDistanceSet.setVisibility(View.VISIBLE);
                    tvDistanceSet.setText(edt_distance.getText().toString().trim()+"-"+edt_max_distance.getText().toString().trim()+" Km");

                    HomeMainActivity.selected_distance = edt_distance.getText().toString();
                    HomeMainActivity.selected_max_distance = edt_max_distance.getText().toString();
                    dialog.dismiss();
                    getJobs(true);

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
                    getJobs(true);
                }

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.iv_filter)
    public void iv_filter()
    {
        showDialogForApplyFilter(getActivity());
    }


    public void getJobs(boolean isFilter)
    {
        if (!NetworkUtils.isNetworkAvailable(getActivity()))
        {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
            getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();

        if(isFilter)
        {
            MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        }

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

        if (isFilter)
        {
            api = apiInterface.getJobs(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        }
        else
        {
            api = apiInterface.getAllJobs("Bearer " + MyApplication.getInstance().useString("user_access_token"));
        }

     Observable<Response<ResponseBody>> observeApi = api
     .subscribeOn(Schedulers.io())
     .observeOn(AndroidSchedulers.mainThread());

    observeApi.subscribe(new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d)
            {

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
                        if(SearchFragment.jobModelList.size() != 0){
                            SearchFragment.jobModelList.clear();
                        }
                        SearchFragment.jobModelList = generalResponse.getDataAsList("result", JobModel.class);
                        JobFilterParam jobFilterParam = generalResponse.getJSONObjectAs("params", JobFilterParam.class);
                        if(SearchFragment.job_duration.size() != 0){
                            SearchFragment.job_duration.clear();
                        }
                        if(SearchFragment.skills.size() != 0){
                            SearchFragment.skills.clear();
                        }
                        if(SearchFragment.tools.size() != 0){
                            SearchFragment.tools.clear();
                        }
                        dis = jobFilterParam.getDistance();

                        SearchFragment.job_duration = jobFilterParam.getJob_duration();
                        SearchFragment.skills = jobFilterParam.getSkills();
                        SearchFragment.tools = jobFilterParam.getTools();

                        LatLongCs latLongCs = new LatLongCs(Double.parseDouble(MyApplication.current_lat),
                                Double.parseDouble(MyApplication.current_lon));

                        TextView mCenterView = new TextView(getActivity());
                        mCenterView.setText("");


                        List<String> myin=new ArrayList<>();

                        int count = 0;

                        for(int i = 0; i < SearchFragment.jobModelList.size(); i++)
                        {
                            if(SearchFragment.jobModelList.size() > arra.length) {
                                if (count == arra.length) {
                                    count = 0;
                                }
                            }
                            myin.add(arra[count]);
                            count++;
                        }

                        String[] new_color_arr = myin.toArray(new String[myin.size()]);

                        Log.e("ListSize ",SearchFragment.jobModelList.size()+"");
                        Log.e("distance ",distance+"");
                        Log.e("ListSizeData ",SearchFragment.jobModelList.toString()+"");

       for(int i = 0; i < SearchFragment.jobModelList.size(); i++)
                        {
                            JobModel jobModel = SearchFragment.jobModelList.get(i);
                            Log.e("Lattitude ",jobModel.getLatitude()+"");
                            Log.e("Longitude ",jobModel.getLongitude()+"");
                            Log.e("LongitudeID ",jobModel.getId()+"");

                            Log.e("insidemrkerRadar ",jobModel.getId()+"");
                            makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                                    Double.parseDouble(jobModel.getLongitude()),
                                    distance,
                                    Color.RED,
                                    new_color_arr[i]);
                            distance = distance + 100;


                            if (!jobModel.getLatitude().equalsIgnoreCase(null))
                            {
                              Log.e("insidemrkerRadar ",jobModel.getId()+"");
                              makeRadarPoints(Double.parseDouble(jobModel.getLatitude()),
                              Double.parseDouble(jobModel.getLongitude()),
                              distance,
                              Color.RED,
                              new_color_arr[i]);
                              distance = distance + 100;
                            }
                            else
                            {
                               Log.e("nulll ","");
                            }
                        }
                        Log.e("distanceOutside ",distance+"");

     /*   makeRadarPoints(23.070390, 72.519176, 200, Color.RED, "#299b5c");
         makeRadarPoints(23.071559, 72.516494, 150, Color.BLACK, "#ed3041");
         makeRadarPoints(23.069906, 72.515504, 150, Color.BLUE, "#1e3b54");*/

        // makeRadarPoints(23.069608, 72.516477, 150, Color.GREEN);
        // makeRadarPoints(23.069213, 72.517739, 100, Color.CYAN);

                        mRadarCustom.removeAllViews();

                        if(isFilter)
                        {
                            Log.e("insideIFFilter ","");
                            mRadarCustom.setupData(Double.parseDouble(HomeMainActivity.selected_distance)*1000,
                                    mDataSet, latLongCs, mCenterView);

//                            mRadarCustom.setupData(50000, mDataSet, latLongCs, mCenterView);

                        }
                        else
                        {
                            Log.e("insideElseFilter ","");
                            mRadarCustom.setupData(50000, mDataSet, latLongCs, mCenterView);
                        }
                    }
                    else
                     {
                        MyApplication.getInstance().
                                displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content),
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

    private boolean checkLocation()
    {
        if (!isLocationEnabled())
          showAlert();
        return isLocationEnabled();
    }

    private void showAlert()
    {
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

    private boolean isLocationEnabled()
    {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
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
