package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.SkillsModel;
import com.helper.Helper2Go.ui.AddJobActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SkillsAdapterForAddJob extends RecyclerView.Adapter<SkillsAdapterForAddJob.MyViewHolder> {


    Context context;

    List<SkillsModel> checkBoxList = new ArrayList<>();

    public SkillsAdapterForAddJob(Context context, List<SkillsModel> checkBoxList){
        this.context = context;
        this.checkBoxList = checkBoxList;
    }

    @NonNull
    @Override
    public SkillsAdapterForAddJob.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skills_list, parent, false);

        SkillsAdapterForAddJob.MyViewHolder myViewHolder = new SkillsAdapterForAddJob.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsAdapterForAddJob.MyViewHolder holder, int position) {
        holder.checkbox.setText(checkBoxList.get(position).getName());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((AddJobActivity)context).selectedSkillsCheckbox(position, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkBoxList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}


