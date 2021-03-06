package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rjp.cnvteachers.ExamResult;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.beans.ExamResultsBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 4/5/2017.
 */
public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.MyViewHolder>  {
    private ArrayList<ExamBean> taskList;
    private ArrayList<ExamBean> arraylist;
    private ArrayList<ExamResultsBean> arrList = new ArrayList<ExamResultsBean>();

    private Context mContext;
    private API retrofitApi;
    public Spinner spnClass;
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();

    private int marks = 0,outOff = 0;
    private ClassBean objClass;
    public String Classid;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvStudName, tvRollNo, tvGRNo,  tvMore;


        private ItemClickListener clickListener;
        private ImageView ivGrade;

        public MyViewHolder(View view) {
            super(view);
            tvStudName = (TextView) view.findViewById(R.id.tvStudName);
            tvRollNo = (TextView) view.findViewById(R.id.tvAdmno);
            tvGRNo = (TextView) view.findViewById(R.id.tvGRNo);
            tvMore = (TextView) view.findViewById(R.id.tvMore);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            // clickListener.onClick(view, getPosition(), false);
        }
    }
    public PerformanceAdapter(Context mContext, ArrayList<ExamBean> arrExam) {
        this.taskList = arrExam;
        this.arraylist = new ArrayList<ExamBean>();
        this.arraylist.addAll(arrExam);
        this.mContext = mContext;
        initRetrofitClient();
    }


    @Override
    public PerformanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.performance_list_items, parent, false);

        return new PerformanceAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PerformanceAdapter.MyViewHolder holder, int position) {
        final ExamBean objStud = taskList.get(position);
        holder.tvStudName.setText(objStud.getStud_name());
        holder.tvRollNo.setText(objStud.getAdmno());
        holder.tvGRNo.setText(objStud.getGr_no());
/*
       if(objStud.getGrade()!=null) {
            TextDrawable drawable = null;
            if(objStud.getGrade().contains("A")) {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.green_600));
            }
            else if(objStud.getGrade().contains("B"))
            {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.orange_600));
            }
            else
            {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.red_600));
            }

           holder.ivGrade.setImageDrawable(drawable);
        }
*/



        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(mContext, ExamResult.class);
                it.putExtra("objExam",objStud);
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        taskList.clear();
        if (charText.length() == 0) {
            taskList.addAll(arraylist);
        } else {
            for (ExamBean wp : arraylist) {
                if (wp.getStud_name().toLowerCase(Locale.getDefault())
                        .contains(charText)|| wp.getAdmno().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getGr_no().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }



    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }
}
