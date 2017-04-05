package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjp.cnvteachers.ExamResult;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Shraddha on 4/5/2017.
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.MyViewHolder>
{

    private ArrayList<ExamResult> taskList;
    private ArrayList<ExamResult> arraylist;
    private TextView tvMarks,tvOutOff,tvPer,tvGrade;
    private Context mContext;

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

    public ResultListAdapter(Context cont, ArrayList<ExamResult> arrList, TextView tvMarks, TextView tvOutOff, TextView tvPer, TextView tvGrade) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ExamResult>();
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
        final ExamResult objStud = taskList.get(position);
     /*   holder.subject.setText(objStud.getSub_name());

        holder.outoff.setText(objStud.getMax_marks());
        marks = marks + Integer.parseInt(objStud.getMarks());
        outOff = outOff + Integer.parseInt(objStud.getMax_marks());

        tvOutOff.setText(""+outOff);

        if(objStud.getGrade()!=null) {
            holder.marks.setText(objStud.getGrade());
        }
        else
        {
            holder.marks.setText("-");
        }


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
        */
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
