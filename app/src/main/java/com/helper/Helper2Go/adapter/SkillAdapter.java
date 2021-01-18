package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.helper.Helper2Go.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {


    Context context;

    List<String> checkBoxList = new ArrayList<>();

    public SkillAdapter(Context context, List<String> checkBoxList){
        this.context = context;
        this.checkBoxList = checkBoxList;
    }

    @NonNull
    @Override
    public SkillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.skills_item, parent, false);

        SkillAdapter.MyViewHolder myViewHolder = new SkillAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SkillAdapter.MyViewHolder holder, int position) {
        if (checkBoxList.get(position).equalsIgnoreCase("null"))
        {
            holder.checkbox.setText("NA");
        }
        else {
            holder.checkbox.setText(checkBoxList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return checkBoxList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

}

