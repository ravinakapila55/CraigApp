package com.helper.Helper2Go.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.custom.ImagePickerFragment;
import com.helper.Helper2Go.custom.MySpinner;
import com.helper.Helper2Go.models.LoginResponse;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.ui.LoginActivity;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends ImagePickerFragment {

    @BindView(R.id.country_code_picker)
    CountryCodePicker country_code_picker;

    private String TAG = "ProfileFragment";

    private static final int SELECT_PHOTO = 100;

    Uri filePath;
    File file;

    boolean isImgeChanged = false;

    @BindView(R.id.img_edit)
    ImageView img_edit;

    @BindView(R.id.btn_save)
    Button btn_save;

    @BindView(R.id.edt_name)
    EditText edt_name;

    @BindView(R.id.edt_intro)
    EditText edt_intro;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.edt_phone)
    EditText edt_phone;

    @BindView(R.id.edt_address)
    EditText edt_address;

    @BindView(R.id.img_profile)
    ImageView img_profile;

    @BindView(R.id.spinner_gender)
    MySpinner spinner_gender;

    View mRootView;

    String selected_gender = "";


    @BindView(R.id.img_name)
    ImageView img_name;

    @BindView(R.id.img_address)
    ImageView img_address;

    @BindView(R.id.img_intro)
    ImageView img_intro;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_profle, container, false);
        ButterKnife.bind(this, mRootView);
        enableDisableETxt(false);
        edt_phone.setEnabled(false);
        edt_email.setEnabled(false);
        spinner_gender.setEnabled(false);
        country_code_picker.setCcpClickable(false);

        hideIconWhenType(edt_name, img_name);
        hideIconWhenType(edt_address, img_address);
        hideIconWhenType(edt_intro, img_intro);

        String name = MyApplication.getInstance().useString("user_name");
        String intro = MyApplication.getInstance().useString("user_intro");
        String email = MyApplication.getInstance().useString("user_email");
        String phone = MyApplication.getInstance().useString("user_phone");
        String image = MyApplication.getInstance().useString("user_image");
        String address = MyApplication.getInstance().useString("user_address");
        String gender = MyApplication.getInstance().useString("user_gender");
        String country_code = MyApplication.getInstance().useString("user_country_code");

        int cc;
        cc=Integer.parseInt(country_code);

        Log.e("Image ",image);
        Log.e("country_code ",country_code);
        Log.e("cc ",cc+"");

