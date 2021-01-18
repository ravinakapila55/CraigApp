package com.helper.Helper2Go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.helper.Helper2Go.R;
import com.helper.Helper2Go.models.JobModelForClient;
import com.helper.Helper2Go.ui.fragments.MyJobsFragment;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<JobModelForClient> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<JobModelForClient, List<JobModelForClient.Applicants>> _listDataChild;

    MyJobsFragment myJobsFragment;

    public ExpandableAdapter(Context context, List<JobModelForClient> listDataHeader,
                             HashMap<JobModelForClient, List<JobModelForClient.Applicants>> listChildData, MyJobsFragment myJobsFragment) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.myJobsFragment = myJobsFragment;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final JobModelForClient.Applicants applicants = (JobModelForClient.Applicants) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txt_job_title = convertView.findViewById(R.id.txt_job_title);
        ImageView iv_accept_job = convertView.findViewById(R.id.iv_accept_job);
        ImageView iv_reject_job = convertView.findViewById(R.id.iv_reject_job);
        ImageView iv_complete = convertView.findViewById(R.id.iv_complete);
        LinearLayout ll_see_job_details = convertView.findViewById(R.id.ll_see_job_details);
        TextView txt_offer_status = convertView.findViewById(R.id.txt_offer_status);

        String status = "";
        int color = 0;

        if(applicants.getAdmin_approval() == 0){ // pending
            status = "";
            color = _context.getResources().getColor(R.color.black);
            txt_offer_status.setVisibility(View.GONE);
            iv_complete.setVisibility(View.GONE);
            iv_accept_job.setVisibility(View.VISIBLE);
            iv_reject_job.setVisibility(View.VISIBLE);
        }
        else if(applicants.getAdmin_approval() == 1){ // approved
            status = "Accepted";
            color = _context.getResources().getColor(R.color.card_green);
            txt_offer_status.setVisibility(View.VISIBLE);
            iv_complete.setVisibility(View.VISIBLE);
            iv_accept_job.setVisibility(View.GONE);
            iv_reject_job.setVisibility(View.GONE);
        } else if(applicants.getAdmin_approval() == 3){ // completed
            status = "Completed";
            color = _context.getResources().getColor(R.color.login_button_color);
            txt_offer_status.setVisibility(View.VISIBLE);
            iv_complete.setVisibility(View.GONE);
            iv_accept_job.setVisibility(View.GONE);
            iv_reject_job.setVisibility(View.GONE);
        }
        else if(applicants.getAdmin_approval() == 2){ // rejected
            status = "Rejected";
            color = _context.getResources().getColor(R.color.card_red);
            txt_offer_status.setVisibility(View.VISIBLE);
            iv_accept_job.setVisibility(View.GONE);
            iv_complete.setVisibility(View.GONE);
            iv_reject_job.setVisibility(View.GONE);
        }

        txt_offer_status.setText(status);
        txt_offer_status.setTextColor(color);

        txt_job_title.setText(applicants.getName());
        iv_accept_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobsFragment.acceptRejectJob("a", applicants);
            }
        });

        iv_reject_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobsFragment.acceptRejectJob("r", applicants);
            }
        });


        iv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobsFragment.acceptRejectJob("c", applicants);
            }
        });

        ll_see_job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myJobsFragment.seeDetails(applicants, _listDataHeader.get(groupPosition).getId() + "");
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        JobModelForClient headerTitle = (JobModelForClient) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        ImageView iv_arrow_my_jobs = convertView.findViewById(R.id.iv_arrow_my_jobs);
        if (isExpanded) {
            iv_arrow_my_jobs.setImageResource(R.drawable.ic_arrow_up);
        } else {
            iv_arrow_my_jobs.setImageResource(R.drawable.ic_down_arrow);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

