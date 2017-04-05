package com.rjp.cnvteachers.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.beans.ExamBean;

import java.util.ArrayList;

/**
 * Created by Shraddha on 4/5/2017.
 */

public class ExamAdapter extends ArrayAdapter<ExamBean> {

        LayoutInflater flater;

        public ExamAdapter(Activity context, int resouceId, int textviewId, ArrayList<ExamBean> list){

            super(context,resouceId,textviewId, list);
            flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ExamBean rowItem = getItem(position);

            View rowview = flater.inflate(R.layout.exam_items,null,true);

            TextView txtTitle = (TextView) rowview.findViewById(R.id.tvType);
            txtTitle.setText(""+rowItem.getExam_types());
            TextView tvtClass = (TextView) rowview.findViewById(R.id.tvName);
            tvtClass.setText(""+rowItem.getExam_name());

            if(position!=0)
            {
                if(rowItem.getExam_types().equals(getItem(position-1).getExam_types()))
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
                convertView = flater.inflate(R.layout.exam_items,parent, false);
            }
            ExamBean rowItem = getItem(position);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.tvType);
            txtTitle.setText(""+rowItem.getExam_types());
            TextView tvtClass = (TextView) convertView.findViewById(R.id.tvName);
            tvtClass.setText(""+rowItem.getExam_name());

            if(position!=0)
            {
                if(rowItem.getExam_types().equals(getItem(position-1).getExam_types()))
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
