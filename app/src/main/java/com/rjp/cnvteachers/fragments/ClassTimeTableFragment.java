package com.rjp.cnvteachers.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.ClassTimeTableAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.ClassTimetableBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 3/18/2017.
 */

public class ClassTimeTableFragment extends Fragment{



    private String TAG = "Class Time Table";
    private Context mContext = null;
    private API retrofitApi;
    private ConfirmationDialogs objDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<ClassTimetableBean> arrTimeTable = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrMon = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrTue = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrWed = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrThu = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrFri = new ArrayList<ClassTimetableBean>();
    private ArrayList<ClassTimetableBean> arrSat = new ArrayList<ClassTimetableBean>();

    private ArrayList<ClassBean> arrClass = new ArrayList<ClassBean>();
    private ArrayList<DivisonBean> arrDiv = new ArrayList<DivisonBean>();

    private ClassBean objClass;
    private DivisonBean objDiv;

    private Spinner spnClass,spnDivision;
    String Class,Division;
    private Button btSubmit;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_class_timetable, container, false);

        mContext = getContext();

        initRetrofitClient();
        init(v);
        initData();
        setListners();
        getClassTimeTableService();
        return v;
    }



    private void setListners() {

        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
                getdiv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"class not",Toast.LENGTH_LONG).show();

            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(mContext,"Div not",Toast.LENGTH_LONG).show();

            }
        });

    }
    private void getdiv() {

        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);

            prog.show();

            Class = "";
            if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                Class = objClass.getClass_id();
            }

            retrofitApi.getDivison_list(AppPreferences.getInstObj(mContext).getCode(),Class, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getDivison_list() != null) {
                            DivisonBean objdiv = new DivisonBean();
                            objdiv.setDiv_id("0");
                            objdiv.setDivision_name("Select Division");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                            ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
                            spnDivision.setAdapter(adapter);
                        } else {
                            if (apiResults.getResult() != null) {
                                objDialog.okDialog("Error", apiResults.getResult());
                            }
                        }
                    } else {
                        if (apiResults.getResult() != null) {
                            objDialog.okDialog("Error", apiResults.getResult());
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_server_down));
                }
            });
        }
        else {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    getdiv();
                }

                @Override
                public void cancelButton() {

                }
            });
        }
    }



    private void initData() {
        if (NetworkUtility.isOnline(mContext)) {

            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);
            prog.show();

            retrofitApi.getClass_list(AppPreferences.getInstObj(mContext).getCode(), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getClass_list() != null) {
                            ClassBean objclas = new ClassBean();
                            objclas.setClass_id("0");
                            objclas.setClasses("Select Class");
                            objclas.setDept_name("");
                            arrClass = apiResults.getClass_list();
                            arrClass.add(0, objclas);
                            //ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                            ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrClass);
                            spnClass.setAdapter(adapter);
                        }
                        else
                        {
                            if(apiResults.getResult()!=null)
                            {
                                objDialog.okDialog("Error",apiResults.getResult());
                            }
                        }
                    }
                    else
                    {
                        if(apiResults.getResult()!=null)
                        {
                            objDialog.okDialog("Error",apiResults.getResult());
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_server_down));
                }

            });

        }
        else {
            objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                @Override
                public void okButton() {
                    initData();
                }

                @Override
                public void cancelButton() {

                }
            });
        }

    }


    private void getClassTimeTableService() {


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spnClass != null && spnDivision!=null) {

                if (NetworkUtility.isOnline(mContext)) {
                    final ProgressDialog prog = new ProgressDialog(mContext);
                    prog.setMessage("Loading...");
                    prog.setCancelable(false);
                    prog.show();

                    if (objClass != null) {
                        Class = objClass.getClass_id();
                    } else {
                        Class = "";
                    }


                    if (objDiv != null) {
                        Division = objDiv.getDivision_name();
                    } else {
                        Division = "";
                    }

                    String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                    String acadyear = AppPreferences.getAcademicYear(mContext);


                    retrofitApi.getClassTimeTable(AppPreferences.getInstObj(mContext).getCode(), Class, br_id, Division, acadyear, new Callback<ApiResults>() {
                        @Override
                        public void success(ApiResults apiResults, Response response) {
                            if (prog.isShowing()) {
                                prog.dismiss();
                            }

                            arrTimeTable = apiResults.getClass_timetable();

                            if (arrTimeTable != null) {
                                if (arrTimeTable.size() > 0) {

                                    arrMon = arrTimeTable.get(0).getMon();
                                    arrTue = arrTimeTable.get(1).getTue();
                                    arrWed = arrTimeTable.get(2).getWed();
                                    arrThu = arrTimeTable.get(3).getThu();
                                    arrFri = arrTimeTable.get(4).getFri();
                                    arrSat = arrTimeTable.get(5).getSat();

                                    if (arrMon.size() > 0 && arrTue.size() > 0 && arrWed.size() > 0 && arrThu.size() > 0 && arrFri.size() > 0 && arrSat.size() > 0) {
                                        Log.e(TAG, "Mon " + arrMon.size());
                                        Log.e(TAG, "Tue " + arrTue.size());
                                        Log.e(TAG, "wed " + arrWed.size());
                                        Log.e(TAG, "Thu " + arrThu.size());
                                        Log.e(TAG, "Fri " + arrFri.size());
                                        Log.e(TAG, "Sat " + arrSat.size());
                                        getList();
                                    } else {
                                        objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                            @Override
                                            public void okButton() {
                                                getClassTimeTableService();
                                            }

                                            @Override
                                            public void cancelButton() {

                                            }
                                        });
                                    }


                                } else {
                                    objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                        @Override
                                        public void okButton() {
                                            getClassTimeTableService();
                                        }

                                        @Override
                                        public void cancelButton() {

                                        }
                                    });
                                }
                            } else {
                                objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                    @Override
                                    public void okButton() {
                                        getClassTimeTableService();
                                    }

                                    @Override
                                    public void cancelButton() {

                                    }
                                });
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (prog.isShowing()) {
                                prog.dismiss();
                            }
                            objDialog.okDialog("Error", mContext.getResources().getString(R.string.error_server_down));
                        }
                    });
                } else {
                    objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                        @Override
                        public void okButton() {
                            getClassTimeTableService();
                        }

                        @Override
                        public void cancelButton() {

                        }
                    });
                }
            }

            else
             {
                 objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_input_field2));
            }
          }
        });
    }

     private void getList() {
        try {
            //ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager()); // if parent is Activity
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()); // if parent is Fragment

            ClassTimeTableAdapter adapt01= new ClassTimeTableAdapter(mContext,arrMon);
            ClassTimeTableAdapter adapt02= new ClassTimeTableAdapter(mContext,arrTue);
            ClassTimeTableAdapter adapt03= new ClassTimeTableAdapter(mContext,arrWed);
            ClassTimeTableAdapter adapt04= new ClassTimeTableAdapter(mContext,arrThu);
            ClassTimeTableAdapter adapt05= new ClassTimeTableAdapter(mContext,arrFri);
            ClassTimeTableAdapter adapt06= new ClassTimeTableAdapter(mContext,arrSat);

            adapter.addFrag(new SubClassTimeTableFragment(adapt01),"Mon");
            adapter.addFrag(new SubClassTimeTableFragment(adapt02),"Tue");
            adapter.addFrag(new SubClassTimeTableFragment(adapt03),"Wed");
            adapter.addFrag(new SubClassTimeTableFragment(adapt04),"Thu");
            adapter.addFrag(new SubClassTimeTableFragment(adapt05),"Fri");
            adapter.addFrag(new SubClassTimeTableFragment(adapt06),"Sat");
            //adapter.addFrag(new StudFeedbackFragment(),"FEEDBACK");
            viewPager.setAdapter(adapter);
            Calendar cl = Calendar.getInstance();
            Log.e(TAG,"Week Cnt "+cl.get(Calendar.DAY_OF_WEEK));
            viewPager.setCurrentItem(cl.get(Calendar.DAY_OF_WEEK)-2);
            tabLayout.setupWithViewPager(viewPager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        spnClass = (Spinner)v.findViewById(R.id.spnClass);
        spnDivision = (Spinner)v.findViewById(R.id.spnDivision);
        btSubmit=(Button)v.findViewById(R.id.btSubmit);


    }


    private void initRetrofitClient()
    {
        RetrofitClient.initRetrofitClient();
    retrofitApi = RetrofitClient.getRetrofitClient();
}

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {


            return mFragmentTitleList.get(position);
        }
    }


}
