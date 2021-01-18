package com.helper.Helper2Go.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.NotificationAdapter;
import com.helper.Helper2Go.models.NotificationModel;
import com.helper.Helper2Go.models.ProfileResponse;
import com.helper.Helper2Go.models.RegistrationResponse;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    return;
                }
                String token = task.getResult().getToken();
                Log.e("Firebase_token", token);
                MyApplication.getInstance().saveString("DeviceToken", token);
            }
        });

        Handler handler = new Handler();
        Runnable runnable = () -> {

            Log.e("loginValue ",MyApplication.getInstance().useBoolean("is_logined")+"");
            if (!checkAccessCameraPermission() || !checkAccessExternalStoragePermission() || checkAccessCoarseLocationPermission() || checkAccessFineLocationPermission()) {
                requestPermission();

            } else {
                if(MyApplication.getInstance().useBoolean("is_logined")){

                    getProfile();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, TutorialsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        };
        handler.postDelayed(runnable, 3000);
    }
    
    
    public void getProfile(){
        if (!NetworkUtils.isNetworkAvailable(SplashActivity.this)) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), SplashActivity.this);
            finish();
            return;
        }

        //MyApplication.getInstance().showProgress(SplashActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> api = apiInterface.getProfile("Bearer " + MyApplication.getInstance().useString("user_access_token"));

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
                        ProfileResponse profileResponse = generalResponse.getJSONObjectAs("result", ProfileResponse.class);
                        MyApplication.getInstance().saveString("user_name", profileResponse.getName());
                        MyApplication.getInstance().saveString("user_email", profileResponse.getEmail());
                        MyApplication.getInstance().saveString("user_phone", profileResponse.getPhone_no());
                        MyApplication.getInstance().saveString("user_image", profileResponse.getProfile_image());
                        MyApplication.getInstance().saveString("user_address", profileResponse.getAddress());
                        MyApplication.getInstance().saveString("user_gender", profileResponse.getGender());
                        MyApplication.getInstance().saveString("user_country_code", profileResponse.getCountry_code());
                        MyApplication.getInstance().saveString("user_intro", profileResponse.getInfo());

                        Intent intent = new Intent(SplashActivity.this, HomeMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), SplashActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(SplashActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
               // MyApplication.getInstance().hideProgress(SplashActivity.this);
            }

            @Override
            public void onComplete() {
               // MyApplication.getInstance().hideProgress(SplashActivity.this);
            }
        });
    }


    private boolean checkAccessCoarseLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAccessFineLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private boolean checkAccessCameraPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAccessExternalStoragePermission()
    {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkReceiveMessagePermission()
    {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkReadMessagePermission() {


        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
    }

    int PERMISSION_REQUEST_CODE = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(MyApplication.getInstance().useBoolean("is_logined")){
                       getProfile();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, TutorialsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    finish();
                }
                break;
        }
    }





}
