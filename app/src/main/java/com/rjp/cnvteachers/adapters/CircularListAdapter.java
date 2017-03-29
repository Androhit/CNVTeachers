package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rjp.cnvteachers.NoticeDetailView;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.CircularBean;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 3/27/2017.
 */

public class CircularListAdapter extends RecyclerView.Adapter<CircularListAdapter.MyViewHolder>{

    private ArrayList<CircularBean> taskList;
    private ArrayList<CircularBean> arraylist;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date,desc,tvMsg,tvMore,tvMonth,tvYear;
        private ItemClickListener clickListener;
        public LinearLayout layMain;
        public ImageView ivDownload;

        public MyViewHolder(View view)
        {
            super(view);
            date = (TextView) view.findViewById(R.id.tvDate);
            tvMonth = (TextView) view.findViewById(R.id.tvMonth);
            tvYear = (TextView) view.findViewById(R.id.tvYear);
            desc = (TextView) view.findViewById(R.id.tvBookName);
            tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            tvMore = (TextView) view.findViewById(R.id.tvMore);
            layMain = (LinearLayout)view.findViewById(R.id.layMain);
            ivDownload = (ImageView)view.findViewById(R.id.ivDownload);

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

    public CircularListAdapter(Context cont, ArrayList<CircularBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<CircularBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.circular_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CircularBean objStud = taskList.get(position);
        holder.date.setText(DateOperations.getDay(objStud.getCir_date()));
        holder.tvMonth.setText(DateOperations.getMonthName(objStud.getCir_date()));
        holder.tvYear.setText(DateOperations.getYear(objStud.getCir_date()));

        //data == html data which you want to load
        //holder.desc.getSettings().setJavaScriptEnabled(true);
        //holder.desc.loadDataWithBaseURL("", objStud.getMsg_text(), "text/html", "UTF-8", "");

        holder.desc.setText(objStud.getCircular_title());
        if(objStud.getMsg_text()!=null)
        {
            holder.tvMsg.setText(Html.fromHtml(objStud.getMsg_text().trim()));
            holder.tvMore.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tvMore.setVisibility(View.GONE);
        }

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showEvent(objStud);
                Intent it  = new Intent(mContext, NoticeDetailView.class);
                it.putExtra("ObjNotice",objStud);
                mContext.startActivity(it);
            }
        });

        holder.tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showEvent(objStud);
                Intent it  = new Intent(mContext, NoticeDetailView.class);
                  it.putExtra("ObjNotice",objStud);
               mContext.startActivity(it);
            }
        });
/*
        if(objStud.getAttachment()!=null) {
            holder.ivDownload.setVisibility(View.VISIBLE);
            holder.ivDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonFunctions.viewAttachmentFile(mContext,objStud.getAttachment());
                }
            });
        }
        else
        {
            holder.ivDownload.setVisibility(View.GONE);
        }*/

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
        taskList.clear();
        if (charText.length() == 0) {
            taskList.addAll(arraylist);
        } else {
            for (CircularBean wp : arraylist) {
                if (wp.getCir_date().toLowerCase(Locale.getDefault())
                        .contains(charText)|| wp.getCircular_title().toLowerCase(Locale.getDefault())
                        .contains(charText) || wp.getCir_date().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
