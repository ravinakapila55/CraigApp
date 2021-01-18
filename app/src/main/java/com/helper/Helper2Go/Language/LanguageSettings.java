package com.helper.Helper2Go.Language;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;


import com.helper.Helper2Go.utils.MyApplication;

import java.util.Locale;


public class LanguageSettings {

    @SuppressLint("NewApi")
    public static void languageMethod(Context context, String languageCode) {


        if (languageCode.equals("de"))
        {
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("de"));
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }
        else if (languageCode.equals("en"))
        {
            Configuration configuration = context.getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }

    }

    public static void setLocale(String lang, Context context) {

       /* SharedPreferences sharedPreferences = App.getGlobalPrefs();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ServiceApi.LANGUAGE_KEY, lang).commit();*/

        MyApplication.getInstance().saveString("lang", lang);


        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        languageMethod(context,lang);

       /* Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        languageMethod(context, lang);*/

    }
}
