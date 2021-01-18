package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.adapter.NotificationAdapter;
import com.helper.Helper2Go.models.NotificationModel;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.helper.Helper2Go.utils.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerTouchListener touchListener;

    @BindView(R.id.recycler_notification)
    RecyclerView recycler_notification;

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.txt_no_notification)
    TextView txt_no_notification;

    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        txt_title.setText(getResources().getString(R.string.notifications));

        getNotifications();

        recycler_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false));

        touchListener = new RecyclerTouchListener(NotificationActivity.this, recycler_notification);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        //Toast.makeText(NotificationActivity.this, "jjjfkjk", Toast.LENGTH_SHORT).show();
                        // recycler view row click listener
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.delete_task:

                                callClearNotificationById(position);

                                break;

                        }
                    }
                });

    }

    private void callClearNotificationById(int position) {
        if (!NetworkUtils.isNetworkAvailable(NotificationActivity.this)) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), NotificationActivity.this);
            return;
        }

        MyApplication.getInstance().showProgress(NotificationActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> api = apiInterface.deleteNotification(String.valueOf(notificationModelList.get(position).getId()), "Bearer " + MyApplication.getInstance().useString("user_access_token"));

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

                        notificationModelList.remove(position);
                        notificationAdapter.setTaskList(notificationModelList);

                        if(notificationModelList.size() != 0) {
                            txt_no_notification.setVisibility(View.GONE);
                            recycler_notification.setVisibility(View.VISIBLE);
                        }
                        else{
                            txt_no_notification.setVisibility(View.VISIBLE);
                            recycler_notification.setVisibility(View.GONE);
                        }

                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), NotificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(NotificationActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(NotificationActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(NotificationActivity.this);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        recycler_notification.addOnItemTouchListener(touchListener);
    }

    @OnClick(R.id.iv_back)
    public void iv_back() {
        finish();
    }


    List<NotificationModel> notificationModelList = new ArrayList<>();

    public void getNotifications() {
        if (!NetworkUtils.isNetworkAvailable(NotificationActivity.this)) {
            MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), getResources().getString(R.string.checkInternetConnection), NotificationActivity.this);
            return;
        }

        MyApplication.getInstance().showProgress(NotificationActivity.this, "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();

        Observable<Response<ResponseBody>> api = apiInterface.getNotifications("Bearer " + MyApplication.getInstance().useString("user_access_token"));

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

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() != 0) {

                            if (notificationModelList.size() != 0) {
                                notificationModelList.clear();
                            }

                            notificationModelList = generalResponse.getDataAsList("result", NotificationModel.class);
                            txt_no_notification.setVisibility(View.GONE);
                            recycler_notification.setVisibility(View.VISIBLE);

                            recycler_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                            notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationModelList);
                            recycler_notification.setAdapter(notificationAdapter);

                        } else {
                            txt_no_notification.setVisibility(View.VISIBLE);
                            recycler_notification.setVisibility(View.GONE);
                        }
                    } else {
                        MyApplication.getInstance().displayMessageNew(findViewById(android.R.id.content), generalResponse.getMessage(), NotificationActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                MyApplication.showToast(NotificationActivity.this, e.getMessage());
                Log.i("Data_Loading_Error", e.toString());
                MyApplication.getInstance().hideProgress(NotificationActivity.this);
            }

            @Override
            public void onComplete() {
                MyApplication.getInstance().hideProgress(NotificationActivity.this);
            }
        });
    }
}
