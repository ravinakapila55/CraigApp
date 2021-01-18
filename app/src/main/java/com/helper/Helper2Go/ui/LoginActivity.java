package com.helper.Helper2Go.ui;

import androidx.annotation.Nullable;
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.Language.LanguageSettings;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.custom.MySpinner;
import com.helper.Helper2Go.models.LoginResponse;
import com.helper.Helper2Go.utils.CommonMethods;
import com.helper.Helper2Go.utils.CommonValidations;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity
{

    public static Activity activity;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.tvLabel)
    TextView tvLabel;

    @BindView(R.id.txt_sign_up)
    TextView txt_sign_up;

    @BindView(R.id.labelll)
    TextView labelll;

    @BindView(R.id.txt_or)
    TextView txt_or;

    @BindView(R.id.txt_forgot_password)
    TextView txt_forgot_password;

    @BindView(R.id.btn_login)
    TextView btn_login;

    @BindView(R.id.tvSigninStart)
    TextView tvSigninStart;

    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;

    private int RC_SIGN_IN = 9001;
    String auth_id = "";
    String username = "";
    String login_via = "";
    String name = "";

    @BindView(R.id.spLanguage)
    Spinner spLanguage;

    String compareValue="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MyApplication.getInstance().saveString("lang", "en");
        LanguageSettings.setLocale("en", LoginActivity.this);

        activity = this;
        ButterKnife.bind(this);

        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        setUpGoogleMethod();

        List<String> genderList = new ArrayList<>();
        genderList.add(getResources().getString(R.string.language));
        genderList.add("English");
        genderList.add("German");


        final ArrayAdapter<String> spinnerArrayAdapterForJobDuration = new ArrayAdapter<String>(
                this, R.layout.spinner, android.R.id.text1, genderList);

        spinnerArrayAdapterForJobDuration.setDropDownViewResource(R.layout.spinner);
        spLanguage.setAdapter(spinnerArrayAdapterForJobDuration);
        spinnerArrayAdapterForJobDuration.notifyDataSetChanged();

       /* if (MyApplication.getInstance().useString("langSelection").equalsIgnoreCase(""))
        {
            Log.e("SavedValue ",MyApplication.getInstance().useString("langSelection"));
        } else
        {
            int spinnerPosition = spinnerArrayAdapterForJobDuration.getPosition(MyApplication.getInstance().useString("langSelection"));
            spLanguage.setSelection(spinnerPosition);
        }*/
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                compareValue = String.valueOf(spLanguage.getSelectedItem());
                Log.e("selectedValue ",compareValue);

                if (position==1)
                {
                    MyApplication.getInstance().saveString("langSelection", compareValue);
                    MyApplication.getInstance().saveString("lang", "en");
                    LanguageSettings.setLocale("en", LoginActivity.this);

                    tvLabel.setText(getResources().getString(R.string.welcome));
                    tvSigninStart.setText(getResources().getString(R.string.sign_in_to_continue));
                    edt_email.setHint(getResources().getString(R.string.email_phone));
                    edt_password.setHint(getResources().getString(R.string.password));
                    btn_login.setText(getResources().getString(R.string.login));
                    txt_forgot_password.setText(getResources().getString(R.string.forgot_password));
                    txt_or.setText(getResources().getString(R.string.or));
                    labelll.setText(getResources().getString(R.string.dont_have_an_account));
                    txt_sign_up.setText(getResources().getString(R.string.sign_UP));
//                    restartActivity();
                }
                else  if (position==2)
                {
                    MyApplication.getInstance().saveString("langSelection", compareValue);
                    MyApplication.getInstance().saveString("lang", "de");
                    LanguageSettings.setLocale("de", LoginActivity.this);
                    tvLabel.setText(getResources().getString(R.string.welcome));
                    tvSigninStart.setText(getResources().getString(R.string.sign_in_to_continue));
                    edt_email.setHint(getResources().getString(R.string.email_phone));
                    edt_password.setHint(getResources().getString(R.string.password));
                    btn_login.setText(getResources().getString(R.string.login));
                    txt_forgot_password.setText(getResources().getString(R.string.forgot_password));
                    txt_or.setText(getResources().getString(R.string.or));
                    labelll.setText(getResources().getString(R.string.dont_have_an_account));
                    txt_sign_up.setText(getResources().getString(R.string.sign_UP));

//                    restartActivity();
                }
                else {
                    MyApplication.getInstance().saveString("langSelection", "lang");
                    MyApplication.getInstance().saveString("lang", "en");
                    LanguageSettings.setLocale("en", LoginActivity.this);

                    tvLabel.setText(getResources().getString(R.string.welcome));
                    tvSigninStart.setText(getResources().getString(R.string.sign_in_to_continue));
                    edt_email.setHint(getResources().getString(R.string.email_phone));
                    edt_password.setHint(getResources().getString(R.string.password));
                    btn_login.setText(getResources().getString(R.string.login));
                    txt_forgot_password.setText(getResources().getString(R.string.forgot_password));
                    txt_or.setText(getResources().getString(R.string.or));
                    labelll.setText(getResources().getString(R.string.dont_have_an_account));
                    txt_sign_up.setText(getResources().getString(R.string.sign_UP));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0) {
                    country_code_picker.setVisibility(View.VISIBLE);
                } else {
                    Pattern ps = Pattern.compile("[0-9]*$");
                    Matcher name = ps.matcher(edt_email.getText().toString());

                    if (name.matches()) {
                        country_code_picker.setVisibility(View.VISIBLE);
                    } else {
                        country_code_picker.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @OnClick(R.id.lin_sign_up)
    public void lin_sign_up()
    {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.txt_forgot_password)
    public void txt_forgot_password()
    {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration config = newBase.getResources().getConfiguration();
//        SharedPreferences sharedPreferences = App.getGlobalPrefs();

        String language =MyApplication.getInstance().useString("lang");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.setLocale(new Locale(language));

        Resources resources = newBase.getResources();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newBase = newBase.createConfigurationContext(config);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newBase = newBase.createConfigurationContext(config);

        } else {
            Resources resources = newBase.getResources();
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }*/
        super.attachBaseContext(newBase);
    }

    @OnClick(R.id.rel_login)
    public void rel_login()
    {
        if (edt_email.getText().toString().trim().equalsIgnoreCase(""))   if ((edt_email.getText().toString().matches("^[0-9]+$"))) {
            if (edt_email.getText().toString().length() != 10) {
                MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidPhone), LoginActivity.this);
                return;
            }
        } else {
            if (!CommonValidations.isValidEmail(edt_email.getText().toString().trim())) {
                MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.invalidEmail), LoginActivity.this);
                return;
            }
        }

        if (edt_password.getText().toString().trim().equalsIgnoreCase("")) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.password_cannot_be_empty), LoginActivity.this);
            return;
        }

        login_via = "";
        callLoginApi();
    }

    @BindView(R.id.country_code_picker)
    CountryCodePicker country_code_picker;

    public void callLoginApi() {
        if (!NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), LoginActivity.this);
            return;
        }
        hideKeyboard();

        MyApplication.getInstance().showProgress(LoginActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();


        if(login_via.equalsIgnoreCase(""))
        {
            jsonObject.addProperty("username", edt_email.getText().toString());
            jsonObject.addProperty("password", edt_password.getText().toString());
//            jsonObject.addProperty("country_code", country_code_picker.getSelectedCountryNameCode());
            jsonObject.addProperty("country_code", country_code_picker.getDefaultCountryCode());
        }
        else
        {
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("auth_id", auth_id);
            jsonObject.addProperty("name", name);
        }
        jsonObject.addProperty("login_via", login_via);
        jsonObject.addProperty("device_type", "android");
        jsonObject.addProperty("device_token", MyApplication.getInstance().useString("DeviceToken"));
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.loginApi(jsonObject)
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
                    if (generalResponse.checkStatus())
                    {
                        LoginResponse loginModel = generalResponse.getJSONObjectAs("result", LoginResponse.class);
                        MyApplication.getInstance().saveString("user_name", loginModel.getName());
                        MyApplication.getInstance().saveString("user_email", loginModel.getEmail());
                        MyApplication.getInstance().saveString("user_phone", loginModel.getPhone_no());
                        MyApplication.getInstance().saveString("user_image", loginModel.getProfile_image());
                        MyApplication.getInstance().saveString("user_address", loginModel.getAddress());
                        MyApplication.getInstance().saveString("user_access_token", loginModel.getAccess_token());
                        MyApplication.getInstance().saveString("user_gender", loginModel.getGender());
                        MyApplication.getInstance().saveString("user_country_code", loginModel.getCountry_code());
                        MyApplication.getInstance().saveString("user_intro", loginModel.getInfo());
                        MyApplication.getInstance().saveString("user_id", loginModel.getId() + "");
                        MyApplication.getInstance().saveBoolean("is_logined", true);

                        Intent intent = new Intent(LoginActivity.this, HomeMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("from","login");
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), LoginActivity.this);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(LoginActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(LoginActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(LoginActivity.this);
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

    public static Activity getInstance() {
        return activity;
    }

    @BindView(R.id.img_eye)
    ImageView img_eye;

    boolean passwordVisibility = false;

    @OnClick(R.id.img_eye)
    public void img_eye()
    {
        if (passwordVisibility)
        {
            if (edt_password.getText().toString().length() > 0)
            {
                passwordVisibility = false;
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                img_eye.setImageResource(R.drawable.eye);
                edt_password.setSelection(edt_password.getText().toString().length());
            }
        } else {
            if (edt_password.getText().toString().length() > 0)
            {
                passwordVisibility = true;
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                img_eye.setImageResource(R.drawable.eye_inivisible);
                edt_password.setSelection(edt_password.getText().toString().length());
            }
        }
    }

    @OnClick(R.id.img_fb)
    public void facebookLogin()
    {
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                LoginManager.getInstance().logOut();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setFaceBookLogin();
    }

    private void setFaceBookLogin()
    {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("MainActivity", "Facebook token: " + loginResult.getAccessToken().getToken());


                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse response) {
                                if (response != null && response.getJSONObject() != null) {

                                    handleSignInResultFacebook(user);

                                }
                            }
                        });

                Bundle parameters = new Bundle();

                parameters.putString("fields","id,name,email,gender,birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d("MainActivity", "Facebook onCancel.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("MainActivity", "Facebook onError." + error);
            }
        });
    }

    private void handleSignInResultFacebook(JSONObject jsonObject) {
        String id = jsonObject.optString("id");
        login_via = "facebook";
        auth_id = id;
        name = jsonObject.optString("name");
        username = jsonObject.optString("email");
        //Toast.makeText(LoginActivity.this, "Fetched User email: " + username +" SEND DATA ON SERVER UNDER DEVELOPMENT", Toast.LENGTH_SHORT).show();
        callLoginApi();
    }

    @OnClick(R.id.img_google)
    public void gmailLogin()
    {
        try
        {
         googleSignInClient.signOut();
        }
        catch (Exception e)
        {
         e.printStackTrace();
        }

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void setUpGoogleMethod()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //int statusCode = result.getStatus().getStatusCode();
            Task<GoogleSignInAccount> result = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = result.getResult(ApiException.class);
                if (result.isSuccessful()) {
                    login_via = "google";
                    username = account.getEmail();
                    auth_id = account.getId();
                    name = account.getGivenName() + " " + account.getFamilyName();
                    //Toast.makeText(LoginActivity.this, "Fetched User email: " + username +" SEND DATA ON SERVER UNDER DEVELOPMENT", Toast.LENGTH_SHORT).show();
                    callLoginApi();
                } else {
                    // CommonMethod.showToast("There was a trouble signing in-Please try again");
                }
            }
            catch (ApiException e){
                e.printStackTrace();
            }
        } /*else if (isTwitterLogin) {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data)
        }*/
    }
}
