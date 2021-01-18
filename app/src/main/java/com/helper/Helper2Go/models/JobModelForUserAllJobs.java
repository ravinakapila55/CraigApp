package com.helper.Helper2Go.models;

import androidx.annotation.Keep;

@Keep
public class JobModelForUserAllJobs
{
    int id;
    String name;
    String short_desc;
    String long_desc;
    String image;
    String category;
    String duration;
    String location;
    String latitude;
    String longitude;
    String skills_required;
    String tools_needed;
    String cost;
    int country_id;
    String status;
    String start_date;

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    String other_info;
    String created_at;
    String status_accepted;
    String payment_amount;
    String payment_status;

    public String getHelper_amount() {
        return helper_amount;
    }

    public void setHelper_amount(String helper_amount) {
        this.helper_amount = helper_amount;
    }

    String helper_amount;

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getStatus_accepted() {
        return status_accepted;
    }

    public void setStatus_accepted(String status_accepted) {
        this.status_accepted = status_accepted;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    String end_date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    public String getUser_cost() {
        return user_cost;
    }

    public void setUser_cost(String user_cost) {
        this.user_cost = user_cost;
    }

    public String getUser_job_desc() {
        return user_job_desc;
    }

    public void setUser_job_desc(String user_job_desc) {
        this.user_job_desc = user_job_desc;
    }

    String user_cost;
    String user_job_desc;

    public int getApplicant_id() {
        return applicant_id;
    }

    public void setApplicant_id(int applicant_id) {
        this.applicant_id = applicant_id;
    }

    int applicant_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    int user_id;




    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

    Pivot pivot;

    @Keep
    public class Pivot{
        String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getJob_id() {
            return job_id;
        }

        public void setJob_id(int job_id) {
            this.job_id = job_id;
        }

        int job_id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getLong_desc() {
        return long_desc;
    }

    public void setLong_desc(String long_desc) {
        this.long_desc = long_desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSkills_required() {
        return skills_required;
    }

    public void setSkills_required(String skills_required) {
        this.skills_required = skills_required;
    }

    public String getTools_needed() {
        return tools_needed;
    }

    public void setTools_needed(String tools_needed) {
        this.tools_needed = tools_needed;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    String updated_at;

}

