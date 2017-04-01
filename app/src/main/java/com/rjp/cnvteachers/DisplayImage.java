package com.rjp.cnvteachers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.rjp.cnvteachers.api.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by shraddha on 3/29/2017.
 */
public class DisplayImage extends AppCompatActivity {

    private String TAG = "Display Image";
    private Context mContext = null;
    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ArrayList<String> arr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        initIntents();
        setListners();
    }

    private void setListners()
    {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                //setTitle(arr.get(position).getDescription());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIntents()
    {
        arr = (ArrayList<String>) getIntent().getSerializableExtra("ObjGallery");
        if(arr!=null)
        {
            int pos = getIntent().getIntExtra("Position",0);
            mCustomPagerAdapter = new CustomPagerAdapter(this,arr);
            mViewPager.setAdapter(mCustomPagerAdapter);
            mViewPager.setCurrentItem(pos,true);
        }
        else
        {
            Log.e(TAG,"List is Null");
        }

    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<String> arrList = null;

        public CustomPagerAdapter(Context context, ArrayList<String> arr) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrList = arr;
        }

        @Override
        public int getCount() {
            return arrList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            String obj = arrList.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            //SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) itemView.findViewById(R.id.imageView);
            //imageView.setImageResource(obj.getPath());

            setTitle("Images");

            //imageView.setImage(ImageSource.uri(RetrofitClient.ROOT_URL+""+obj.getPath()));
            //imageView.setImage(ImageSource.uri("http://laschoolreport.com/wp-content/uploads/2013/03/Empty-classroom-600x450.jpg"));
            //Log.e(TAG,""+RetrofitClient.ROOT_URL+""+obj);
            //Picasso.with(mContext).load(RetrofitClient.ROOT_URL+""+obj).into(imageView);
            Glide.with(mContext).load(RetrofitClient.ROOT_URL+""+obj).into(imageView);
            //Picasso.with(mContext).load("http://laschoolreport.com/wp-content/uploads/2013/03/Empty-classroom-600x450.jpg").into();

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
