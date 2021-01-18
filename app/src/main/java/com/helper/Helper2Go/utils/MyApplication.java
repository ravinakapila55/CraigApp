package com.helper.Helper2Go.utils;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.multidex.MultiDex;

import com.helper.Helper2Go.ApiUtils.Injector;
import com.helper.Helper2Go.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MyApplication extends Application {

    private static MyApplication instance;

    public static ProgressDialog mProgressDialog;

    public static String current_lat;
    public static String current_lon;

    public static final String CHANNEL_1_ID="channel";

    // private SplashModel splashModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sharedPreferences = getApplicationContext().getSharedPreferences("a12mg09jk267kld0hpnkd", 0);
        createNotificationChannels();

    }


    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /*public static void showCustomisedToast(Context context, String message, String type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (type){
            case "s":
                view = inflater.inflate(R.layout.custom_toast_green, (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container), false);
                break;
            case "e":
                view = inflater.inflate(R.layout.custom_toast_red, (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container), false);
                break;
            case "i":
                view = inflater.inflate(R.layout.custom_toast_grey, (ViewGroup) ((Activity) context).findViewById(R.id.custom_toast_container), false);
                break;
        }

        TextView tv = (TextView) view.findViewById(R.id.txtvw);
        tv.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }*/

    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void disableTouch(Activity context) {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enbleTouch(Activity context) {
        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }



    public static final void setAppLocale(String language, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Resources resources = activity.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(new Locale(language));
            activity.getApplicationContext().createConfigurationContext(configuration);
        } else {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = activity.getResources().getConfiguration();
            config.locale = locale;
            activity.getResources().updateConfiguration(config,
                    activity.getResources().getDisplayMetrics());
        }

    }


    public void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void saveBoolean(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean containsKey(String key) {

        return sharedPreferences.contains(key);
    }

    /* public void clearSharedPreferences()
     {
         sharedPreferences.edit().clear().apply();
     }*/
    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String useString(String key) {
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    public void saveInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int useInt(String key) {
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }

    public Boolean useBoolean(String key) {
        boolean value = sharedPreferences.getBoolean(key, false);
        return value;
    }


   /* public void showProgress(Context context, String title, String message) {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(message);
        mProgressDialog.setMessage(context.getResources().getString(R.string.loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }*/

   public void showProgress(Context context, String title, String message) {

       mProgressDialog = new ProgressDialog(context);
       mProgressDialog.show();
       if (mProgressDialog.getWindow() != null) {
           mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       }

       mProgressDialog.setContentView(R.layout.progress_dialog);
       mProgressDialog.setIndeterminate(true);
       mProgressDialog.setCancelable(false);
       mProgressDialog.setCanceledOnTouchOutside(false);

    }

    public void hideProgress(Context context) {
        if (mProgressDialog.isShowing() && mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog.hide();
        }
    }


    public void changeLocale(String language) {
        Locale locale = new Locale(language);
        Configuration conf = getResources().getConfiguration();
        conf.locale = locale;
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLayoutDirection(conf.locale);
        }
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());

    }




    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is Channel 1");
            channel1.setShowBadge(true);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }


    public void displayMessageNew(View view, String toastString, Activity thisActivity) {
        try{
            Snackbar snackbar = Snackbar.make(view, toastString, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(thisActivity.getResources().getColor(R.color.app_color));
            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(thisActivity.getResources().getColor(R.color.white));
            textView.setMaxLines(5);
            snackbar.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getImageUrl(String imgUrl)
    {
        if (imgUrl != null && imgUrl.length() > 0) {
            if (imgUrl.startsWith("http")) {
                return imgUrl;
            } else {
                return Injector.BASE_IMAGE_LOAD_URL_WITH_STORAGE + imgUrl;
            }
        } else
            return "";
    }
}
