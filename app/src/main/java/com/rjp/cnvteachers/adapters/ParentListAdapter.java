package com.rjp.cnvteachers.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import com.rjp.cnvteachers.ChatRoomActivity;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.ParentBean;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.MyViewHolder> {

    private ArrayList<ParentBean> taskList;
    private ArrayList<ParentBean> arraylist;
    private Context mContext;
    private AlertDialog mainDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemClickListener clickListener;
        private TextView tvName;
        private CircularImageView ivAvatar;

        public MyViewHolder(View view)
        {
            super(view);

            tvName = (TextView)view.findViewById(R.id.tvEmpName);
            ivAvatar = (CircularImageView)view.findViewById(R.id.ivAvatar);

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

    public ParentListAdapter(Context cont, ArrayList<ParentBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ParentBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ParentBean obj = taskList.get(position);
        holder.tvName.setText(obj.getFathername()+" "+obj.getLastname());
        if(obj.getPhoto_url()!=null)
            //Picasso.with(mContext).load(objEmp.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.ivAvatar);
            Glide.with(mContext).load(obj.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.ivAvatar);
        //data == html data which you want to load
        //holder.desc.getSettings().setJavaScriptEnabled(true);
        //holder.desc.loadDataWithBaseURL("", objEmp.getMsg_text(), "text/html", "UTF-8", "");

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(mainDialog!=null)
                {
                    mainDialog.dismiss();
                }
                Intent it = new Intent(mContext, ChatRoomActivity.class);
                it.putExtra("obj",obj);
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
            for (ParentBean wp : arraylist) {
                if (wp.getFathername().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getLastname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
