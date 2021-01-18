package com.helper.Helper2Go.models;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;

@Keep
public class JobModelForClient
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

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    String tools_needed;
    String cost;
    String max_price;
    String payment_status;

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
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

    String start_date;
    String end_date;


    public String getApplicants_required() {
        return applicants_required;
    }

    public void setApplicants_required(String applicants_required) {
        this.applicants_required = applicants_required;
    }

    String applicants_required;

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

    public List<Applicants> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Applicants> applicants) {
        this.applicants = applicants;
    }

    List<Applicants> applicants = new ArrayList<>();


    @Keep
    public class Applicants {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        String name;
        String email;

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        String phone_no;
        String gender;
        String payment_status;


        String address;
        String profile_image;
        String country_code;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public int getAdmin_approval() {
            return admin_approval;
        }

        public void setAdmin_approval(int admin_approval) {
            this.admin_approval = admin_approval;
        }

        public int getApplicant_id() {
            return applicant_id;
        }

        public void setApplicant_id(int applicant_id) {
            this.applicant_id = applicant_id;
        }

        String user_cost;
        String payment_amount;

        public String getUser_job_desc() {
            return user_job_desc;
        }

        public void setUser_job_desc(String user_job_desc) {
            this.user_job_desc = user_job_desc;
        }

        public String getOther_info() {
            return other_info;
        }

        public void setOther_info(String other_info) {
            this.other_info = other_info;
        }

        String user_job_desc;
        String other_info;
        String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        int admin_approval;

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

        int applicant_id;








        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUser_cost() {
            return user_cost;
        }

        public void setUser_cost(String user_cost) {
            this.user_cost = user_cost;
        }



    }
}
