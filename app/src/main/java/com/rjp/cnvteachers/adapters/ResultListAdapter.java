package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.ExamResultsBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Shraddha on 4/5/2017.
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.MyViewHolder>
{

    private ArrayList<ExamResultsBean> taskList;
    private ArrayList<ExamResultsBean> arraylist;
    private TextView tvMarks,tvOutOff,tvPer,tvGrade;
    private Context mContext;
    private int marks = 0,outOff = 0, perc=0;

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subject,marks,outoff,per,grade;
        private ItemClickListener clickListener;

        public MyViewHolder(View view)
        {
            super(view);
            subject= (TextView) view.findViewById(R.id.tvResultSub);
            marks = (TextView) view.findViewById(R.id.tvResultMarks);
            outoff = (TextView) view.findViewById(R.id.tvResultOutOff);
            per = (TextView) view.findViewById(R.id.tvResultPer);
            grade = (TextView) view.findViewById(R.id.tvResultGrade);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
    }

    public ResultListAdapter(Context cont, ArrayList<ExamResultsBean> arrList, TextView tvMarks, TextView tvOutOff, TextView tvPer, TextView tvGrade) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ExamResultsBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
        this.tvMarks = tvMarks;
        this.tvOutOff = tvOutOff;
        this.tvPer = tvPer;
        this.tvGrade = tvGrade;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_result_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ExamResultsBean objStud = taskList.get(position);

     /*   int s = objStud.getData_array().size();
        int i;
        for(i=0;i<s;i++)
        {
            holder.subject.setText(objStud.getData_array().get(i).getSub_name());
            holder.marks.setText(objStud.getData_array().get(i).getMarks());
            holder.outoff.setText(objStud.getData_array().get(i).getMax_marks());
            holder.per.setText(objStud.getData_array().get(i).getPercentage());
            holder.grade.setText(objStud.getData_array().get(i).getGrade());
        }*/

        holder.subject.setText(objStud.getSub_name());
        holder.marks.setText(objStud.getMarks());
        holder.outoff.setText(objStud.getMax_marks());
        holder.per.setText(objStud.getPercentage());
        holder.grade.setText(objStud.getGrade());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else
                {
                    //showResults();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