//        country_code_picker.setCountryForNameCode(country_code);
        country_code_picker.setCountryForPhoneCode(cc);


        if (name == null || name.equalsIgnoreCase("null") || name.equalsIgnoreCase("")) {
            edt_name.setText("");
        } else {
            edt_name.setText(name);
        }

        if (intro == null || intro.equalsIgnoreCase("null") || intro.equalsIgnoreCase("")) {
            edt_intro.setText("");
        } else {
            edt_intro.setText(intro);
        }

        if (email == null || email.equalsIgnoreCase("null") || email.equalsIgnoreCase("")) {
            edt_email.setText("");
        } else {
            edt_email.setText(email);
        }

        if (phone == null || phone.equalsIgnoreCase("null") || phone.equalsIgnoreCase(""))
        {
            edt_phone.setText("");
        }
        else
        {
            edt_phone.setText(phone);
        }

        if (address == null || address.equalsIgnoreCase("null") || address.equalsIgnoreCase(""))
        {
            edt_address.setText("");
        }
        else
        {
            edt_address.setText(address);
        }

        if (image == null || image.equalsIgnoreCase("null") || image.equalsIgnoreCase(""))
        {
            img_profile.setImageResource(R.drawable.ic_dummy_user);
        }
        else
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_dummy_user);
            Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(MyApplication.getImageUrl(image)).into(img_profile);
        }


        List<String> genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("male");
        genderList.add("female");
        genderList.add("other");

        final ArrayAdapter<String> spinnerArrayAdapterForJobDuration = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner, android.R.id.text1, genderList);

        spinnerArrayAdapterForJobDuration.setDropDownViewResource(R.layout.spinner);
        spinner_gender.setAdapter(spinnerArrayAdapterForJobDuration);
        spinnerArrayAdapterForJobDuration.notifyDataSetChanged();

        if (gender == null || gender.equalsIgnoreCase("null") || gender.equalsIgnoreCase(""))
        {

        }
        else
        {
            spinner_gender.setSelection(spinnerArrayAdapterForJobDuration.getPosition(gender));
        }

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_gender = genderList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });


        View.OnFocusChangeListener myFocusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    HomeMainActivity.navigation.setVisibility(View.INVISIBLE);
                } else {

                    HomeMainActivity.navigation.setVisibility(View.VISIBLE);

                }
            }
        };


        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    HomeMainActivity.navigation.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "Keyboard is showing", Toast.LENGTH_LONG).show();
                } else {
                    edt_name.clearFocus();
                    edt_intro.clearFocus();
                    edt_address.clearFocus();
                    HomeMainActivity.navigation.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(), "keyboard closed", Toast.LENGTH_LONG).show();
                    }
            }
        });
        /*edt_name.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
                    edt_name.setFocusable(false);
                    edt_name.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });*/

      /*  edt_name.setOnFocusChangeListener(myFocusListener);
        edt_address.setOnFocusChangeListener(myFocusListener);
        edt_email.setOnFocusChangeListener(myFocusListener);
        edt_phone.setOnFocusChangeListener(myFocusListener);
*/

        return mRootView;
    }

    public void hideIconWhenType(EditText editText, ImageView imageView)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick(R.id.btn_save)
    public void btn_save() {
        if (btn_save.getText().toString().equalsIgnoreCase(getString(R.string.edit))) {
            btn_save.setText(getString(R.string.save));
            enableDisableETxt(true);
        } else {
            Pattern ps = Pattern.compile(".*[0-9].*");
            Matcher name = ps.matcher(edt_name.getText().toString());

            if (edt_name.getText().toString().equals("") || edt_name.getText().toString().length() == 0) {
                MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.name_empty), getActivity());
            } else if (name.matches()) {
                MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.name_no_number), getActivity());
            } else if (edt_address.getText().toString().equals("") || edt_address.getText().toString().length() == 0) {
                MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.address_empty), getActivity());
            } else if (selected_gender.equalsIgnoreCase("Select Gender")) {
                MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.please_select_gender), getActivity());
            }
            else if (edt_intro.getText().toString().equals("") || edt_intro.getText().toString().length() == 0) {
                MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.intro_empty), getActivity());
            }else {
                updateProfile();
            }
        }
    }


    private void enableDisableETxt(boolean isEnabled)
    {
        img_profile.setEnabled(isEnabled);
        img_edit.setEnabled(isEnabled);
        edt_name.setEnabled(isEnabled);
        edt_intro.setEnabled(isEnabled);
        edt_address.setEnabled(isEnabled);
        spinner_gender.setEnabled(isEnabled);
        if(isEnabled){
            img_edit.setVisibility(View.VISIBLE);
        }
        else{
            img_edit.setVisibility(View.GONE);
        }
        //.setEnabled(isEnabled);
        // edt_phone.setEnabled(isEnabled);
    }


    public void takeImage()
    {
        mFileNumber = 0;
        gallery(getActivity());
    }


    int mFileNumber = 0;

    @Override
    public void selectedImage(String imagePath, String type, String thumbnailPath) {
        switch (mFileNumber) {
            case 0:
                file = new File(imagePath);
                Log.e(TAG, "onActivityResult: img url" + file);
                Log.e(TAG, "onActivityResult: ");
                Glide.with(getActivity())
                        .load(file)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_dummy_user)).into(img_profile);

                isImgeChanged = true;

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
            for (int grantResult : grantResults)
                if (grantResult == PackageManager.PERMISSION_GRANTED)
                    goToImageIntent();
    }

    public void goToImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
    }


    public void updateProfile() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), getActivity());
            return;
        }

        MultipartBody.Part image;


        File file_1 = file;

        MyApplication.getInstance().showProgress(getActivity(), "", "");
        TinderAppInterface apiInterface = Injector.provideApi();
        Observable<Response<ResponseBody>> api;


        RequestBody nameRequest = RequestBody.create(MediaType.parse("text/plain"), edt_name.getText().toString());
        RequestBody genderRequest = RequestBody.create(MediaType.parse("text/plain"), selected_gender);
        RequestBody addressRequest = RequestBody.create(MediaType.parse("text/plain"), edt_address.getText().toString());
        RequestBody introRequest = RequestBody.create(MediaType.parse("text/plain"), edt_intro.getText().toString());

        Log.e("ParamsProfile ",nameRequest.toString());
        Log.e("gender ",genderRequest.toString());
        Log.e("address ",addressRequest.toString());
        Log.e("intro ",introRequest.toString());

        if (isImgeChanged)
        {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_1);
            image = MultipartBody.Part.createFormData("image", file_1.getName(), requestBody);
            api = apiInterface.updateProfileWithImage(nameRequest, genderRequest, addressRequest, introRequest, image, "Bearer " + MyApplication.getInstance().useString("user_access_token"));
        } else {
            api = apiInterface.updateProfileWithoutImage(nameRequest, genderRequest, addressRequest, introRequest, "Bearer " + MyApplication.getInstance().useString("user_access_token"));
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

                        JSONObject jsonObject = generalResponse.response;

                        Log.e("ResponseProfile ",jsonObject.getJSONObject("result").getString("profile_image"));

                        MyApplication.getInstance().saveString("user_name", jsonObject.getJSONObject("result").getString("name"));
                        MyApplication.getInstance().saveString("user_email", jsonObject.getJSONObject("result").getString("email"));
                        MyApplication.getInstance().saveString("user_image", jsonObject.getJSONObject("result").getString("profile_image"));
                        MyApplication.getInstance().saveString("user_phone", jsonObject.getJSONObject("result").getString("phone_no"));
                        MyApplication.getInstance().saveString("user_gender", jsonObject.getJSONObject("result").getString("gender"));
                        MyApplication.getInstance().saveString("user_address", jsonObject.getJSONObject("result").getString("address"));
                        MyApplication.getInstance().saveString("user_intro", jsonObject.getJSONObject("result").getString("info"));

                        MyApplication.getInstance().displayMessageNew(((Activity) getContext()).findViewById(android.R.id.content), getResources().getString(R.string.update_success), getActivity());
                        btn_save.setText(getString(R.string.edit));
                        enableDisableETxt(false);
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


    String imageAbsolutePath = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.e(TAG, "onActivityResult: img url" + filePath);
            try {
                Log.e(TAG, "onActivityResult: ");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                img_profile.setImageBitmap(bitmap);

                isImgeChanged = true;
                imageAbsolutePath = getImagePath(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
