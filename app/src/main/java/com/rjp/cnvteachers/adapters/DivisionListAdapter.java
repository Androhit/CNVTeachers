package com.rjp.cnvteachers.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.DivisonBean;

import java.util.ArrayList;

/**
 * Created by Shraddha on 4/12/2017.
 */

public class DivisionListAdapter extends ArrayAdapter<DivisonBean> {
    LayoutInflater flater;

    public DivisionListAdapter(Activity context, int resouceId, int textviewId, ArrayList<DivisonBean> list) {

        super(context, resouceId, textviewId, list);
        flater = context.getLayoutInflater();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DivisonBean rowItem = getItem(position);

        View rowview = flater.inflate(R.layout.div_list_items,null,true);

        TextView txtTitle = (TextView) rowview.findViewById(R.id.tvDiv);
        txtTitle.setText(""+rowItem.getDivision_name());

        return rowview;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = flater.inflate(R.layout.div_list_items,parent, false);
        }
        DivisonBean rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tvDiv);
        txtTitle.setText(""+rowItem.getDivision_name());

        if(position!=0)
        {
            if(rowItem.getDivision_name().equals(getItem(position-1).getDivision_name()))
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
