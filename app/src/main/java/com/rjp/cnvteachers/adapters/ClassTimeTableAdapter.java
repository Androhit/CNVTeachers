package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.ClassTimetableBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;
/**
 * Created by Shraddha on 3/18/2017.
 */

public class ClassTimeTableAdapter extends RecyclerView.Adapter<ClassTimeTableAdapter.MyViewHolder> {

    private ArrayList<ClassTimetableBean> taskList;
    private ArrayList<ClassTimetableBean> arraylist;
    private Context mContext;
    private int[] clr = {R.color.colorPrimaryDark,R.color.amber_600,R.color.green_600,R.color.brown_500,R.color.red_600,R.color.pink_600,R.color.yellow_700,R.color.blue_800,R.color.purple_700,R.color.teal_700,R.color.black,R.color.colorPrimaryDark,R.color.amber_600,R.color.green_600,R.color.brown_500,R.color.red_600,R.color.pink_600,R.color.yellow_700,R.color.blue_800,R.color.purple_700,R.color.teal_700,R.color.black};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subject,timefrom,timeto; public TextView teacher;
        private ImageView ivPeriod;
        private ItemClickListener clickListener;

        public MyViewHolder(View view)
        {
            super(view);
            subject = (TextView) view.findViewById(R.id.tvSubject);
            teacher = (TextView) view.findViewById(R.id.tvTeacher);
            timefrom = (TextView) view.findViewById(R.id.tvTimeFrom);
            timeto = (TextView) view.findViewById(R.id.tvTimeTo);
            ivPeriod = (ImageView) view.findViewById(R.id.ivPeriod);

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

    public ClassTimeTableAdapter(Context cont, ArrayList<ClassTimetableBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ClassTimetableBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cls_time_table_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassTimetableBean obj = taskList.get(position);
        holder.subject.setText(obj.getSubject());

        //data == html data which you want to load
        //holder.desc.getSettings().setJavaScriptEnabled(true);
        //holder.desc.loadDataWithBaseURL("", objStud.getMsg_text(), "text/html", "UTF-8", "");

        holder.teacher.setText(obj.getEmp_name());
        holder.timefrom.setText(obj.getFromtime());
        holder.timeto.setText(obj.getTotime());

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(obj.getPeriod(), mContext.getResources().getColor(clr[position]));
        holder.ivPeriod.setImageDrawable(drawable);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else
                {
                    //showResults(objStud);
                }
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
        if(charText.contains("-"))
        {
            String[] str = charText.split("-");
            charText = str[1];
        }
        taskList.clear();
        if (charText.length() == 0) {
            taskList.addAll(arraylist);
        } else {
            for (ClassTimetableBean wp : arraylist) {
                if (wp.getEmp_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}