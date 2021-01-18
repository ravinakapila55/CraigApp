package com.helper.Helper2Go.ApiUtils;


import android.util.Log;

import com.helper.Helper2Go.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;


/**
 * Created by 123 on 25-09-2017.
 */

public class Injector
{
/*    public static final String BASE_URL = "http://178.128.116.149/tinder/api/";
    public static final String JOB_IMAGE_URL = "http://178.128.116.149/tinder/images/";
    public static String BASE_IMAGE_LOAD_URL_WITH_STORAGE = "http://178.128.116.149/tinder/public/profile/";  */

    public static final String BASE_URL = "http://54.77.62.201/api/";
    public static final String JOB_IMAGE_URL = "http://54.77.62.201/images/";
    public static String BASE_IMAGE_LOAD_URL_WITH_STORAGE = " http://54.77.62.201/public/profile/";

//  public static String SOCKET_URL = "http://178.128.116.149:4201/";
    public static String SOCKET_URL = "http://54.77.62.201:8081/";
//  public static String SOCKET_URL = "http://165.22.213.185:4400/";

//  public static final String BASE_URL_GET_CHAT = "http://178.128.116.149:4201/api/";
    public static final String BASE_URL_GET_CHAT = "http://54.77.62.201:8081/api/";
//  public static final String BASE_URL_GET_CHAT = "http://165.22.213.185:4400/api/";

    private static Retrofit provideRetrofit(String baseUrl)
    {
         return new Retrofit.Builder()
         .baseUrl(baseUrl)
         .client(provideOkHttpClient())
         .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
         .addConverterFactory(GsonConverterFactory.create())
         .build();
    }

    private static OkHttpClient provideOkHttpClient()
    {
                 return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideHeaderInterceptor())
                .addInterceptor(new ForbiddenInterceptor())
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log(String message) {
                        Log.e("Injector","mess "+message);
                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return httpLoggingInterceptor;
    }

    public static TinderAppInterface provideApi()
    {
        return provideRetrofit(BASE_URL).create(TinderAppInterface.class);
    }

    public static TinderAppInterface provideChatApi()
    {
        return provideRetrofit(BASE_URL_GET_CHAT).create(TinderAppInterface.class);
    }

    private static Interceptor provideHeaderInterceptor()
    {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request;
               /* if (!helper.getAuthToken().equals("")) {
                    request = chain.request().newBuilder()
                           // .header("Accept", "application/json")
                            .header("Authorization-API-KEY", helper.getAuthToken())
                            .build();
                } else {
                    request = chain.request().newBuilder().build();
                }*/
                request = chain.request().newBuilder().build();
                return chain.proceed(request);
            }
        };
    }

    public static class ForbiddenInterceptor implements Interceptor
    {
        @Override
        public Response intercept(final Chain chain) throws IOException
        {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() == 401)
            {
//                SharedPrefUtil.getInstance().setLogin(false);
//                MyApplication.tokenExpired();
            }
            return response;
        }
    }
}