package com.rjp.cnvteachers.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.ExamTimeTable;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.ExamBean;
import com.rjp.cnvteachers.beans.ExamResultsBean;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.ItemClickListener;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/24/2017.
 */

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.MyViewHolder> {

    private ArrayList<ExamBean> taskList;
    private ArrayList<ExamBean> arraylist;
    private Context mContext;
    private API retrofitApi;
    public Spinner spnClass;
    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<ExamResultsBean> arrList = new ArrayList<ExamResultsBean>();
    private int marks = 0,outOff = 0;
    private ClassBean objClass;
    public String Classid;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvExamType, tvExam, tvTimetable,  tvClass,tvDuration;
        private ItemClickListener clickListener;
        private ImageView ivGrade;

        public MyViewHolder(View view) {
            super(view);
            tvExamType = (TextView) view.findViewById(R.id.tvExamType);
            tvExam = (TextView) view.findViewById(R.id.tvExamName);
            tvTimetable = (TextView) view.findViewById(R.id.tvTimetable);
            tvDuration = (TextView) view.findViewById(R.id.tvDuration);
            tvClass=(TextView) view.findViewById(R.id.tvClass);

            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            //this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            // clickListener.onClick(view, getPosition(), false);
        }
    }

    public ExamListAdapter(Context cont, ArrayList<ExamBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<ExamBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
        initRetrofitClient();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ExamBean objStud = taskList.get(position);
        holder.tvExamType.setText(objStud.getMainexam_name());
        holder.tvExam.setText(objStud.getExam_name());
        holder.tvClass.setText(objStud.getClass_name());

        /*if(objStud.getGrade()!=null) {
            TextDrawable drawable = null;
            if(objStud.getGrade().contains("A")) {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.green_600));
            }
            else if(objStud.getGrade().contains("B"))
            {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.orange_600));
            }
            else
            {
                drawable = TextDrawable.builder().buildRound(objStud.getGrade(), mContext.getResources().getColor(R.color.red_600));
            }

           // holder.ivGrade.setImageDrawable(drawable);
        }
*/
        if(objStud.getStart_date()!=null && objStud.getEnd_date()!=null)
        {
            holder.tvDuration.setText(DateOperations.convertToddMMMyyyy(objStud.getStart_date())+" To "+DateOperations.convertToddMMMyyyy(objStud.getEnd_date()));
        }
        else
        {
            holder.tvDuration.setText("Not Decided");
        }

       holder.tvTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(mContext, ExamTimeTable.class);
                it.putExtra("objExam",objStud);
                mContext.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
    }

    public void showResults(ExamBean objStud) {
        try {

            marks = 0;
            outOff = 0;
            AlertDialog alert = new AlertDialog.Builder(mContext).create();
            View view = View.inflate(mContext, R.layout.exam_result_dialog, null);
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvExamResults);
            TextView tvExamname = (TextView)view.findViewById(R.id.tvExamName);
            TextView tvMarks = (TextView)view.findViewById(R.id.tvMarks);
            TextView tvOutOff = (TextView)view.findViewById(R.id.tvOutOff);
            spnClass=(Spinner)view.findViewById(R.id.spnClass);
            TextView tvSubExamname = (TextView)view.findViewById(R.id.tvSubExamName);


            retrofitApi.getClass_list(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response)
                {
                    if(apiResults!=null) {
                        if (apiResults.getClass_list() != null) {
                            ClassBean objclas= new ClassBean();
                            objclas.setClass_id("0");
                            objclas.setClasses("Select Class");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0,objclas);
                            ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            spnClass.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setTitle("Alert");
                    alert.setMessage("Server Network Error");
                    alert.show();
                    alert.setCancelable(false);

                }

            });


            spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    objClass = (ClassBean) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            tvExamname.setText(""+objStud.getExam_name());

            arrList = getExamResultsService(alert,objStud,rv,tvMarks,tvOutOff);

            if(arrList!=null) {
                alert.setView(view);
                alert.show();
            }
            else
            {
                Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ExamResultsBean> getExamResultsService(final AlertDialog alert, ExamBean obj, final RecyclerView rv, final TextView tvMarks, final TextView tvOutOff) {
        try {
            if (NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("loading...");
                prog.setCancelable(false);
                prog.show();

                if(objClass != null && (!(objClass.getClass_id().equals("0")))) {
                    Classid = objClass.getClass_id();
                }
                else if(objClass.getClass_id().equals("0"))
                {
                    Classid = "0";
                }


                retrofitApi.getExamResults("exam_marks", AppPreferences.getBranchId(mContext), new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResultses, Response response) {
                        arrList = apiResultses.getExm_all_sub();
                        if (arrList != null) {

                            if (arrList.size() >0)
                            {
                                tvMarks.setText(""+apiResultses.getTotal_grade());
                                ResultListAdapter adapt = new ResultListAdapter(mContext, arrList,tvMarks,tvOutOff);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                rv.setLayoutManager(mLayoutManager);
                                rv.setItemAnimator(new DefaultItemAnimator());
                                rv.setAdapter(adapt);
                            }
                            else
                            {
                                arrList = null;
                                Toast.makeText(mContext,"Result data not found",Toast.LENGTH_SHORT).show();
                                alert.dismiss();
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

    class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.MyViewHolder>
    {

        private ArrayList<ExamResultsBean> taskList;
        private ArrayList<ExamResultsBean> arraylist;
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

        public ResultListAdapter(Context cont, ArrayList<ExamResultsBean> arrList, TextView tvMarks, TextView tvOutOff) {
            this.taskList = arrList;
            this.arraylist = new ArrayList<ExamResultsBean>();
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
            final ExamResultsBean objStud = taskList.get(position);
            holder.subject.setText(objStud.getSub_name());

            holder.outoff.setText(objStud.getMax_marks());
            marks = marks + Integer.parseInt(objStud.getMarks());
            outOff = outOff + Integer.parseInt(objStud.getMax_marks());

            //tvMarks.setText(""+marks);
            tvOutOff.setText(""+outOff);

            if(objStud.getGrade()!=null) {
                holder.marks.setText(objStud.getGrade());
            }
            else
            {
                holder.marks.setText("-");
            }

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
}
