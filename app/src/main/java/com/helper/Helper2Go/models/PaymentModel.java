package com.helper.Helper2Go.models;

import androidx.annotation.Keep;

@Keep
public class PaymentModel {
    String job_name;

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getUser_cost() {
        return user_cost;
    }

    public void setUser_cost(String user_cost) {
        this.user_cost = user_cost;
    }

    String user_cost;


}
