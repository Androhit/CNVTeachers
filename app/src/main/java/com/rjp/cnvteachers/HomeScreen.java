package com.rjp.cnvteachers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rjp.cnvteachers.beans.InstitutesBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.fragments.AchievmentFragment;
import com.rjp.cnvteachers.fragments.AttendanceFragment;
import com.rjp.cnvteachers.fragments.CircularFragment;
import com.rjp.cnvteachers.fragments.ClassTimeTableFragment;
import com.rjp.cnvteachers.fragments.EmployeeFragment;
import com.rjp.cnvteachers.fragments.ExamFragment;
import com.rjp.cnvteachers.fragments.HandsOnScienceFragment;
import com.rjp.cnvteachers.fragments.MyTimeTableFragment;
import com.rjp.cnvteachers.fragments.PerformanceFragment;
import com.rjp.cnvteachers.fragments.StudFragment;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.squareup.picasso.Picasso;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = HomeScreen.class.getSimpleName();
    private Context mContext;
    Fragment fragment = null;
    Class fragmentClass = null;
    private ImageView ivStudPic;
    private FloatingActionButton fabback;
    private ConfirmationDialogs objDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabback=(FloatingActionButton)findViewById(R.id.fabBack);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initDrawerSecond(toolbar,AppPreferences.getInstObj(mContext));
        initListners();
    }

    private void initDrawerSecond(Toolbar toolbar, InstitutesBean instObj) {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            TextView tvUser = (TextView)headerView.findViewById(R.id.tvname);
            TextView tvemail = (TextView)headerView.findViewById(R.id.textView);
            ImageView ivAvatar = (ImageView)headerView.findViewById(R.id.imageView);

            tvUser.setText(""+AppPreferences.getLoginObj(mContext).getFirstname()+" "+AppPreferences.getLoginObj(mContext).getLastname());
            tvemail.setText(""+AppPreferences.getLoginObj(mContext).getEmail());
            Picasso.with(mContext).load(""+AppPreferences.getLoginObj(mContext).getPhoto_url()).placeholder(R.drawable.user_512).into(ivAvatar);

            Fragment fragment = null;
            fragment = (Fragment) fragmentClass.newInstance();
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            navigationView.getMenu().getItem(0).setChecked(true);
            setTitle(navigationView.getMenu().getItem(0).getTitle());
            //showHintsFirstTime();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initListners() {
        fabback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout flm = (FrameLayout) findViewById(R.id.flContent);
                if (flm != null && flm.getChildCount() > 0) {
                    flm.removeAllViews();
                    fabback.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id) {
            case R.id.nav_dashboard:

                break;
            case R.id.nav_profile  :
                fragmentClass = EmployeeFragment.class;
                 break;

            case R.id.nav_studdetails:
                fragmentClass = StudFragment.class;
                break;

            case R.id.nav_mytt  :
                fragmentClass = MyTimeTableFragment.class;
                break;

            case R.id.nav_classtt :
                fragmentClass = ClassTimeTableFragment.class;
                break;

            case R.id.nav_att :
               fragmentClass = AttendanceFragment.class;
                break;

            case R.id.nav_exam_tt :
                fragmentClass = ExamFragment.class;
                break;

            case R.id.nav_circular :
                     fragmentClass = CircularFragment.class;
                     break;

            case R.id.nav_achievment :
                fragmentClass = AchievmentFragment.class;
                break;

            case R.id.nav_hos :
                fragmentClass = HandsOnScienceFragment.class;
                break;

            case R.id.nav_perform :
                fragmentClass = PerformanceFragment.class;
                break;

            case R.id.nav_logout :  logout(); break;
            }

         try {
                fragment = (Fragment) fragmentClass.newInstance();
             }
         catch (Exception e) {
                e.printStackTrace();
             }

        if(fragment!=null) {
            fabback.setVisibility(View.VISIBLE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.flContent, fragment );
            transaction.addToBackStack(null);
            transaction.commit();
            setTitle(item.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout()
    {
        AlertDialog.Builder build = new AlertDialog.Builder(mContext);
        build.setTitle("Logout Confirmation...");
        build.setMessage("Do you want to logout?");
        build.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                restartAppication();
            }
        });
        build.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        build.show();
    }

    public void restartAppication()
    {
        AppPreferences.setAdmnoCount(mContext, 0);
        AppPreferences.setAdmno(mContext, null);
        AppPreferences.setLoginObj(mContext,null);
        AppPreferences.setIsRemember(mContext,false);

        Intent mStartActivity = new Intent(mContext, Splash.class);

        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 300, mPendingIntent);
        System.exit(0);
    }
}
