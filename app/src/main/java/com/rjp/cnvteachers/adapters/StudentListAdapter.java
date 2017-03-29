package com.rjp.cnvteachers.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shraddha on 3/8/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder>
{
    public static StudentBean objStudent = null;
    private ArrayList<StudentBean> taskList;
    private ArrayList<StudentBean> arraylist;
    private API retrofitApi;
    private Context mContext;
    private TextView StudName;
    private TextView ClassName;
    private TextView Division;
    private TextView Admno;
    private CircularImageView StudPic;
    private TextView RollNo;
    private TextView Sex;
    private TextView BloodGrp;
    private TextView Dob;
    private TextView Caste;
    private TextView Religion;
    private TextView MotherMobile;
    private TextView Nationality;
    private TextView EmerNo;
    private TextView FatherName;
    private TextView MotherName;
    private TextView PerAddr;
    private TextView FatherMobile;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, client,tvDivision,tvAdmno;

        private ItemClickListener clickListener;

        public CircularImageView StudPic;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvStudName);
            client = (TextView) view.findViewById(R.id.spnClassName);
            tvDivision = (TextView) view.findViewById(R.id.tvDivision);
            tvAdmno = (TextView) view.findViewById(R.id.tvAdmno);
            StudPic = (CircularImageView) view.findViewById(R.id.ivStudPic);

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


    public StudentListAdapter(Context cont, ArrayList<StudentBean> arrList) {
        this.taskList = arrList;
        this.arraylist = new ArrayList<StudentBean>();
        this.arraylist.addAll(arrList);
        this.mContext = cont;
        Log.e("Data"," "+arrList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final StudentBean objStud = taskList.get(position);
        holder.name.setText(objStud.getFirstname().trim()+" "+objStud.getMiddlename().trim()+" "+objStud.getLastname().trim());
        holder.client.setText(""+objStud.getClass_name());
        holder.tvDivision.setText(""+objStud.getDivision());
        holder.tvAdmno.setText(""+objStud.getAdmno());

        Log.e("Name","NM "+objStud.getFirstname());


        if(objStud.getPhoto_url()!=null) {
            //Log.e("Adapter ","Path "+objStud.getPhoto_url());
            //Picasso.with(mContext).load(objStud.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.ivPic);
            Picasso.with(mContext).load(""+objStud.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(holder.StudPic);
        }



        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(mContext, "#" + position + " - " + mList[position] + " (Long click)", Toast.LENGTH_SHORT).show();
                } else
                {
                   //show detail view dialog
                    viewDetails(mContext,objStud);
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
            for (StudentBean wp : arraylist) {
                if (wp.getFirstname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    taskList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void viewDetails(Context mContext, StudentBean objStudent)
    {
        try {
            View vw = View.inflate(mContext, R.layout.view_details, null);


            RollNo = (TextView) vw.findViewById(R.id.tvRollNo);
            Admno = (TextView) vw.findViewById(R.id.tvAdmno);
            Sex = (TextView) vw.findViewById(R.id.tvSex);
            BloodGrp = (TextView) vw.findViewById(R.id.tvBloodGrp);
            Dob = (TextView) vw.findViewById(R.id.tvDob);
            Caste = (TextView) vw.findViewById(R.id.tvCaste);
            Religion = (TextView) vw.findViewById(R.id.tvReligion);
            Nationality = (TextView) vw.findViewById(R.id.tvNationality);
            EmerNo = (TextView) vw.findViewById(R.id.tvEmerNo);
            FatherName = (TextView) vw.findViewById(R.id.tvFatherName);
            MotherName = (TextView) vw.findViewById(R.id.tvMotherName);
            PerAddr = (TextView) vw.findViewById(R.id.tvPerAddr);
            FatherMobile = (TextView) vw.findViewById(R.id.tvFatherMobile);
            MotherMobile = (TextView) vw.findViewById(R.id.tvMotherMobile);
            StudName = (TextView) vw.findViewById(R.id.tvStudName);
            ClassName = (TextView) vw.findViewById(R.id.spnClassName);
            Division = (TextView) vw.findViewById(R.id.tvDivision);
            Admno = (TextView) vw.findViewById(R.id.tvAdmno);
            StudPic = (CircularImageView) vw.findViewById(R.id.ivStudPic);




            final AlertDialog alert = new AlertDialog.Builder(mContext).create();

            Button btCancel;
            btCancel = (Button) vw.findViewById(R.id.btn_cancel);


            StudName.setText(objStudent.getFirstname().trim()+" "+objStudent.getMiddlename().trim()+" "+objStudent.getLastname().trim());
            RollNo.setText(""+objStudent.getRollno());
            Admno.setText(""+objStudent.getAdmno());
            Sex.setText(""+objStudent.getSex());
            ClassName.setText(""+objStudent.getClass_name());
            Division.setText(""+objStudent.getDivision());
            BloodGrp.setText(""+objStudent.getBgroup());
            Dob.setText(""+objStudent.getBirthdate());
            Caste.setText(""+objStudent.getCaste());
            Religion.setText(""+objStudent.getReligion());
            Nationality.setText(""+objStudent.getNationality());
            EmerNo.setText(""+objStudent.getEmergencontact());
            FatherName.setText(""+objStudent.getFathername());
            MotherName.setText(""+objStudent.getMothername());
            PerAddr.setText(""+objStudent.getAddress1());
            FatherMobile.setText(""+objStudent.getFather_mobile());
            MotherMobile.setText(""+objStudent.getMother_mobile());
//            Picasso.with(mContext).load(""+objStudent.getPhoto_url());

            Picasso.with(mContext).load(""+objStudent.getPhoto_url()).placeholder(R.drawable.student_icon).error(R.drawable.student_icon).into(StudPic);

            alert.setView(vw);


            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                  //  System.exit(0);
                }
            });

            alert.show();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
