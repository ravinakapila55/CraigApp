package com.helper.Helper2Go.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.custom.ImagePicker;
import com.helper.Helper2Go.google_place_search.GeoLocation;
import com.helper.Helper2Go.google_place_search.GooglePlacesAutocompleteAdapter;
import com.helper.Helper2Go.google_place_search.PlacesAutoCompleteAdapter;
import com.helper.Helper2Go.models.CategoryModel;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.models.SkillsModel;
import com.helper.Helper2Go.models.ToolsModel;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.RangeSlider;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddJobActivity extends ImagePicker
{

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.edt_location)
    AutoCompleteTextView edt_location;

    String latt = "";
    String lngg = "";
    String from = "";

    @BindView(R.id.edt_category)
    TextView edt_category;

    @BindView(R.id.recycler_view_skills)
    RecyclerView recycler_view_skills;

    @BindView(R.id.recycler_view_tools)
    RecyclerView recycler_view_tools;

    @BindView(R.id.edt_duration)
    TextView edt_duration;

    @BindView(R.id.edt_job_title)
    EditText edt_job_title;

    @BindView(R.id.edt_short_description)
    EditText edt_short_description;

    @BindView(R.id.edt_long_description)
    EditText edt_long_description;

    @BindView(R.id.edt_price)
    TextView edt_price;

    @BindView(R.id.etMinPrice)
    TextView etMinPrice;

    @BindView(R.id.etMaxPrice)
    TextView etMaxPrice;

    @BindView(R.id.edt_no_of_applicants)
    EditText edt_no_of_applicants;

    @BindView(R.id.edt_skills)
    EditText edt_skills;

    @BindView(R.id.edt_tools)
    EditText edt_tools;

    @BindView(R.id.uploaded_image)
    ImageView uploaded_image;

    String job_id = "";

    boolean isImgeChanged = false;

    int pos;

    /*
    @BindView(R.id.rangeseekbar)
    RangeSeekBar rangeseekbar;
    */

    String start_price = "";
    String end_price = "";
    int i = 1;
    String cost = "";
    RangeSlider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Log.e("Validator ",System.currentTimeMillis()+"");

        dateList = new ArrayList<String>();
        dateFomatList = new ArrayList<String>();
        calendarList = new ArrayList<String>();

        if (intent != null)
        {
            txt_title.setText(intent.getStringExtra("message"));
            job_id = intent.getStringExtra("job_id");
            from = intent.getStringExtra("from");
            if (!job_id.equalsIgnoreCase(""))
            {
                pos = Integer.parseInt(intent.getStringExtra("position"));
            }
        }

     /* rangeseekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                edt_price.setText(minValue + " -- " + maxValue);
         }
     });*/


        slider = findViewById(R.id.slider);
        slider.setLabelFormatter(value ->
        {
            if (i == 1) {
                start_price = String.format(Locale.US, "%.0f", value);
                i++;
            } else if (i == 2) {
                end_price = String.format(Locale.US, "%.0f", value);
                i = 1;
            }

            if (start_price.equalsIgnoreCase("") || end_price.equalsIgnoreCase("")) {

            } else
            {
                if (Integer.parseInt(start_price) > Integer.parseInt(end_price))
                {
                    cost = end_price + "-" + start_price;
                }
                else
                {
                    cost = start_price + "-" + end_price;
                }
                edt_price.setText(cost);
                etMaxPrice.setText(end_price);
                etMinPrice.setText(start_price);

            }
            return String.format(Locale.US, "%.0f", value);
        });

        getSkills();

        PlacesAutoCompleteAdapter mAdapter = new PlacesAutoCompleteAdapter
                (this, R.layout.custom_spinner_new);
        edt_location.setAdapter(mAdapter);
        edt_location.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setLoactionAdapter(edt_location);
    }

    List<SkillsModel> skillsList = new ArrayList<>();
    List<ToolsModel> toolsList = new ArrayList<>();
    List<CategoryModel> categoryList = new ArrayList<>();

    String job_title = "";
    String short_des = "";
    String long_des = "";
    String no_of_applicants = "";
    String location = "";
    String category = "";
    String price = "";
    String skillsf = "";
    String toolsf = "";
    String duration = "";

    private void getSkills()
    {
        if (!NetworkUtils.isNetworkAvailable(AddJobActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), AddJobActivity.this);
            return;
        }

        MyApplication.getInstance().showProgress(AddJobActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.getSkills
                ("Bearer " + MyApplication.getInstance().useString("user_access_token"))
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

                        skillsList = generalResponse.getDataAsList("skills", SkillsModel.class);
                        toolsList = generalResponse.getDataAsList("tools", ToolsModel.class);
                        categoryList = generalResponse.getDataAsList("categories", CategoryModel.class);

                       /* recycler_view_skills.setLayoutManager(new GridLayoutManager(AddJobActivity.this, 2));
                        recycler_view_skills.setAdapter(new SkillsAdapterForAddJob(AddJobActivity.this, skillsList));
                        recycler_view_skills.setVisibility(View.VISIBLE);

                        recycler_view_tools.setLayoutManager(new GridLayoutManager(AddJobActivity.this, 2));
                        recycler_view_tools.setAdapter(new ToolsAdapterForAddJob(AddJobActivity.this, toolsList));
                        recycler_view_tools.setVisibility(View.VISIBLE);*/


                        if (job_id.equalsIgnoreCase("")) {

                        } else {
                            JobModelForClient jobModelForClient = MyJobsFragment.jobModelForClientList.get(pos);


                            edt_job_title.setText(jobModelForClient.getName());
                            edt_short_description.setText(jobModelForClient.getShort_desc());
                            edt_long_description.setText(jobModelForClient.getLong_desc());
                            edt_no_of_applicants.setText(jobModelForClient.getApplicants_required());


                            if (jobModelForClient.getImage() == null ||
                                    jobModelForClient.getImage().equalsIgnoreCase("null") || jobModelForClient.getImage().equalsIgnoreCase("")) {
                                price = "";
                            } else {
                                file = new File(Injector.JOB_IMAGE_URL + jobModelForClient.getImage());

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.user_img);
                                Glide.with(AddJobActivity.this).setDefaultRequestOptions(requestOptions).load(Injector.JOB_IMAGE_URL +
                                        jobModelForClient.getImage()).into(uploaded_image);

                            }
                            edt_duration.setText(jobModelForClient.getStart_date() + " to " + jobModelForClient.getEnd_date());
                            edt_location.setText(jobModelForClient.getLocation());

                            latt = jobModelForClient.getLatitude();
                            lngg = jobModelForClient.getLongitude();

                            String skillsStr="";
                            JSONArray jsonArraySkills = new JSONArray(jobModelForClient.getSkills_required());
                            StringBuilder skills = new StringBuilder();
                            for (int i = 0; i < jsonArraySkills.length(); ++i) {
                                if (i == jsonArraySkills.length() - 1) {
                                    int pos=jsonArraySkills.length() - 1;
                                    skills.append(jsonArraySkills.get(i));

                                } else {
                                    skills.append(jsonArraySkills.get(i) + ", ");
                                }
                            }
                            skillsStr = skills.toString();


                            if (skillsStr.equalsIgnoreCase("null"))
                            {
                                edt_skills.setText("NA");
                            }
                            else {
                                edt_skills.setText(skillsStr);
                            }





                            JSONArray jsonArrayTools = new JSONArray(jobModelForClient.getTools_needed());
                            String toolsStr="";
                            StringBuilder tools = new StringBuilder();

                            for (int i = 0; i < jsonArrayTools.length(); ++i) {
                                if (i == jsonArrayTools.length() - 1) {
                                    tools.append(jsonArrayTools.get(i));
                                } else {
                                    tools.append(jsonArrayTools.get(i) + ", ");
                                }
                            }
                            toolsStr = tools.toString();


                            if (toolsStr.equalsIgnoreCase("null"))
                            {
                                edt_tools.setText("NA");
                            }
                            else {
                                edt_tools.setText(toolsStr);
                            }



                             if (jobModelForClient.getCategory().equalsIgnoreCase("null"))
                             {
                                 edt_category.setText("NA");
                             }
                             else {
                                 edt_category.setText(jobModelForClient.getCategory());
                             }




//                            edt_price.setText(jobModelForClient.getCost());
                            etMinPrice.setText(jobModelForClient.getCost());
                            etMaxPrice.setText(jobModelForClient.getMax_price());
//                            edt_price.setText(jobModelForClient.getCost());
                            //edt_price.setText("50");

                            job_title = jobModelForClient.getName();
                            short_des = jobModelForClient.getShort_desc();
                            long_des = jobModelForClient.getLong_desc();
                            no_of_applicants = jobModelForClient.getApplicants_required();
                            duration = jobModelForClient.getDuration();
                            start_date = jobModelForClient.getStart_date();
                            end_date = jobModelForClient.getEnd_date();
                            duration = start_date + " to " + end_date;
                            location = jobModelForClient.getLocation();
                            skillsf = skillsStr;
                            toolsf = toolsStr;
                            category = jobModelForClient.getCategory();
                            if (jobModelForClient.getCost() == null || jobModelForClient.getCost().equalsIgnoreCase("null") || jobModelForClient.getCost().equalsIgnoreCase("")) {
                                price = "";
                            } else {
                                price = jobModelForClient.getCost();
                            }

                            String[] pricel = price.split("-");

                            //slider.setValueFrom(Float.parseFloat(pricel[0]));
                            //slider.setValueTo(Float.parseFloat(pricel[1]));
                            slider.setValues(Float.parseFloat(pricel[0]), Float.parseFloat(pricel[1]));
                            //price = "50";
                        }

                        MyApplication.getInstance().hideProgress(AddJobActivity.this);


                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), AddJobActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(AddJobActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(AddJobActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(AddJobActivity.this);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void iv_back() {
        finish();
    }


    private void setLoactionAdapter(final AutoCompleteTextView autoCompleteTextView)
    {
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(),
                R.layout.custom_spinner_new));
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String placeId = GooglePlacesAutocompleteAdapter.resultListId.get(position).toString();

  /*              final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                        placeId + "&key=AIzaSyCxj7Z3cWeV8phaVuua1cSQ88bWT_ls5u0";*/

                final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                        placeId + "&key=AIzaSyBqnNVt17Rxoh12iw83l69DSGBaHYXD2L4";


//                constant.hideKeyboard(EnterPickUp.this,view);

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
//                      DialogClass.showDialog(Listing.this);
                    }

                    @Override
                    protected String doInBackground(final Void... params) {
                        try {
                            serviceArrive(url, autoCompleteTextView);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(final String result) {
//                        DialogClass.logout();
                    }
                }.execute();
            }
        });
    }

    private void serviceArrive(String url1, AutoCompleteTextView autoCompleteTextView) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            URL url = new URL(url1);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            Log.e("url ", "" + url);
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("EXC", "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e("EXC", "Error connecting to Places API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("JSONObjectValue ", jsonObj + "");
            JSONObject res = jsonObj.getJSONObject("result");
            JSONObject geo = res.getJSONObject("geometry");
            JSONObject loc = geo.getJSONObject("location");

            latt = loc.getString("lat");
            lngg = loc.getString("lng");

            Log.e("HomeLocation lat ", latt);
            Log.e("HomeLocation lngg ", lngg);

            String locc = GeoLocation.getAddress(AddJobActivity.this, Double.parseDouble(latt), Double.parseDouble(lngg));

            Log.e("HomeLocation locc ", locc);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.rel_category)
    public void rel_category() {

        final CharSequence[] items = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++) {
            items[i] = categoryList.get(i).getTitle();
        }

        int checkItemPosition = -1;
        if (job_id.equalsIgnoreCase("")) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (edt_category.getText().toString().equals(categoryList.get(i).getTitle())) {
                    checkItemPosition = i;
                    break;
                }
            }
        } else {
            for (int i = 0; i < categoryList.size(); i++) {
                if (edt_category.getText().toString().equals(categoryList.get(i).getTitle())) {
                    checkItemPosition = i;
                    break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Select Category")
                .setCancelable(false)
                .setSingleChoiceItems(items, checkItemPosition, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        edt_category.setText(categoryList.get(selectedPosition).getTitle());
                        selected_category = categoryList.get(selectedPosition).getId() + "";
                    }
                })
                .show();
    }

    String selected_category = "";

    List<String> selectedSkillsList = new ArrayList<>();

    public void selectedSkillsCheckbox(int position, boolean check) {
        if (check) {
            selectedSkillsList.add(skillsList.get(position).getId() + "");
        } else {
            for (int i = 0; i < selectedSkillsList.size(); i++) {
                if (skillsList.get(position).getId() == Integer.parseInt(selectedSkillsList.get(i))) {
                    selectedSkillsList.remove(i);
                    break;
                }
            }
        }

        Log.e("List_Of_Checkbox_Skills", String.valueOf(selectedSkillsList));
    }


    List<String> selectedToolsList = new ArrayList<>();

    public void selectedToolsCheckbox(int position, boolean check) {
        if (check) {
            selectedToolsList.add(toolsList.get(position).getId() + "");
        } else {
            for (int i = 0; i < selectedToolsList.size(); i++) {
                if (toolsList.get(position).getId() == Integer.parseInt(selectedToolsList.get(i))) {
                    selectedToolsList.remove(i);
                    break;
                }
            }
        }

        Log.e("List_Of_Checkbox_Tools", String.valueOf(selectedToolsList));
    }

    int mYear, mMonth, mDay;

    String dates = "";



    String start_date = "";
    String end_date = "";

    //calendarConstraints.setValidator(DateValidatorPointForward.from(startDate))

    /* E/start<<<: 2020-09-2endDate<<<2020-09-18
2020-09-18 10:54:04.308 12825-12825/com.antypro E/start_fomat_date<<<: 2 September,2020end_format_date<<<18 September,2020*/
    AlertDialog.Builder builder, builder1;
    View view1;
    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    ArrayList<String> dateList;
    ArrayList<String> dateFomatList;
    ArrayList<String> calendarList;
    AlertDialog dialog;


    private void calendarView() {
        builder = new AlertDialog.Builder(this);
        view1 = LayoutInflater.from(this).inflate(R.layout.custom_multiple_date, null);

        calendarView = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) view1.findViewById(R.id.calendarView);

        calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .setMinimumDate(Calendar.getInstance())
                .commit();
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);//Todo for set range wise selection mode
        calendarView.setAllowClickDaysOutsideCurrentMonth(true);
        calendarView.setTopbarVisible(true);

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                Log.e("datesList", dates.toString() + "");

                for (int i = 0; i < dates.size(); i++) {

                    //[CalendarDay{2017-5-27}, CalendarDay{2017-5-28}, CalendarDay{2017-5-29}, CalendarDay{2017-5-30}]

                    String CalendarDate = "";
                    CalendarDate = String.valueOf(dates.get(i));
                    Log.e("CalendarDate", CalendarDate + "");
                    String arr[] = CalendarDate.split("CalendarDay");
                    String s = "";
                    for (int j = 0; j < arr.length; j++) {
                        s = arr[j].toString().trim(); // 2017-5-8

                        Log.e("StringS<<", s);
                        Log.e("NewString", s.replace("{", "").replace("}", ""));

                        String s1 = s.replace("{", "").replace("}", "");
                        Log.e("s1<<", s1);

                        String s2 = "", sYear = "", sMonth = "", sDay = "", exactMonth = "", exactFormatMont = "";
                        int month = 0;
                        ArrayList<String> listDate = new ArrayList<String>();

                        if (s1.contains("-")) {
                            String[] data = s1.split("-");
                            listDate.clear();

                            for (int k = 0; k < data.length; k++) {
                                s2 = data[k].toString().trim();
                                Log.e("S2Data<<", s2);
                                if (k == 0) {
                                    sYear = s2;
                                    Log.e("s2Year<<", sYear);
                                }
                                if (k == 1) {
                                    sMonth = s2;
                                    Log.e("s2Month<<", sMonth);
                                    month = Integer.valueOf(sMonth) + 1;
                                    Log.e("month<<", month + "");

                                    HashMap<Integer, String> map = new HashMap<Integer, String>();

                                    map.put(1, "01");
                                    map.put(2, "02");
                                    map.put(3, "03");
                                    map.put(4, "04");
                                    map.put(5, "05");
                                    map.put(6, "06");
                                    map.put(7, "07");
                                    map.put(8, "08");
                                    map.put(9, "09");
                                    map.put(10, "10");
                                    map.put(11, "11");
                                    map.put(12, "12");

                                    exactMonth = map.get(month);

                                    Log.e("exactMonthRange", exactMonth);

                                    HashMap<Integer, String> map1 = new HashMap<Integer, String>();

                                    map1.put(1, "Jan");
                                    map1.put(2, "Feb");
                                    map1.put(3, "March");
                                    map1.put(4, "April");
                                    map1.put(5, "May");
                                    map1.put(6, "June");
                                    map1.put(7, "July");
                                    map1.put(8, "August");
                                    map1.put(9, "September");
                                    map1.put(10, "October");
                                    map1.put(11, "November");
                                    map1.put(12, "December");

                                    exactFormatMont = map1.get(month);

                                    Log.e("exactFormatMont", exactFormatMont);

                                }
                                if (k == 2) {
                                    sDay = s2;
                                    Log.e("s2Day<<", sDay);

                                }
                            }

                            dateList.add(sYear + "-" + exactMonth + "-" + sDay);
                            dateFomatList.add(sDay + " " + exactFormatMont + "," + sYear);


                        }


                    }

                }
            }
        });




        view1.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        view1.findViewById(R.id.tvDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {


                    if (checkMonthlyValidations(view)) {
                        start_date = dateList.get(0).toString();
                        end_date = dateList.get(dateList.size() - 1).toString();

                        Log.e("start<<<", start_date + "endDate<<<" + end_date);

                        start_fomat_date = dateFomatList.get(0).toString();
                        end_format_date = dateFomatList.get(dateFomatList.size() - 1);

                        Log.e("start_fomat_date<<<", start_fomat_date + "end_format_date<<<" + end_format_date);



                       /* start_date = getDate(Long.parseLong(startTimeMillies), "yyyy-MM-dd");
                        end_date = getDate(Long.parseLong(endTimeMillies), "yyyy-MM-dd");*/
                        edt_duration.setText(start_date + " to " + end_date);

                        dialog.dismiss();
                    }


                }
            }
        });


        builder.setView(view1);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }
    String  start_fomat_date = "", end_format_date = "";

    private boolean checkMonthlyValidations(View view) {
        if (dateList.size() == 0) {
            // Alerts.snackBar(view,getString(R.string.monthly_range_empty));
            Toast.makeText(this,"Please select start date and end date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.rel_duration)
    public void rel_duration() {
        calendarView();
    }



    public void set2DatesCalendar()
    {
        Calendar c = Calendar.getInstance();
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(System.currentTimeMillis() - 172800000);
        builder.setCalendarConstraints(new CalendarConstraints.Builder().setValidator(dateValidator).build());
        // long starMilliseconds = milliseconds(start_date);
        // long endMilliseconds = milliseconds(end_date);
        //builder.setSelection(Pair<starMilliseconds, endMilliseconds>);
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        MaterialDatePicker<Long> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());
        picker.setCancelable(false);


        picker.addOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {

            }
        });

        picker.addOnNegativeButtonClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                String[] split1 = selection.toString().split("\\{");
                String[] split2 = split1[1].split(" ");
                String startTimeMillies = split2[0];

                String[] split3 = split2[1].split("\\}");
                String endTimeMillies = split3[0];

                start_date = getDate(Long.parseLong(startTimeMillies), "yyyy-MM-dd");
                end_date = getDate(Long.parseLong(endTimeMillies), "yyyy-MM-dd");
                edt_duration.setText(start_date + " to " + end_date);
                //Toast.makeText(AddJobActivity.this, picker.getHeaderText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    @OnClick(R.id.rel_submit)
    public void rel_submit() {
        if (checkValidations()) {
            selectedSkillsList = Arrays.asList(edt_skills.getText().toString().split("\\s*,\\s*"));
            selectedToolsList = Arrays.asList(edt_tools.getText().toString().split("\\s*,\\s*"));

            Log.e("skillsClick ",selectedSkillsList.toString());
            Log.e("toolsClick ",selectedToolsList.toString());
            if (job_id.equalsIgnoreCase("")) {
                callCreateJob();
            } else {
                if (checkForModification()) {
                    MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                            getResources().getString(R.string.not_updated_any_information), AddJobActivity.this);
                    return;
                } else {
                    //Toast.makeText(AddJobActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    updateJob();
                }
            }
        }
    }

    public boolean checkForModification() {
        return (job_title.equals(edt_job_title.getText().toString()) &&
                short_des.equals(edt_short_description.getText().toString()) &&
                long_des.equals(edt_long_description.getText().toString()) &&
                no_of_applicants.equals(edt_no_of_applicants.getText().toString()) &&
                location.equals(edt_location.getText().toString()) &&
                category.equals(edt_category.getText().toString()) &&
                skillsf.equals(edt_skills.getText().toString()) &&
                toolsf.equals(edt_tools.getText().toString()) &&
                price.equals(edt_price.getText().toString()) &&
                duration.equals(edt_duration.getText().toString()) &&
                !isImgeChanged
        );
    }

    private boolean checkValidations() {
        if (edt_job_title.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_title), AddJobActivity.this);
            return false;
        }
        if (edt_short_description.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_short_description), AddJobActivity.this);
            return false;
        }

        if (edt_long_description.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_long_description), AddJobActivity.this);
            return false;
        }

        if (edt_no_of_applicants.getText().toString().equalsIgnoreCase(""))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_no_of_applicants), AddJobActivity.this);
            return false;
        }

        /*if (file == null) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_image), AddJobActivity.this);
            return false;
        }*/

        if (edt_duration.getText().toString().equalsIgnoreCase(""))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_start_date), AddJobActivity.this);
            return false;
        }

        if (edt_location.getText().toString().equalsIgnoreCase(""))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_location), AddJobActivity.this);
            return false;
        }

        /*if (edt_category.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_category), AddJobActivity.this);
            return false;
+++++++        }

        if (edt_skills.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_skills), AddJobActivity.this);
            return false;
        }

        if (edt_tools.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.empty_job_tools), AddJobActivity.this);
            return false;
        }
*/
      /*  if (edt_price.getText().toString().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Please select price range or add price", AddJobActivity.this);
            return false;
        }  */

      if (etMinPrice.getText().toString().equalsIgnoreCase(""))
      {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Please add minimum price", AddJobActivity.this);
            return false;
        }
        else if (etMaxPrice.getText().toString().equalsIgnoreCase(""))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Please add maximum  price", AddJobActivity.this);
            return false;
        }
        return true;
    }

    int mFileNumber = 0;

    @OnClick(R.id.edt_choose_file_txt)
    public void edt_choose_file()
    {
        mFileNumber = 0;
        gallery(AddJobActivity.this);
    }


    @Override
    public void selectedImage(String imagePath, String type, String thumbnailPath)
    {
        switch (mFileNumber)
        {
            case 0:
            file = new File(imagePath);
            Glide.with(AddJobActivity.this).load(file).into(uploaded_image);
            isImgeChanged = true;
            break;
        }
    }

    String imageAbsolutePath = "";
    private String TAG = "AddJobActivity";

    private static final int SELECT_PHOTO = 100;

    Uri filePath;

    File file;
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e(TAG, "onActivityResult: img url" + filePath);
            try {
                Log.e(TAG, "onActivityResult: ");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploaded_image.setImageBitmap(bitmap);

                imageAbsolutePath = getImagePath(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    File file_1=null;

    public void callCreateJob()
    {

        int  startPrice=Integer.parseInt(etMinPrice.getText().toString().trim());
        int   endPrice=Integer.parseInt(etMaxPrice.getText().toString().trim());
        MultipartBody.Part image;
        file_1 = file;

        if (!NetworkUtils.isNetworkAvailable(AddJobActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
             getResources().getString(R.string.checkInternetConnection), AddJobActivity.this);
            return;
        }
        else  if (endPrice<startPrice) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Maximum price must be greater than start price", AddJobActivity.this);
        }
        else {
            MyApplication.getInstance().showProgress(AddJobActivity.this, "", "");
            TinderAppInterface apiInterface = Injector.provideApi();
            Observable<Response<ResponseBody>> api;

            RequestBody jobTitleRequest = RequestBody.create(MediaType.parse("text/plain"), edt_job_title.getText().toString());
            RequestBody shortDescriptionRequest = RequestBody.create(MediaType.parse("text/plain"), edt_short_description.getText().toString());
            RequestBody longDescriptionRequest = RequestBody.create(MediaType.parse("text/plain"), edt_long_description.getText().toString());
            RequestBody durationRequest = RequestBody.create(MediaType.parse("text/plain"), start_date);
            RequestBody startDateRequest = RequestBody.create(MediaType.parse("text/plain"), start_date);
            RequestBody endDateRequest = RequestBody.create(MediaType.parse("text/plain"), end_date);
            RequestBody locationRequest = RequestBody.create(MediaType.parse("text/plain"), edt_location.getText().toString());
//            RequestBody categoryRequest = RequestBody.create(MediaType.parse("text/plain"), edt_category.getText().toString());
            RequestBody categoryRequest = RequestBody.create(MediaType.parse("text/plain"), "Software Developer");
            RequestBody applicantsRequest = RequestBody.create(MediaType.parse("text/plain"), edt_no_of_applicants.getText().toString());
//      RequestBody priceRequest = RequestBody.create(MediaType.parse("text/plain"), edt_price.getText().toString());
            RequestBody priceRequest = RequestBody.create(MediaType.parse("text/plain"), etMinPrice.getText().toString());
            RequestBody latRequest = RequestBody.create(MediaType.parse("text/plain"), latt);
            RequestBody lonRequest = RequestBody.create(MediaType.parse("text/plain"), lngg);
            RequestBody maxPrice = RequestBody.create(MediaType.parse("text/plain"),etMaxPrice.getText().toString().trim());

            Log.e("ParamsAddJob ",edt_job_title.getText().toString()+edt_short_description.getText().toString()+edt_long_description.getText().toString()+
                    start_date+end_date+edt_location.getText().toString()+edt_no_of_applicants.getText().toString()+etMinPrice.getText().toString()+
                    latt+lngg+etMaxPrice.getText().toString().trim()+"");


            Log.e("Skills ",selectedSkillsList.toString());
            Log.e("tools ",selectedToolsList.toString());

            if (isImgeChanged)
            {
                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_1);
                image = MultipartBody.Part.createFormData("image", file_1.getName(), requestBody);

                api = apiInterface.createJobApiWithImage(jobTitleRequest, shortDescriptionRequest, longDescriptionRequest, applicantsRequest,
                        image, durationRequest, locationRequest, categoryRequest, selectedSkillsList, selectedToolsList, priceRequest,
                        latRequest, lonRequest, startDateRequest, endDateRequest,maxPrice, "Bearer " +
                                MyApplication.getInstance().useString("user_access_token"));

            }
            else
            {
                api = apiInterface.createJobApiWithoutImage(jobTitleRequest, shortDescriptionRequest, longDescriptionRequest, applicantsRequest, durationRequest,
                        locationRequest,categoryRequest, selectedSkillsList, selectedToolsList, priceRequest,
                        latRequest, lonRequest, startDateRequest, endDateRequest,maxPrice, "Bearer " +
                                MyApplication.getInstance().useString("user_access_token"));
            }

            Observable<Response<ResponseBody>> observeApi = api.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            observeApi.subscribe(new Observer<Response<ResponseBody>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response<ResponseBody> response) {
                    MyApplication.getInstance().hideProgress(AddJobActivity.this);
                    Log.e("Data_Loading_Error", String.valueOf(response));
                    GeneralResponse generalResponse = new GeneralResponse(response);
                    Log.e("request....", String.valueOf(generalResponse.response));
                    try {
                        if (generalResponse.checkStatus()) {
                            final android.app.AlertDialog.Builder mainDialog = new android.app.AlertDialog.Builder(AddJobActivity.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.session_dialog, null);
                            mainDialog.setView(dialogView);


                            final Button save = (Button) dialogView.findViewById(R.id.save);
                            final TextView title = (TextView) dialogView.findViewById(R.id.title);
                            final android.app.AlertDialog alertDialog = mainDialog.create();
                            alertDialog.setCancelable(false);
                            title.setText("Job Added Successfully");
                            alertDialog.show();


                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    MyApplication.getInstance().saveString("is_updated", "true");
                                    finish();
                                }
                            });
                        } else {
                            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), AddJobActivity.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Throwable e) {
                    MyApplication.showToast(AddJobActivity.this, e.getMessage());
                    Log.i("Data_Loading_Error", e.toString());
                    MyApplication.getInstance().hideProgress(AddJobActivity.this);
                }

                @Override
                public void onComplete() {
                    MyApplication.getInstance().hideProgress(AddJobActivity.this);
                }
            });
        }
    }


    public void updateJob()
    {
        if (!NetworkUtils.isNetworkAvailable(AddJobActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), AddJobActivity.this);
            return;
        }

        MultipartBody.Part image;
        File file_1 = file;
        MyApplication.getInstance().showProgress(AddJobActivity.this, "", "");
        TinderAppInterface apiInterface = Injector.provideApi();
        Observable<Response<ResponseBody>> api;

        RequestBody jobTitleRequest = RequestBody.create(MediaType.parse("text/plain"), edt_job_title.getText().toString());
        RequestBody shortDescriptionRequest = RequestBody.create(MediaType.parse("text/plain"), edt_short_description.getText().toString());
        RequestBody longDescriptionRequest = RequestBody.create(MediaType.parse("text/plain"), edt_long_description.getText().toString());
        RequestBody durationRequest = RequestBody.create(MediaType.parse("text/plain"), start_date);
        RequestBody startDateRequest = RequestBody.create(MediaType.parse("text/plain"), start_date);
        RequestBody endDateRequest = RequestBody.create(MediaType.parse("text/plain"), end_date);
        RequestBody locationRequest = RequestBody.create(MediaType.parse("text/plain"), edt_location.getText().toString());
