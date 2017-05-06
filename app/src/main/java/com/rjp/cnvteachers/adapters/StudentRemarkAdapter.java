package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.RemarkBean;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.common.ItemClickListener;
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shraddha on 4/26/2017.
 */

public class StudentRemarkAdapter extends RecyclerView.Adapter<StudentRemarkAdapter.MyViewHolder>{
    private  Button btnremark;
    private ArrayList<StudentBean> studkList;
    private ArrayList<StudentBean> arraylist;
    private Context mContext;
    private RemarkBean objRemark;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<RemarkBean> remarksArray;
    ArrayAdapter<RemarkBean> adapt = null;
    Spinner spnRemark;
    HashMap<String,String> mapRemarks = new HashMap<>();
    StudentBean objStudent;
    StudentRemarkAdapter mainAdapter;
    StringBuilder chkId=new StringBuilder();
    private String chk;

    public StudentRemarkAdapter(Context mContext, ArrayList<RemarkBean> remarks, ArrayList<StudentBean> arr) {
        this.arraylist = new ArrayList<StudentBean>();
        this.remarksArray = remarks;
        this.studkList =arr;
        this.mContext = mContext;
        mainAdapter = this;
        adapt = new ArrayAdapter<RemarkBean>(mContext,android.R.layout.simple_spinner_dropdown_item,remarksArray);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView studname,admno;
//        public Spinner spnRemark;
        private ItemClickListener clickListener;
        private MultiSpinner spnRemark;
        public MyViewHolder(View view)
        {
            super(view);
            studname= (TextView) view.findViewById(R.id.tvStudName);
            admno = (TextView) view.findViewById(R.id.tvAdmno);
           // btnremark = (Button) view.findViewById(R.id.btnremark);
            spnRemark = (MultiSpinner) view.findViewById(R.id.spnMulti);

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

    @Override
    public StudentRemarkAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_remark_items, parent, false);

        return new StudentRemarkAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final StudentBean objStud = studkList.get(position);

        Log.e("View ",""+objStud.getAdmno());
        holder.admno.setText(""+objStud.getAdmno());
        holder.studname.setText(""+objStud.getName());

        holder.spnRemark.setAdapter(adapt, false, new MultiSpinner.MultiSpinnerListener() {
            public void onItemsSelected(boolean[] selected) {
                // Do something here with the selected items
                // your operation with code...
                chkId.setLength(0);
                for(int i=0; i<selected.length; i++) {

                    if(selected[i]) {
                        Log.i("TAG SELECTED", i + " : "+ objStud.getAdmno());
                        Log.i("TAG", i + " : "+ remarksArray.get(i).getValue());
                        chkId.append(remarksArray.get(i).getValue());
                        chkId.append(",");
                    }
                    chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
                }


                mapRemarks.put(objStud.getAdmno(),chk);
                Log.e("MapRemarks",""+mapRemarks);

            }
        });
        boolean[] selectedItems = new boolean[adapt.getCount()];
        //selectedItems[1] = true; // select second item
        holder.spnRemark.setSelected(selectedItems);

    }

    public StudentBean getSelectedObject()
    {
        return objStudent;
    }

    public void setSelectedObject(StudentBean obj)
    {
        this.objStudent = obj;
    }


//    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
//        public void onItemsSelected(boolean[] selected) {
//            // Do something here with the selected items
//            // your operation with code...
//            for(int i=0; i<selected.length; i++) {
//                if(selected[i]) {
//                    Log.i("TAG SELECTED",""+ mainAdapter.getSelectedObject().getAdmno());
//                    Log.i("TAG", i + " : "+ remarksArray.get(i).getValue());
//
//                    chkId.append(remarksArray.get(i).getValue());
//                    chkId.append(",");
//                    Log.i("TAG", i + " : "+ chkId);
//                }
//            }
//            chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
//            Log.i("TAG Chk",""+chk);
//            mapRemarks.put(mainAdapter.getSelectedObject().getAdmno(),chk);
//            Log.e("MapRemarks",""+mapRemarks);
//        }
//    };

    public HashMap<String,String> getRemarkList() {
        HashMap<String,String> list = new HashMap<>();
        for (Map.Entry<String, String> set : mapRemarks.entrySet()) {
            //list.add(set.getKey());
            String key=set.getKey();
            String value=set.getValue();
            list.put(key,value);
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return studkList.size();
    }
}
