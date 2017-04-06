package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ExamDetailsBeans;
import com.rjp.cnvteachers.beans.ExamResultsBean;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 3/23/2017.
 */

public class ExamDetailsListAdapter extends RecyclerView.Adapter<ExamDetailsListAdapter.MyViewHolder> {



    private ArrayList<ExamDetailsBeans> taskList;
    private ArrayList<ExamDetailsBeans> arraylist;
    private Context mContext;
    private API retrofitApi;
    private ArrayList<ExamResultsBean> arrList = new ArrayList<ExamResultsBean>();
    private int marks = 0,outOff = 0;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subject, date, timefrom, timeto;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            subject = (TextView) view.findViewById(R.id.tvSubject);
            date = (TextView) view.findViewById(R.id.tvDate);
            timefrom = (TextView) view.findViewById(R.id.tvTimeFrom);
            timeto = (TextView) view.findViewById(R.id.tvTimeTo);

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

    public ExamDetailsListAdapter(Context cont, ArrayList<ExamDetailsBeans> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ExamDetailsBeans>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
        initRetrofitClient();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_details_list_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ExamDetailsBeans objStud = taskList.get(position);
        holder.subject.setText(objStud.getSub_name());
        holder.date.setText(DateOperations.convertToddMMMyyyy(objStud.getExam_date()));
        holder.timefrom.setText(objStud.getTime_from() + " -- ");
        holder.timeto.setText(objStud.getTime_to());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    //showEvent();
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
        if (charText.contains("-")) {
            String[] str = charText.split("-");
            charText = str[1];
        }
        taskList.clear();
        if (charText.length() == 0) {
            taskList.addAll(arraylist);
        } else {
            for (ExamDetailsBeans wp : arraylist) {
                if (wp.getExam_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }
    /*
    private ArrayList<ExamResults> getExamResultsService(final RecyclerView rv, final TextView tvMarks, final TextView tvOutOff) {
        try {
            if (NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("loading...");
                prog.setCancelable(false);
                prog.show();

                //RestAdapter retrofit = new RestAdapter.Builder()
                //        .setEndpoint("http://115.112.186.62/schoolems/mobandroid/exam_info.php?")
                //        .build();
                //API service = retrofit.create(API.class);
                //service.getExamResults("exam_marks", AppPreferences.getBranchId(mContext),"7","85", new Callback<ApiResults>() {
                retrofitApi.getExamResults("exam_marks", AppPreferences.getBranchId(mContext), new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResultses, Response response) {
                        arrList = apiResultses.getExm_all_sub();
                        if (arrList != null) {

                            if (arrList.size() >0)
                            {
                                ExamResultListAdapter adapt = new ExamResultListAdapter(mContext, arrList,tvMarks,tvOutOff);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                rv.setLayoutManager(mLayoutManager);
                                rv.setItemAnimator(new DefaultItemAnimator());
                                rv.setAdapter(adapt);
                            }
                            else
                            {
                                arrList = null;
                            }
                        }
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Res Adapt", "Error One Frag" + error.toString());
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                    }
                });
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrList;
    }

    class ExamResultListAdapter extends RecyclerView.Adapter<ExamResultListAdapter.MyViewHolder>
    {

        private ArrayList<ExamResults> taskList;
        private ArrayList<ExamResults> arraylist;
        private TextView tvMarks,tvOutOff;
        private Context mContext;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView subject,marks,outoff;
            private ItemClickListener clickListener;

            public MyViewHolder(View view)
            {
                super(view);
                subject= (TextView) view.findViewById(R.id.tvResultSub);
                marks = (TextView) view.findViewById(R.id.tvResultMarks);
                outoff = (TextView) view.findViewById(R.id.tvResultOutOff);

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

        public ExamResultListAdapter(Context cont, ArrayList<ExamResults> arrList,TextView tvMarks,TextView tvOutOff) {
            this.taskList = arrList;
            this.arraylist = new ArrayList<ExamResults>();
            this.arraylist.addAll(arrList);
            this.mContext = cont;
            this.tvMarks = tvMarks;
            this.tvOutOff = tvOutOff;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.exam_result_items, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ExamResults objStud = taskList.get(position);
            holder.subject.setText(objStud.getSub_name());
            holder.marks.setText(objStud.getMarks());
            holder.outoff.setText(objStud.getMax_marks());
            marks = marks + Integer.parseInt(objStud.getMarks());
            outOff = outOff + Integer.parseInt(objStud.getMax_marks());

            tvMarks.setText(""+marks);
            tvOutOff.setText(""+outOff);

            //data == html data which you want to load
            //holder.desc.getSettings().setJavaScriptEnabled(true);
            //holder.desc.loadDataWithBaseURL("", objStud.getData(), "text/html", "UTF-8", "");

//        holder.desc.setText(objStud.getData());

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        //showResults();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

    }
*/
}
