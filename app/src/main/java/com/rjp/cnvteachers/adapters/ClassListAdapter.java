package com.rjp.cnvteachers.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.ClassBean;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/25/2017.
 */


public class ClassListAdapter extends ArrayAdapter<ClassBean> {

    LayoutInflater flater;

    public ClassListAdapter(Activity context, int resouceId, int textviewId, ArrayList<ClassBean> list){

        super(context,resouceId,textviewId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassBean rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.class_list_items,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.tvDept);
        txtTitle.setText(""+rowItem.getDept_name());
        TextView tvtClass = (TextView) rowview.findViewById(R.id.tvClass);
        tvtClass.setText(""+rowItem.getclasses());

        if(position!=0)
        {
            if(rowItem.getDept_name().equals(getItem(position-1).getDept_name()))
            {
                txtTitle.setVisibility(View.GONE);
            }
            else
            {
                txtTitle.setVisibility(View.GONE);
            }
        }

        return rowview;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.class_list_items,parent, false);
        }
        ClassBean rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tvDept);
        txtTitle.setText(""+rowItem.getDept_name());
        TextView tvtClass = (TextView) convertView.findViewById(R.id.tvClass);
        tvtClass.setText(""+rowItem.getclasses());

        if(position!=0)
        {
            if(rowItem.getDept_name().equals(getItem(position-1).getDept_name()))
            {
                txtTitle.setVisibility(View.GONE);
            }
            else
            {
                txtTitle.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }
}