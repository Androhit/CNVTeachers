package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.StudentBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shraddha on 4/17/2017.
 */

public class StudentAdapter extends BaseAdapter {

    private Context mContext;
    private List<StudentBean> mOriginalData = null, mTempData = null;
    private HashMap<String, Boolean> mCheckedList;


    public StudentAdapter(Context mContext, List<StudentBean> list) {
        this.mContext = mContext;
        this.mOriginalData = list;
        this.mTempData = list;
        mCheckedList = new HashMap<String, Boolean>();


        for (StudentBean obj : list) {
            if (obj.isSelected()) {
                mCheckedList.put(obj.getAdmno(),true);
            } else {
                mCheckedList.put(obj.getAdmno(), false);
            }
        }
    }

    public List<String> get_Student() {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Boolean> set : mCheckedList.entrySet()) {
            if (set.getValue()) {
                list.add(set.getKey());
            }
        }
        return list;
    }


    public int getCount() {
        return mTempData.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.student_items, null);
        final ViewHolder holder = new ViewHolder(view);
        final StudentBean objStud = mTempData.get(position);

        holder.name.setText(""+objStud.getName());
        holder.admno.setText(""+objStud.getAdmno());
        holder.chkSelected.setTag(objStud);

        if (mCheckedList.get(objStud.getAdmno())) {
            holder.chkSelected.setChecked(true);
        } else {
            holder.chkSelected.setChecked(false);
        }

        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                StudentBean student = (StudentBean) holder.chkSelected.getTag();
                if (isChecked)
                {
                    mCheckedList.put(student.getAdmno(), true);
                }
                else {
                  //  mCheckedList.remove(student.getAdmno());
                    mCheckedList.put(student.getAdmno(), false);
                }
            }
        });
        return view;
    }

    public StudentBean getItem(int position) {
        return mTempData.get(position);
    }

    public ArrayList<String> getCheckedList() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(mCheckedList.keySet());
        return list;
    }


    class ViewHolder {
        TextView name;
        TextView admno;
        CheckBox chkSelected;

        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.tvStudName);
            chkSelected = (CheckBox) view.findViewById(R.id.chkselected);
            admno = (TextView) view.findViewById(R.id.tvAdmno);
        }
    }
}
