package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.SchoolGallery;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.HandsOnScienceBeans;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class HandsOnScienceListAdapter   extends RecyclerView.Adapter<HandsOnScienceListAdapter.MyViewHolder> {
    private ArrayList<HandsOnScienceBeans> taskList;
    private ArrayList<HandsOnScienceBeans> arraylist;
    private Context mContext;

    private final int GALLERY_AVAILABLE = 0, GALLERY_UNAVAILABLE = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date; public TextView desc,tvMore;
        public TextView title,tvAim;
        private ItemClickListener clickListener;
        private ImageView ivGallery;


        public MyViewHolder(View view)
        {
            super(view);
            title= (TextView) view.findViewById(R.id.tvTitle);
            tvAim= (TextView) view.findViewById(R.id.tvAim);
            date = (TextView) view.findViewById(R.id.tvDate);
            desc = (TextView) view.findViewById(R.id.tvDesc);
            tvMore = (TextView) view.findViewById(R.id.tvMore);
            ivGallery = (ImageView)view.findViewById(R.id.ivGallery);

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

    public HandsOnScienceListAdapter(Context cont, ArrayList<HandsOnScienceBeans> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<HandsOnScienceBeans>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        switch (viewType) {
            case GALLERY_AVAILABLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hands_on_sci_list_items2, parent, false);
                break;
            case GALLERY_UNAVAILABLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.hands_on_sci_list_items, parent, false);
                break;

        }
        //return viewHolder;

        return new MyViewHolder(itemView);
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if(taskList.get(position).getCount().size() > 0) {
            return GALLERY_AVAILABLE;
        } else
        {
            return GALLERY_UNAVAILABLE;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HandsOnScienceBeans objStud = taskList.get(position);
        switch (holder.getItemViewType()) {
            case GALLERY_AVAILABLE:
                holder.date.setText(DateOperations.convertToddMMMyyyy(objStud.getFrom_date()) + " to " + DateOperations.convertToddMMMyyyy(objStud.getTo_date()));
                holder.tvAim.setText(objStud.getProj_aim());
                holder.title.setText(objStud.getProj_title());
                holder.desc.setText(objStud.getProj_disc());
                if(objStud.getProj_disc()!=null)
                {
                    if(objStud.getProj_disc().length()>100)
                    {
                        holder.tvMore.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.tvMore.setVisibility(View.GONE);

                    }
                }
                else
                {
                    holder.tvMore.setVisibility(View.GONE);
                }

                if (objStud.getCount().size() > 0) {
                    //holder.ivGallery.setVisibility(View.VISIBLE);

                    //Picasso.with(mContext).load(RetrofitClient.ROOT_URL + "" + objStud.getCount().get(0)).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(holder.ivGallery);
                    Glide.with(mContext).load(RetrofitClient.ROOT_URL + "" + objStud.getCount().get(0)).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(holder.ivGallery);

                }

                holder.ivGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (objStud.getCount().size() > 0) {
                            Intent it = new Intent(mContext, SchoolGallery.class);
                            it.putExtra("ObjScience", objStud);
                            mContext.startActivity(it);
                        }
                    }
                });

                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            showEvent(objStud);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case GALLERY_UNAVAILABLE:

                holder.date.setText(DateOperations.convertToddMMMyyyy(objStud.getFrom_date()) + " to " + DateOperations.convertToddMMMyyyy(objStud.getTo_date()));
                holder.tvAim.setText(objStud.getProj_title());
                holder.title.setText(objStud.getProj_aim());
                holder.desc.setText(objStud.getProj_disc());
                holder.desc.setText(objStud.getProj_disc());
                if(objStud.getProj_disc()!=null)
                {
                    if(objStud.getProj_disc().length()>100)
                    {
                        holder.tvMore.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.tvMore.setVisibility(View.GONE);

                    }
                }
                else
                {
                    holder.tvMore.setVisibility(View.GONE);
                }

                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            showEvent(objStud);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Filter Class
    public void filter(String charText) {
        try {
            charText = charText.toLowerCase(Locale.getDefault());
            taskList.clear();
            if (charText.length() == 0) {
                taskList.addAll(arraylist);
            } else {
                for (HandsOnScienceBeans wp : arraylist) {
                    if (wp.getProj_aim().toLowerCase(Locale.getDefault())
                            .contains(charText) || wp.getProj_disc().toLowerCase(Locale.getDefault())
                            .contains(charText) || wp.getFrom_date().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        taskList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showEvent(HandsOnScienceBeans obj)
    {
        try {
            if(obj.getProj_disc()!=null) {
                if (obj.getProj_disc().length() > 0) {
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    View view = View.inflate(mContext, R.layout.view_events_dialog, null);
                    TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                    TextView tvDesc = (TextView) view.findViewById(R.id.tvDetails);
                    Button btOk = (Button)view.findViewById(R.id.btOk);

                    tvTitle.setText(""+DateOperations.convertToddMMMyyyy(obj.getFrom_date()));
                    tvDesc.setText(obj.getProj_disc().trim());

                    alert.setView(view);

                    btOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            alert.dismiss();
                        }
                    });

                    alert.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
