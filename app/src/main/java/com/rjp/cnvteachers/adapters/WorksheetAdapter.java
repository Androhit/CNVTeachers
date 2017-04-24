package com.rjp.cnvteachers.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.AddWorksheet;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.SchoolGallery;
import com.rjp.cnvteachers.WorksheetDetails;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.WorksheetBean;
import com.rjp.cnvteachers.common.CommonFunctions;
import com.rjp.cnvteachers.common.ItemClickListener;
import com.rjp.cnvteachers.utils.AppPreferences;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 4/18/2017.
 */

public class WorksheetAdapter extends RecyclerView.Adapter<WorksheetAdapter.MyViewHolder>{
    public static WorksheetBean obj = null;

    private Context mContext;
    private ArrayList<WorksheetBean> taskList;
    private ArrayList<WorksheetBean> arraylist;
    private LinearLayout laymain;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private String TAG="Worksheet";
    private final int GALLERY_AVAILABLE = 0, GALLERY_UNAVAILABLE = 1;
    String empid,eid;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Aim,Title,Desc,From,To,GuidedBy,Class_id,division,tvMore;
        public ImageView fileUpload,ivGallery;
        public TextView fabEdit;

        public MyViewHolder(View view) {
            super(view);
            Aim = (TextView) view.findViewById(R.id.tvAim);
            Title = (TextView) view.findViewById(R.id.tvTitle);
            Desc = (TextView) view.findViewById(R.id.tvDesc);
            From = (TextView) view.findViewById(R.id.tvFrom);

            GuidedBy = (TextView) view.findViewById(R.id.tvEmp);
            fileUpload = (ImageView) view.findViewById(R.id.ivAttachment);
            ivGallery = (ImageView) view.findViewById(R.id.ivGallery);
            tvMore = (TextView) view.findViewById(R.id.tvMore);
            fabEdit = (TextView) view.findViewById(R.id.fab);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
       //     this.clickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
         //   clickListener.onClick(view, getPosition(), false);
        }
    }


    public WorksheetAdapter(Context mContext, ArrayList<WorksheetBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<WorksheetBean>();
        this.arraylist.addAll(arrList);
        this.mContext = mContext;
    }

    @Override
    public WorksheetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        switch (viewType) {
            case GALLERY_AVAILABLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.worksheet_list_items, parent, false);
                break;
            case GALLERY_UNAVAILABLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.worksheet_list_items1, parent, false);
                break;

        }
        //return viewHolder;

        return new MyViewHolder(itemView);
    }

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
    public void onBindViewHolder(WorksheetAdapter.MyViewHolder holder, int position) {
        final WorksheetBean obj = taskList.get(position);

        switch (holder.getItemViewType()) {
            case GALLERY_AVAILABLE:
                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //  showEvent(obj);
                            Intent it = new Intent(mContext, WorksheetDetails.class);
                            it.putExtra("ObjWorksheet", obj);
                            mContext.startActivity(it);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.Aim.setText("" + obj.getAim());
                holder.Title.setText("" + obj.getTitle());
                holder.Desc.setText("" + obj.getDesc());
                holder.GuidedBy.setText("" + obj.getEmpname());
                holder.From.setText("" + obj.getFromDate());
                if (obj.getCount().size() > 0) {
                    //holder.ivGallery.setVisibility(View.VISIBLE);

                    //Picasso.with(mContext).load(RetrofitClient.ROOT_URL + "" + objStud.getCount().get(0)).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(holder.ivGallery);
                    Glide.with(mContext).load(RetrofitClient.ROOT_URL + "" + obj.getCount().get(0)).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(holder.ivGallery);
                }

                holder.ivGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (obj.getCount().size() > 0) {
                            Intent it = new Intent(mContext, SchoolGallery.class);
                            it.putExtra("ObjScience", obj);
                            mContext.startActivity(it);
                        }
                    }
                });

                if(obj.getAttachment()!=null)
                {
                    holder.fileUpload.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.fileUpload.setVisibility(View.INVISIBLE);
                }

                holder.fileUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            CommonFunctions.viewAttachmentFile(mContext,obj.getAttachment());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                empid= AppPreferences.getLoginObj(mContext).getEmpid();
                eid= obj.getEmpid();

                if(empid.equals(eid))
                {
                    holder.fabEdit.setVisibility(View.VISIBLE);

                    holder.fabEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final ProgressDialog prog = new ProgressDialog(mContext);
                            prog.setMessage("Wait...");
                            prog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    prog.dismiss();
                                  //  makeEditable();
                                }
                            }, 500);
                        }
                    });

                }

                break;

            case GALLERY_UNAVAILABLE:

                holder.tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            //  showEvent(obj);
                            Intent it = new Intent(mContext, WorksheetDetails.class);
                            it.putExtra("ObjWorksheet", obj);
                            mContext.startActivity(it);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.Aim.setText("" + obj.getAim());
                holder.Title.setText("" + obj.getTitle());
                holder.Desc.setText("" + obj.getDesc());
                holder.GuidedBy.setText("" + obj.getEmpname());
                holder.From.setText("" + obj.getFromDate());

                Log.e(TAG,"Attachment"+obj.getAttachment());

                if(obj.getAttachment()!=null)
                {
                    holder.fileUpload.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.fileUpload.setVisibility(View.GONE);
                }

                holder.fileUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            CommonFunctions.viewAttachmentFile(mContext,obj.getAttachment());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                empid= AppPreferences.getLoginObj(mContext).getEmpid();
                eid= obj.getEmpid();

                if(empid.equals(eid))
                {
                    holder.fabEdit.setVisibility(View.VISIBLE);

                    holder.fabEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final ProgressDialog prog = new ProgressDialog(mContext);
                            prog.setMessage("Wait...");
                            prog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    prog.dismiss();
                                    try {
                                        Intent it = new Intent(mContext, AddWorksheet.class);
                                        it.putExtra("ObjWorksheet", obj);
                                        mContext.startActivity(it);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 500);
                        }
                    });

                }


                break;

        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public void filter(String charText) {
        try {
            charText = charText.toLowerCase(Locale.getDefault());
            taskList.clear();
            if (charText.length() == 0) {
                taskList.addAll(arraylist);
            } else {
                for (WorksheetBean wp : arraylist) {
                    if (wp.getAim().toLowerCase(Locale.getDefault())
                            .contains(charText) || wp.getDesc().toLowerCase(Locale.getDefault())
                            .contains(charText) || wp.getFromDate().toLowerCase(Locale.getDefault())
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

}
