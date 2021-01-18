package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.utils.CommonValidations;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        txt_title.setText(getResources().getString(R.string.forgot_password));
    }

    @OnClick(R.id.btn_send)
    public void btn_send()
    {

        if (edt_email.getText().toString().trim().equalsIgnoreCase(""))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.email_not_empty), ForgotPasswordActivity.this);
            return;
        }

        if((edt_email.getText().toString().matches("^[0-9]+$")))
        {
            if (edt_email.getText().toString().length() != 10 )
            {
                MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                        getResources().getString(R.string.invalidPhone), ForgotPasswordActivity.this);
                return;
            }
        }
        else {
            if (!CommonValidations.isValidEmail(edt_email.getText().toString().trim()))
            {
                MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                        getResources().getString(R.string.invalidEmail), ForgotPasswordActivity.this);
                return;
            }
        }

        callForgetPasswordApi();

    }

    @OnClick(R.id.iv_back)
    public void iv_back()
    {
        finish();
    }


    public void callForgetPasswordApi()
    {
        if (!NetworkUtils.isNetworkAvailable(ForgotPasswordActivity.this))
        {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content),
                    getResources().getString(R.string.checkInternetConnection), ForgotPasswordActivity.this);
            return;
        }
        hideKeyboard();

        MyApplication.getInstance().showProgress(ForgotPasswordActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", edt_email.getText().toString());
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.forgotPasswordApi(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
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
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(),
                                ForgotPasswordActivity.this);
                        Handler handler = new Handler();
                        Runnable runnable = () -> {
                            finish();
                        };
                        handler.postDelayed(runnable, 1200);

                    }
                    else
                    {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), ForgotPasswordActivity.this);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(ForgotPasswordActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(ForgotPasswordActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(ForgotPasswordActivity.this);
            }
        });
    }

    public void hideKeyboard()
    {
        View mRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (mRootView != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
        }
    }
}