//        RequestBody categoryRequest = RequestBody.create(MediaType.parse("text/plain"), edt_category.getText().toString());
        RequestBody categoryRequest = RequestBody.create(MediaType.parse("text/plain"),"Software Developer");
        RequestBody applicantsRequest = RequestBody.create(MediaType.parse("text/plain"), edt_no_of_applicants.getText().toString());
        RequestBody priceRequest = RequestBody.create(MediaType.parse("text/plain"), etMinPrice.getText().toString());
        RequestBody latRequest = RequestBody.create(MediaType.parse("text/plain"), latt);
        RequestBody lonRequest = RequestBody.create(MediaType.parse("text/plain"), lngg);
        RequestBody jobIdRequest = RequestBody.create(MediaType.parse("text/plain"), job_id);
        RequestBody maxPrice = RequestBody.create(MediaType.parse("text/plain"), etMaxPrice.getText().toString().trim());

        if (isImgeChanged)
        {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_1);
            image = MultipartBody.Part.createFormData("image", file_1.getName(), requestBody);
            api = apiInterface.updateJobApi(
                    jobTitleRequest,
                    shortDescriptionRequest,
                    longDescriptionRequest,
                    applicantsRequest,
                    image,
                    durationRequest,
                    locationRequest,
                    categoryRequest,
                    selectedSkillsList,
                    selectedToolsList,
                    priceRequest,
                    latRequest,
                    lonRequest,
                    jobIdRequest,
                    startDateRequest,
                    endDateRequest,maxPrice,
                    "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        }
        else
        {
            api = apiInterface.updateJobWithoutImageApi(jobTitleRequest,
                    shortDescriptionRequest, longDescriptionRequest,
                    applicantsRequest, durationRequest,
                    locationRequest, categoryRequest,
                    selectedSkillsList, selectedToolsList,
                    priceRequest, latRequest,
                    lonRequest, jobIdRequest,
                    startDateRequest, endDateRequest,maxPrice,
                    "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        }

        Observable<Response<ResponseBody>> observeApi = api.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(Response<ResponseBody> response)
            {
                MyApplication.getInstance().hideProgress(AddJobActivity.this);
                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));
                try {
                    if (generalResponse.checkStatus()) {
                        final android.app.AlertDialog.Builder mainDialog = new android.app.AlertDialog.Builder(AddJobActivity.this);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.session_dialog, null);
                        mainDialog.setView(dialogView);


                        final Button save = (Button) dialogView.findViewById(R.id.save);
                        final TextView title = (TextView) dialogView.findViewById(R.id.title);
                        final android.app.AlertDialog alertDialog = mainDialog.create();
                        alertDialog.setCancelable(false);
                        title.setText("Job Updated Successfully");
                        alertDialog.show();


                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                alertDialog.dismiss();
                                MyApplication.getInstance().saveString("is_updated", "true");

                                if (from.equalsIgnoreCase("list"))
                                {
                                    finish();
                                }

                                else
                                {
//                                    Intent intent = new Intent(AddJobActivity.this, HomeMainActivity.class);
//                                    intent.putExtra("from","job");
//                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else
                    {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), AddJobActivity.this);
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
                MyApplication.showToast(AddJobActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(AddJobActivity.this);
            }

            @Override
            public void onComplete()
            {
                MyApplication.getInstance().hideProgress(AddJobActivity.this);
            }
        });
    }


}
