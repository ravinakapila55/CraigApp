package com.helper.Helper2Go.models;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;

@Keep
public class JobFilterParam {

    List<String> job_duration = new ArrayList<>();
    List<NameModel> skills = new ArrayList<>();

    public List<String> getJob_duration() {
        return job_duration;
    }

    public void setJob_duration(List<String> job_duration) {
        this.job_duration = job_duration;
    }

    public List<NameModel> getSkills() {
        return skills;
    }

    public void setSkills(List<NameModel> skills) {
        this.skills = skills;
    }

    public List<NameModel> getTools() {
        return tools;
    }

    public void setTools(List<NameModel> tools) {
        this.tools = tools;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    List<NameModel> tools = new ArrayList<>();
    String distance;


    @Keep
    public class NameModel{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        int id;
    }

}
