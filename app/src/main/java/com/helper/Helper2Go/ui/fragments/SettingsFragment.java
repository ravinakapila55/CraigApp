package com.helper.Helper2Go.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.Language.LanguageSettings;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.HomeScreenAdapter;
import com.helper.Helper2Go.adapter.SkillAdapter;
import com.helper.Helper2Go.models.JobModel;
import com.helper.Helper2Go.models.SkillsModel;
import com.helper.Helper2Go.ui.ApplyAsHelperActivity;
import com.helper.Helper2Go.ui.LoginActivity;
import com.helper.Helper2Go.ui.SignUpActivity;
import com.helper.Helper2Go.ui.TermsConditions;
import com.helper.Helper2Go.utils.CommonValidations;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SettingsFragment extends Fragment {

    View mRootView;

    @BindView(R.id.recycler_view_skills)
    RecyclerView recycler_view_skills;

    @BindView(R.id.rel_view)
    RelativeLayout rel_view;

    @BindView(R.id.languageLayout)
    LinearLayout languageLayout;

    @BindView(R.id.rg_Language)
    RadioGroup rg_Language;

    @BindView(R.id.rb_english)
    RadioButton rb_english;

    @BindView(R.id.rb_german)
    RadioButton rb_german;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, mRootView);

        if (MyApplication.getInstance().useString("lang").equalsIgnoreCase("de")) {
            rb_german.setChecked(true);
        } else if (MyApplication.getInstance().useString("lang").equalsIgnoreCase("en")) {
            rb_english.setChecked(true);
        }

        rg_Language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rb_english:
                        MyApplication.getInstance().saveString("lang", "en");
                        LanguageSettings.setLocale("en", getActivity());
                        break;

                    case R.id.rb_german:
                        MyApplication.getInstance().saveString("lang", "de");
                        LanguageSettings.setLocale("de", getActivity());
                        break;
                }
            }
        });
        //getSkills();
        return mRootView;
    }



    private boolean aBoolean = false;

    public void getSkills() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.getSkills("Bearer " + MyApplication.getInstance().useString("user_access_token"))
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

                        List<SkillsModel> skillsModelList = generalResponse.getDataAsList("result", SkillsModel.class);
                        MyApplication.getInstance().hideProgress(getContext());

                        List<String> checkBoxListForSkills = new ArrayList<>();

                        for(int i = 0; i < skillsModelList.size(); i++){
                            checkBoxListForSkills.add(skillsModelList.get(i).getName());
                        }

                        recycler_view_skills.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        recycler_view_skills.setAdapter(new SkillAdapter(getContext(), checkBoxListForSkills));
                        rel_view.setVisibility(View.VISIBLE);
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


    boolean passwordVisibility=false;
    boolean passwordVisibilityCofirm=false;
    
    @OnClick(R.id.card_change_password)
    public void card_change_password(){
        showChangePasswordDialog(getActivity());
    }

    @OnClick(R.id.card_terms)
    public void card_terms(){

        Intent intent=new Intent(getActivity(), TermsConditions.class);
        startActivity(intent);

    }

    @OnClick(R.id.card_language)
    public void card_language(){

        if (!aBoolean) {
            languageLayout.setVisibility(View.VISIBLE);
            aBoolean = true;
        } else {
            languageLayout.setVisibility(View.GONE);
            aBoolean = false;
        }

    }

    private void showChangePasswordDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.change_password_dialog);
        final EditText etConfirmPassword = dialog.findViewById(R.id.edt_new_confirm_password);
        final EditText etPassword = dialog.findViewById(R.id.edt_new_password);
        TextView button_submit = dialog.findViewById(R.id.button_submit);
        ImageView iv_crosee = dialog.findViewById(R.id.iv_crosee);
        final ImageView img_pass = dialog.findViewById(R.id.img_eye);
        final ImageView img_confirm_pass = dialog.findViewById(R.id.img_confirm_eye);



        Window window = dialog.getWindow();
        window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //dialog.dismiss();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (password.equalsIgnoreCase("")) {
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.password_cannot_be_empty), getActivity());
                    return;
                }

                if(!CommonValidations.isValidPassword(password)){
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.invalidPassword), getActivity());
                    return;
                }

                if (confirmPassword.equalsIgnoreCase("")) {
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.confirm_password_not_empty), getActivity());
                    return;
                }

                if(!CommonValidations.isValidPassword(confirmPassword)){
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.invalidConfirmPassword), getActivity());
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.password_missmatch), getActivity());
                    return;
                }

                showDialog(password, dialog);

                //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

            }
        });

        img_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordVisibility){
                    if(etPassword.getText().toString().length()>0) {
                        passwordVisibility=false;
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_pass.setImageResource(R.drawable.eye);
                        etPassword.setSelection(etPassword.getText().length());
                    }

                }
                else {
                    if (etPassword.getText().toString().length() > 0) {
                        passwordVisibility = true;
                        etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        img_pass.setImageResource(R.drawable.eye_inivisible);
                        etPassword.setSelection(etPassword.getText().length());
                    }
                }
            }
        });

        img_confirm_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordVisibilityCofirm){
                    if(etConfirmPassword.getText().toString().length()>0) {
                        passwordVisibilityCofirm=false;
                        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        img_confirm_pass.setImageResource(R.drawable.eye);
                        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
                    }

                }
                else {
                    if (etConfirmPassword.getText().toString().length() > 0) {
                        passwordVisibilityCofirm = true;
                        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        img_confirm_pass.setImageResource(R.drawable.eye_inivisible);
                        etConfirmPassword.setSelection(etConfirmPassword.getText().length());
                    }
                }
            }
        });

        iv_crosee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @OnClick(R.id.card_logout)
    public void card_logout(){
        showLogoutDialog();
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.laucher_icon);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.exit_confirm));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callLogoutApi();
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


    private void callLogoutApi() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> observeApi = apiInterface.logoutApi("Bearer " + MyApplication.getInstance().useString("user_access_token"))
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
                    if (generalResponse.checkStatus())
                    {
                        MyApplication.getInstance().saveString("user_name", "");
                        MyApplication.getInstance().saveString("user_email", "");
                        MyApplication.getInstance().saveString("user_phone", "");
                        MyApplication.getInstance().saveString("user_image", "");
                        MyApplication.getInstance().saveString("user_address", "");
                        MyApplication.getInstance().saveString("user_access_token", "");
                        MyApplication.getInstance().saveString("user_gender", "");
                        MyApplication.getInstance().saveString("user_country_code", "");

                        MyApplication.getInstance().saveBoolean("is_logined", false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
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


    private void showDialog(final String password, Dialog dialogs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.change_password))
                .setMessage(getResources().getString(R.string.change_password_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callChangePasswordApi(password, dialogs);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogs.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void callChangePasswordApi(String password, Dialog dialog) {
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MyApplication.getInstance().showProgress(getContext(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("confirm_password", password);
        Log.e("request....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.changePasswordApi(jsonObject, "Bearer " + MyApplication.getInstance().useString("user_access_token"))
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
                        dialog.dismiss();
                        MyApplication.getInstance().hideProgress(getContext());
                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), generalResponse.getMessage(), getActivity());
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

}
