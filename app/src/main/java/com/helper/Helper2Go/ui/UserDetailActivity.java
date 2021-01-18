package com.helper.Helper2Go.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.helper.Helper2Go.R;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;
import com.helper.Helper2Go.utils.MyApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class UserDetailActivity extends AppCompatActivity
{
    @BindView(R.id.circular_img)
    ImageView circular_img;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.gender)
    TextView gender;

  /* @BindView(R.id.offerDetail)
     TextView offerDetail;

     @BindView(R.id.otherInfo)
     TextView otherInfo;

     @BindView(R.id.budget)
     TextView budget;

     */

    @BindView(R.id.user_cost)
    TextView user_cost;

    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.edt_intro)
    TextView edt_intro;

    String job_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        ButterKnife.bind(this);
        job_id = getIntent().getStringExtra("job_id");

        txt_title.setText(getResources().getString(R.string.user_details));
        name.setText(MyJobsFragment.applicant.getName());
        email.setText(MyJobsFragment.applicant.getEmail());
        phone.setText(MyJobsFragment.applicant.getPhone_no());
        String genders = MyJobsFragment.applicant.getGender();

        if (genders == null || genders.equalsIgnoreCase("null") || genders.equalsIgnoreCase(""))
        {
            gender.setText("N/A");
        }
        else
        {
            gender.setText(genders);
        }

        user_cost.setText(MyJobsFragment.applicant.getUser_cost());
        edt_intro.setText(MyJobsFragment.applicant.getInfo());

  /*      offerDetail.setText(MyJobsFragment.applicant.getUser_job_desc());
        otherInfo.setText(MyJobsFragment.applicant.getOther_info());
        budget.setText("CHF "+MyJobsFragment.applicant.getUser_cost());*/
//        offerDetail.setText(MyJobsFragment.applicant.getO());

        String image = MyJobsFragment.applicant.getProfile_image();

        if (image == null || image.equalsIgnoreCase("null") || image.equalsIgnoreCase(""))
        {

        }
        else
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_dummy_user);
            Glide.with(UserDetailActivity.this).setDefaultRequestOptions(requestOptions).load(MyApplication.getImageUrl(image)).into(circular_img);
        }

    }

    @OnClick(R.id.iv_back)
    public void iv_back()
    {
        finish();
    }

    @OnClick(R.id.iv_chat)
    public void iv_chat()
    {
        callChatScreen();
    }

    public void callChatScreen()
    {
        Intent intent = new Intent(UserDetailActivity.this, ChatActivity.class);
        intent.putExtra("senderId", MyApplication.getInstance().useString("user_id"));
        intent.putExtra("receiverId", MyJobsFragment.applicant.getId() + "");
        intent.putExtra("receiver_job_id", job_id);
        intent.putExtra("sender_job_id", MyJobsFragment.applicant.getApplicant_id() + "");
        intent.putExtra("receiver_name", MyJobsFragment.applicant.getName());
        intent.putExtra("budget", MyJobsFragment.applicant.getUser_cost());
        intent.putExtra("offer", MyJobsFragment.applicant.getUser_job_desc());
        intent.putExtra("other", MyJobsFragment.applicant.getOther_info());
        startActivity(intent);
    }
}
