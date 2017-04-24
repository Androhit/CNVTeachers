package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by Shraddha on 4/19/2017.
 */

public class WorksheetStudentAdapter extends RecyclerView.Adapter<WorksheetStudentAdapter.MyViewHolder>{

    public static StudentBean obj = null;

    private Context mContext;
    private ArrayList<StudentBean> taskList;
    private ArrayList<StudentBean> arraylist;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,admno;

        private ItemClickListener clickListener;


        public MyViewHolder(View view) {
            super(view);
            Name = (TextView) view.findViewById(R.id.tvName);
            admno = (TextView) view.findViewById(R.id.tvAdmno);

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



    public WorksheetStudentAdapter(Context mContext, ArrayList<StudentBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<StudentBean>();
        this.arraylist.addAll(arrList);
        this.mContext = mContext;

    }

    @Override
    public WorksheetStudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stud_list_items, parent, false);
        return new WorksheetStudentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorksheetStudentAdapter.MyViewHolder holder, int position) {
        final StudentBean obj = taskList.get(position);

        holder.Name.setText(""+obj.getName());
        holder.admno.setText(""+obj.getAdmno());
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
