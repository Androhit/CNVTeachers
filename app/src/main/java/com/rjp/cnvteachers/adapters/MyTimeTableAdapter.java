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
import com.rjp.cnvteachers.beans.MyTimeTableBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/20/2017.
 */

public class MyTimeTableAdapter extends RecyclerView.Adapter<MyTimeTableAdapter.MyViewHolder> {

    private ArrayList<MyTimeTableBean> taskList;
    private ArrayList<MyTimeTableBean> arraylist;
    private Context mContext;
    private int[] clr = {R.color.colorPrimaryDark, R.color.amber_600, R.color.green_600, R.color.brown_500, R.color.red_600, R.color.pink_600, R.color.yellow_700, R.color.blue_800, R.color.purple_700, R.color.teal_700, R.color.black, R.color.colorPrimaryDark, R.color.amber_600, R.color.green_600, R.color.brown_500, R.color.red_600, R.color.pink_600, R.color.yellow_700, R.color.blue_800, R.color.purple_700, R.color.teal_700, R.color.black};

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subject, timefrom, timeto;
        public TextView division,class_name;
        private ImageView ivPeriod;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            subject = (TextView) view.findViewById(R.id.tvSubject);
            division = (TextView) view.findViewById(R.id.tvDivision);
            timefrom = (TextView) view.findViewById(R.id.tvTimeFrom);
            timeto = (TextView) view.findViewById(R.id.tvTimeTo);
            ivPeriod = (ImageView) view.findViewById(R.id.ivPeriod);
            class_name = (TextView) view.findViewById(R.id.tvClass);
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

    public MyTimeTableAdapter(Context cont, ArrayList<MyTimeTableBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<MyTimeTableBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyTimeTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_time_table_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyTimeTableAdapter.MyViewHolder holder, int position) {
        final MyTimeTableBean obj = taskList.get(position);
        holder.subject.setText(obj.getSubject_full());

        //data == html data which you want to load
        //holder.desc.getSettings().setJavaScriptEnabled(true);
        //holder.desc.loadDataWithBaseURL("", objStud.getMsg_text(), "text/html", "UTF-8", "");

        holder.division.setText(obj.getDivision());
        holder.timefrom.setText(obj.getFromtime());
        holder.timeto.setText(obj.getTotime());
        holder.class_name.setText(obj.getClass_name());


        TextDrawable drawable = TextDrawable.builder()
                .buildRect(obj.getPeriod(), mContext.getResources().getColor(clr[position]));
        holder.ivPeriod.setImageDrawable(drawable);

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    //showResults(objStud);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
