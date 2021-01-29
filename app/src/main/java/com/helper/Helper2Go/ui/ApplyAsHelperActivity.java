package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.flowable.FlowableElementAt;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.custom.CustomEditTextRegular;
import com.helper.Helper2Go.models.JobFilterParam;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.ui.fragments.HomeFragment;
import com.helper.Helper2Go.ui.fragments.ViewPersonDetailFragment;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;
import com.tristate.radarview.LatLongCs;

import java.util.ArrayList;
import java.util.List;

public class ApplyAsHelperActivity extends AppCompatActivity
{

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.checkbox1)
    CheckBox checkBox1;

    @BindView(R.id.checkbox2)
    CheckBox checkBox2;

    @BindView(R.id.txt_order_num)
    TextView txt_order_num;

    @BindView(R.id.txt_order_name)
    TextView txt_order_name;

    int job_id;

    @BindView(R.id.edt_intro)
    EditText edt_intro;

    @BindView(R.id.edt_other)
    EditText edt_other;

    @BindView(R.id.edt_price)
    EditText edt_price;

    String experienec="";
    String tools="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_as_helper);
        ButterKnife.bind(this);

        txt_order_name.setText(ViewPersonDetailFragment.job_name);
        txt_order_num.setText(ViewPersonDetailFragment.job_id + "");

        /*Intent intent = new Intent();
        if(intent != null)
        {
        job_id = intent.getIntExtra("job_id", 0);
        }
        */
        job_id = ViewPersonDetailFragment.job_id;
        txt_title.setText(getResources().getString(R.string.apply_as_helper));
    }

    @OnClick(R.id.iv_back)
    public void iv_back()
    {
        finish();
    }

    @BindView(R.id.edt_experience)
    EditText edt_experience;

    @BindView(R.id.edt_tools)
    EditText edt_tools;

    @OnClick(R.id.rel_apply)
    public void rel_apply()
    {
     if (edt_price.getText().toString().trim().isEmpty())
     {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Please enter your budget", ApplyAsHelperActivity.this);
            return;
      }
      /* else if (edt_experience.getText().toString().trim().isEmpty())
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                "Please enter your experience", ApplyAsHelperActivit`q```y.this);
            return;
        }
        else if (edt_tools.getText().toString().trim().isEmpty())
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    "Please enter your tools", ApplyAsHelperActivity.this);
            return;
        }*/

     else  if(!checkBox1.isChecked())
     {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
            getResources().getString(R.string.please_accept_conditions), ApplyAsHelperActivity.this);
            return;
     }

     /*if(!checkBox2.isChecked()){
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.please_accept_conditions), ApplyAsHelperActivity.this);
            return;
      }*/

     callApiApplyForJob(ViewPersonDetailFragment.job_price, "", ApplyAsHelperActivity.this);

     // showDialogForPriceTime(ApplyAsHelperActivity.this);
    }

    public void showDialogForPriceTime(final Context activity) {
        final Dialog dialog = new Dialog(activity);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_price_time);

        EditText edt_price  = dialog.findViewById(R.id.edt_price);
        EditText edt_time = dialog.findViewById(R.id.edt_time);
        RelativeLayout rel_apply = dialog.findViewById(R.id.rel_apply);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);

        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        rel_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_price.getText().toString().equalsIgnoreCase("")){
                    MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.please_enter_price), ApplyAsHelperActivity.this);
                }
                else if(edt_time.getText().toString().equalsIgnoreCase("")){
                    MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.please_enter_time), ApplyAsHelperActivity.this);
                }
                else {
                    dialog.dismiss();
                    callApiApplyForJob(edt_price.getText().toString(), edt_time.getText().toString(), activity);
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callApiApplyForJob(String price, String time, Context activity)
    {
        if (!NetworkUtils.isNetworkAvailable(ApplyAsHelperActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), ApplyAsHelperActivity.this);
            return;
        }

        MyApplication.getInstance().showProgress(ApplyAsHelperActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("job_id", job_id);
        jsonObject.addProperty("user_cost", edt_price.getText().toString().trim());
        jsonObject.addProperty("other_info", edt_other.getText().toString().trim());
        //jsonObject.addProperty("user_time", time);
        jsonObject.addProperty("user_job_desc", edt_intro.getText().toString());
        jsonObject.addProperty("helper_exp", "test experience");
        jsonObject.addProperty("helper_tools","test tools");

        /* jsonObject.addProperty("helper_exp", edt_experience.getText().toString().trim());
       jsonObject.addProperty("helper_tools", edt_tools.getText().toString().trim());
       */
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.applyForJob(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"))
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
                        MyApplication.getInstance().hideProgress(ApplyAsHelperActivity.this);
                        showDialogForSuccess(activity);
                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), ApplyAsHelperActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(ApplyAsHelperActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(ApplyAsHelperActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(ApplyAsHelperActivity.this);
            }
        });

    }


    public void showDialogForSuccess(final Context activity)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_success);

        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Handler handler = new Handler();
        Runnable runnable = () -> {
            dialog.dismiss();
            finish();
        };
        handler.postDelayed(runnable, 2000);

        dialog.show();

    }

}
