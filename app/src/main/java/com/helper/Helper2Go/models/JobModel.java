package com.helper.Helper2Go.models;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class JobModel {
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

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    String tools_needed;
    String cost;
    String end_date;
    int country_id;
    int status;

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

    public void setShort_desc(String short_desc)
    {
        this.short_desc = short_desc;
    }

    public String getLong_desc()
    {
        return long_desc;
    }

    public void setLong_desc(String long_desc)
    {
        this.long_desc = long_desc;
    }

    public String getImage()
    {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    String created_at;
    String updated_at;

}
