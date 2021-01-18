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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.LoginResponse;
import com.helper.Helper2Go.models.RegistrationResponse;
import com.helper.Helper2Go.utils.CommonValidations;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.edt_name)
    EditText edt_name;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.edt_phone)
    EditText edt_phone;

    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    TextView tvTerms;


    @BindView(R.id.agree_checkbox)
    CheckBox agree_checkbox;

    @BindView(R.id.agree_checkbox_terms)
    CheckBox agree_checkbox_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tvTerms=(TextView)findViewById(R.id.tvTerms);
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, TermsConditions.class);
                startActivity(intent);
            }
        });
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rel_signup)
    public void rel_signup() {
        if (edt_name.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.name_not_empty), SignUpActivity.this);
            return;
        }

        if (!CommonValidations.isValidName((edt_name.getText().toString()))) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidName), SignUpActivity.this);
            return;
        }

        if (edt_email.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.email_not_empty), SignUpActivity.this);
            return;
        }

        if (!CommonValidations.isValidEmail(edt_email.getText().toString().trim())) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidEmail), SignUpActivity.this);
            return;
        }

        if (edt_phone.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.contact_not_empty), SignUpActivity.this);
            return;
        }

        if (edt_phone.getText().toString().trim().length() < 8 || edt_phone.getText().toString().trim().length() > 12) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.contact_not_invalid), SignUpActivity.this);
            return;
        }

        if (edt_password.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.password_cannot_be_empty), SignUpActivity.this);
            return;
        }

        if(!CommonValidations.isValidPassword(edt_password.getText().toString())){
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidPassword), SignUpActivity.this);
            return;
        }

        if (edt_confirm_password.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.confirm_password_not_empty), SignUpActivity.this);
            return;
        }

        if(!CommonValidations.isValidPassword(edt_confirm_password.getText().toString())){
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidConfirmPassword), SignUpActivity.this);
            return;
        }

        if (!edt_password.getText().toString().equals(edt_confirm_password.getText().toString())) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.password_missmatch), SignUpActivity.this);
            return;
        }

        if(!agree_checkbox.isChecked()){
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.accept_term), SignUpActivity.this);
            return;
        }

        if(!agree_checkbox_terms.isChecked()){
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.accept_term_cond), SignUpActivity.this);
            return;
        }

        callSignupApi();
    }

    @OnClick(R.id.lin_sign_in)
    public void lin_sign_in() {
//        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//        startActivity(intent);
        finish();
    }


    @BindView(R.id.country_code_picker)
    CountryCodePicker country_code_picker;

    public void callSignupApi() {
        if (!NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), SignUpActivity.this);
            return;
        }
        hideKeyboard();

        MyApplication.getInstance().showProgress(SignUpActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", edt_name.getText().toString());
        jsonObject.addProperty("email", edt_email.getText().toString());
        jsonObject.addProperty("phone_no", edt_phone.getText().toString());
        jsonObject.addProperty("password", edt_password.getText().toString());
        jsonObject.addProperty("c_password", edt_confirm_password.getText().toString());
//        jsonObject.addProperty("country_code", country_code_picker.getSelectedCountryNameCode());
        jsonObject.addProperty("country_code", country_code_picker.getSelectedCountryCode());
        jsonObject.addProperty("device_type", "android");
        jsonObject.addProperty("device_token", MyApplication.getInstance().useString("DeviceToken"));
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.registerApi(jsonObject)
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
                        MyApplication.getInstance().hideProgress(SignUpActivity.this);
                        showDialogForSuccess(SignUpActivity.this);
                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), SignUpActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(SignUpActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(SignUpActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(SignUpActivity.this);
            }
        });
    }

    public void hideKeyboard() {
        View mRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (mRootView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
        }
    }


    @BindView(R.id.img_confirm_eye)
    ImageView img_confirm_eye;
    @BindView(R.id.img_eye)
    ImageView img_eye;

    boolean passwordVisibility = false;

    @OnClick(R.id.img_eye)
    public void img_eye() {
        if (passwordVisibility) {
            if (edt_password.getText().toString().length() > 0) {
                passwordVisibility = false;
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                img_eye.setImageResource(R.drawable.eye);
                edt_password.setSelection(edt_password.getText().toString().length());
            }

        } else {
            if (edt_password.getText().toString().length() > 0) {
                passwordVisibility = true;

                edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                img_eye.setImageResource(R.drawable.eye_inivisible);
                edt_password.setSelection(edt_password.getText().toString().length());
            }
        }
    }

    boolean passwordVisibilityConfirm = false;

    @OnClick(R.id.img_confirm_eye)
    public void img_confirm_eye() {
        if (passwordVisibilityConfirm) {
            if (edt_confirm_password.getText().toString().length() > 0) {
                passwordVisibilityConfirm = false;
                edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                img_confirm_eye.setImageResource(R.drawable.eye);
                edt_confirm_password.setSelection(edt_confirm_password.getText().toString().length());
            }

        } else {
            if (edt_confirm_password.getText().toString().length() > 0) {
                passwordVisibilityConfirm = true;
                edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                img_confirm_eye.setImageResource(R.drawable.eye_inivisible);
                edt_confirm_password.setSelection(edt_confirm_password.getText().toString().length());
            }
        }
    }



    public void showDialogForSuccess(final Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_success);

        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        TextView txt_message = dialog.findViewById(R.id.txt_message);
        txt_message.setText(getResources().getString(R.string.registered_successfully));

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Handler handler = new Handler();
        Runnable runnable = () -> {
            dialog.dismiss();
           /* Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);*/

            /*     if (LoginActivity.getInstance() != null) {
                LoginActivity.getInstance().finish();
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
            finish();
        };
        handler.postDelayed(runnable, 2000);

        dialog.show();

    }



    public void disclosure(View view)
    {
        //startActivity(new Intent(SignUpActivity.this,DisclosureActivity.class));
    }
}
