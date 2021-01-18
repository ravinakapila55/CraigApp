package com.helper.Helper2Go.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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

import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.ApiUtils.TinderAppInterface;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.LoginResponse;
import com.helper.Helper2Go.ui.AddJobActivity;
import com.helper.Helper2Go.ui.ApplyAsHelperActivity;
import com.helper.Helper2Go.ui.HomeMainActivity;
import com.helper.Helper2Go.ui.LoginActivity;
import com.helper.Helper2Go.utils.GeneralResponse;
import com.helper.Helper2Go.utils.MyApplication;
import com.helper.Helper2Go.utils.NetworkUtils;
import com.google.gson.JsonObject;

public class HomeTutorialFragment extends Fragment {

    View mRootView;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btn_completed;

    int selected_position = 0;

    public HomeTutorialFragment()
    {
        // Required empty public constructor
    }


    public static HomeTutorialFragment newInstance(String param1, String param2)
    {
        HomeTutorialFragment fragment = new HomeTutorialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_home_tutorial, container, false);
        ButterKnife.bind(this, mRootView);

        viewPager = mRootView.findViewById(R.id.view_pager);
        dotsLayout = mRootView.findViewById(R.id.layoutDots);
        btn_completed = mRootView.findViewById(R.id.btn_completed);


        btn_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSendFeedback();
            }
        });

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.tutorial_home_one,
                R.layout.tutorial_home_two};


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        return mRootView;
    }



    private void callSendFeedback(EditText editText,AlertDialog alertDialog) {


        MyApplication.getInstance().showProgress(getActivity(), "", getResources().getString(R.string.loading));
        TinderAppInterface apiInterface = Injector.provideApi();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message",editText.getText().toString().trim());

        Log.e("requestParams....", String.valueOf(jsonObject));

        Observable<Response<ResponseBody>> observeApi = apiInterface.sendFeedback(jsonObject, "Bearer " +
                MyApplication.getInstance().useString("user_access_token"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observeApi.subscribe(new Observer<Response<ResponseBody>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(Response<ResponseBody> response)
            {
                Log.e("Data_Loading_Error", String.valueOf(response));
                GeneralResponse generalResponse = new GeneralResponse(response);
                Log.e("request....", String.valueOf(generalResponse.response));

                try
                {
                    if (generalResponse.checkStatus())
                    {
                        MyApplication.getInstance().hideProgress(getActivity());
                        Toast.makeText(getContext(), getResources().getString(R.string.feedback_success_msg), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                    else
                    {

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
            public void onComplete() {
                MyApplication.getInstance().hideProgress(getActivity());
            }
        });

    }

    public void showSendFeedback()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.send_feedback, null);

        ImageView ivCross=(ImageView) dialogView.findViewById(R.id.ivCross);
        EditText etFeedback=(EditText) dialogView.findViewById(R.id.etFeedback);
        TextView tvSubmit=(TextView) dialogView.findViewById(R.id.tvSubmit);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (etFeedback.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(), "Enter Feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    callSendFeedback(etFeedback,alertDialog);
                }
            }
        });






        alertDialog.show();
    }


    private void addBottomDots(int currentPage)
    {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                //selected_position = 1;
                //tv_looking_for_mini_jobs.setText(getResources().getString(R.string.go_to_dashboard));

            } else {
                //selected_position = 0;
                //tv_looking_for_mini_jobs.setText(getResources().getString(R.string.looking_for_mini_job));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
    }
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter()
        {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            RelativeLayout rel_apply_as_helper = view.findViewById(R.id.rel_apply_as_helper);
            TextView tv_looking_for_mini_jobs = view.findViewById(R.id.tv_looking_for_mini_jobs);

            Log.e("inside ","inside");

            rel_apply_as_helper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tv_looking_for_mini_jobs.getText().toString().equalsIgnoreCase("Go to dashboard")){
                        // go to dashboard
                        ((HomeMainActivity) getActivity()).changeFragment(new MyJobsFragment(), R.id.navigation_admin);
                    }
                    else{
                        // Looking for mini jobs
                        ((HomeMainActivity) getActivity()).changeFragment(new SearchFragment(), R.id.navigation_search);
                    }
                }
            });

            return view;
        }

        @Override
        public int getCount()
        {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

   /* @OnClick(R.id.rel_apply_as_helper)
    public void rel_apply_as_helper(){

        if(selected_position == 0){
            // Looking for mini jobs
            ((HomeMainActivity) getActivity()).changeFragment(new SearchFragment(), R.id.navigation_search);
        }
        else{
            // go to dashboard
            ((HomeMainActivity) getActivity()).changeFragment(new MyJobsFragment(), R.id.navigation_admin);
        }

    }*/

}
