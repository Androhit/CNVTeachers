package com.rjp.cnvteachers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.DisplayImage;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by Shraddha on 3/29/2017.
 */
public class GalleryGridAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> arrList;

    public GalleryGridAdapter(Context c, ArrayList<String> arr) {
        mContext = c;
        this.arrList = arr;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.arrList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            //LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.gallery_list_items, null);

        }
        else
        {
            grid = (View) convertView;
        }

        final String objVideos = arrList.get(position);
        ImageView ivGal = (ImageView)grid.findViewById(R.id.grid_image) ;
        //TextView tvDesc = (TextView) grid.findViewById(R.id.tvDesc) ;

        //http://laschoolreport.com/wp-content/uploads/2013/03/Empty-classroom-600x450.jpg
        //Picasso.with(mContext).load("http://laschoolreport.com/wp-content/uploads/2013/03/Empty-classroom-600x450.jpg").placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(ivGal);
        //Log.e("Gal Adapt ",""+RetrofitClient.ROOT_URL+""+objVideos.getPath());
        //Log.e("Path",""+RetrofitClient.ROOT_URL+""+objVideos);
        //Picasso.with(mContext).load(RetrofitClient.ROOT_URL+""+objVideos).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(ivGal);
        Glide.with(mContext).load(RetrofitClient.ROOT_URL+""+objVideos).placeholder(R.mipmap.ic_launcher).error(R.drawable.logo).into(ivGal);
        //tvDesc.setText(objVideos.getDescription());

        ivGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(mContext, DisplayImage.class);
                it.putExtra("ObjGallery",arrList);
                it.putExtra("Position",position);
                mContext.startActivity(it);
            }
        });

        return grid;
    }
}


