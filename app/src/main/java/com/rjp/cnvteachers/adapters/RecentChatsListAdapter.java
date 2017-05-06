package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rjp.cnvteachers.ChatRoomActivity;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.FcmNotificationBean;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class RecentChatsListAdapter extends RecyclerView.Adapter<RecentChatsListAdapter.MyViewHolder> {
    private ArrayList<FcmNotificationBean> taskList;
    private ArrayList<FcmNotificationBean> arraylist;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName,tvMessage,tvDate;
        private ImageView ivPic;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvUserName);
            tvMessage = (TextView) view.findViewById(R.id.tvMsg);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            ivPic = (ImageView)view.findViewById(R.id.ivAvatar);
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

    public RecentChatsListAdapter(Context cont, ArrayList<FcmNotificationBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<FcmNotificationBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_chats_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final FcmNotificationBean objStud = taskList.get(position);
        if(objStud.getSent_by()==0)
        {
            holder.tvName.setText(objStud.getSender_name());
        }
        else
        {
            holder.tvName.setText(objStud.getReceiver_name());
        }

        holder.tvMessage.setText(""+objStud.getMessage());
        holder.tvDate.setText(""+ DateOperations.getTimeStamp(objStud.getEntry_date()));

        //Log.e("Adapter ","Path "+objStud.getPhoto_url());
        //if(objStud.getPhoto_url()!=null)
        //Picasso.with(mContext).load(objStud.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.ivPic);
        //Picasso.with(mContext).load(objStud.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.ivPic);


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else
                {
                    Intent it = new Intent(mContext, ChatRoomActivity.class);
                    it.putExtra("objChats",objStud);
                    mContext.startActivity(it);
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
            for (FcmNotificationBean wp : arraylist) {
                if (wp.getSender_name().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getReceiver_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
