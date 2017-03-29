package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.AchievementsBean;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Arun on 3/29/2017.
 */
public class AchievmentListAdapter extends RecyclerView.Adapter<AchievmentListAdapter.MyViewHolder>{
    private ArrayList<AchievementsBean> taskList;
    private ArrayList<AchievementsBean> arraylist;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date,desc,rank,rank1,tvBy,tvSubEvent,tvVenue;
        public ImageView ivRank;
        private ItemClickListener clickListener;

        public MyViewHolder(View view)
        {
            super(view);
            date = (TextView) view.findViewById(R.id.tvDate);
            desc = (TextView) view.findViewById(R.id.tvBookName);
            rank= (TextView) view.findViewById(R.id.tvRank);
            rank1= (TextView) view.findViewById(R.id.tvRank1);
            tvSubEvent= (TextView) view.findViewById(R.id.tvSubEvent);
            tvVenue= (TextView) view.findViewById(R.id.tvVenue);
            tvBy= (TextView) view.findViewById(R.id.tvBy);
            ivRank = (ImageView)view.findViewById(R.id.ivRank);

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

    public AchievmentListAdapter(Context cont, ArrayList<AchievementsBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<AchievementsBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goodnews_list_items2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AchievementsBean objStud = taskList.get(position);
        holder.date.setText(DateOperations.convertToddMMMyyyy(objStud.getEvent_date()));

        //data == html data which you want to load
        //holder.desc.getSettings().setJavaScriptEnabled(true);
        //holder.desc.loadDataWithBaseURL("", objStud.getMsg_text(), "text/html", "UTF-8", "");

        holder.rank.setText(objStud.getAchivement());
        holder.desc.setText(objStud.getEvent_name());
        holder.tvSubEvent.setText(objStud.getSubevent_name());
        holder.tvVenue.setText(objStud.getVenu());
        holder.tvBy.setText(objStud.getOrganized_by());


        if(objStud.getAchivement()!=null)
        {
            if(objStud.getAchivement().length()>10)
            {
                holder.rank1.setVisibility(View.VISIBLE);
                holder.rank1.setText(objStud.getAchivement());
                holder.rank.setText("");
            }
            else
            {
                holder.rank1.setVisibility(View.GONE);
                holder.rank.setText(objStud.getAchivement());
                holder.rank1.setText("");
            }

            if(objStud.getAchivement().contains("Gold") || objStud.getAchivement().contains("1") || objStud.getAchivement().contains("First"))
            {
                holder.ivRank.setImageDrawable(mContext.getResources().getDrawable(R.drawable.first_rank));
            }
            else if(objStud.getAchivement().contains("Silver") || objStud.getAchivement().contains("2") || objStud.getAchivement().contains("Second"))
            {
                holder.ivRank.setImageDrawable(mContext.getResources().getDrawable(R.drawable.second_rank));
            }
            else if(objStud.getAchivement().contains("Bronze") || objStud.getAchivement().contains("3") || objStud.getAchivement().contains("Third"))
            {
                holder.ivRank.setImageDrawable(mContext.getResources().getDrawable(R.drawable.third_rank));
            }
            else
            {
                holder.ivRank.setImageDrawable(mContext.getResources().getDrawable(R.drawable.medal));
            }
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else
                {
                    //showEvent(objStud);
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
        taskList.clear();
        if (charText.length() == 0) {
            taskList.addAll(arraylist);
        } else {
            for (AchievementsBean wp : arraylist) {
                if (wp.getEvent_name().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getOrganized_by().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getAchivement().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
