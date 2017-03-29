package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.AttendanceBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/21/2017.
 */

public class AttendanceClassFragmentAdapter extends RecyclerView.Adapter<AttendanceClassFragmentAdapter.MyViewHolder>{

    private final Context mContext;


    private ArrayList<AttendanceBean> taskList;
    private ArrayList<AttendanceBean> arraylist;
    private LinearLayout layMain;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, tvTotalDays,tvPresentDays,tvAbsentDays,tvAttendance;

        private ItemClickListener clickListener;

        public CircularImageView StudPic;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvStudName);
            tvTotalDays = (TextView) view.findViewById(R.id.tvTotalDays);
            tvPresentDays = (TextView) view.findViewById(R.id.tvPresentDays);
            tvAbsentDays = (TextView) view.findViewById(R.id.tvAbsentDays);
            tvAttendance=(TextView) view.findViewById(R.id.tvAttendance);
            layMain=(LinearLayout)view.findViewById(R.id.layMain);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            //this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            //clickListener.onClick(view, getPosition(), false);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public AttendanceClassFragmentAdapter(Context cont, ArrayList<AttendanceBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<AttendanceBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
        Log.e("Data"," "+arrList.size());
    }

    @Override
    public AttendanceClassFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_stud_att_items, parent, false);
        return new AttendanceClassFragmentAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final AttendanceBean obj =taskList.get(position);

        if(position == 0)
        {
            layMain.setVisibility(View.VISIBLE);

        }
        else
        {
            layMain.setVisibility(View.GONE);

        }

        holder.name.setText(""+obj.getName());
        holder.tvAbsentDays.setText(""+obj.getAbsent_days());
        holder.tvPresentDays.setText(""+obj.getPresent_day());
        holder.tvTotalDays.setText(""+obj.getWorking_days());
        holder.tvAttendance.setText(""+obj.getPercent());


    }


}



