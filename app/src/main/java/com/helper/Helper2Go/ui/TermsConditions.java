package com.helper.Helper2Go.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.helper.Helper2Go.R;

public class TermsConditions extends AppCompatActivity implements View.OnClickListener
{

    ImageView iv_filter;
    WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conditions);
        findInits();
        loadWeb();
    }

    public void findInits()
    {
        iv_filter=(ImageView)findViewById(R.id.iv_filter);
        web=(WebView) findViewById(R.id.web);
        iv_filter.setOnClickListener(this);
    }

    public void loadWeb()
    {
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl("http://54.77.62.201/terms-conditions-page");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_filter:
                onBackPressed();
                break;

        }
    }
}
