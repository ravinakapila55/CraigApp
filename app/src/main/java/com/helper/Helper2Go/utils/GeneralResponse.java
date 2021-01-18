package com.helper.Helper2Go.utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class GeneralResponse  {
    ResponseBody responseBody;
    public JSONObject response;

    int status_code;
    public GeneralResponse(Response<ResponseBody> rb) {
        status_code=rb.code();
        this.responseBody = (rb.body() != null) ? rb.body() : rb.errorBody();

        try {
            this.response = new JSONObject(responseBody.string());
        } catch (Exception je) {
            je.printStackTrace();
        }
    }

    public boolean checkStatus() throws JSONException {

        return status_code == 200 && (response.getString("status").equals("true") ||
                response.getString("status").equals("200") || response.getString("status").equals("OK") || response.getString("status").equals("success"));
    }

    public String getMessage() throws JSONException {

       return status_code==200? response.getString("message"):"Server Error !";
    }

    public <T> T getJSONObjectAs(String key,Class<T> classOFT) throws JSONException {
        JSONObject ja = response;
        Gson gson = new Gson();
        return gson.fromJson(ja.getJSONObject(key).toString(), (Type) classOFT);

    }

    public <T> List<T> getDataAsList(String key, Class<T> classOFT) throws JSONException {
        if (response == null) response = new JSONObject(responseBody.toString());
        JSONArray ja = response.getJSONArray(key);
        int len = ja.length();
        Gson gson = new Gson();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < ja.length(); i++) {
            list.add(gson.fromJson(ja.getString(i), classOFT));
        }
        return list;
    }



    public <T> List<T> getDataAsListDecoded(JSONObject jsonObject,String key, Class<T> classOFT) throws JSONException {
        // if (response == null) response = new JSONObject(responseBody.toString());
        JSONArray ja = jsonObject.getJSONArray(key);
        int len = ja.length();
        Gson gson = new Gson();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < ja.length(); i++) {
            list.add(gson.fromJson(ja.getString(i), classOFT));
        }
        return list;
    }

    public int getValue(String Key) throws JSONException {
        if (response == null) response = new JSONObject(responseBody.toString());

        int val = response.getInt(Key);
        return val;
    }

    public String getStringValue(String Key) throws JSONException {
        if (response == null) response = new JSONObject(responseBody.toString());

        String val = response.getString(Key);
        return val;
    }

    public <T> List<T> getDataAsList(String key,String key2, Class<T> classOFT) throws JSONException {
        if (response == null) response = new JSONObject(responseBody.toString());
        JSONArray ja = response.getJSONObject(key).getJSONArray(key2);
        int len = ja.length();
        Gson gson = new Gson();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < ja.length(); i++) {
            list.add(gson.fromJson(ja.getString(i), classOFT));
        }
        return list;
    }


    public <T> String objectgetstringAs(Class<T> classOFT){
        Gson gson = new Gson();
        return gson.toJson(classOFT);
    }

}