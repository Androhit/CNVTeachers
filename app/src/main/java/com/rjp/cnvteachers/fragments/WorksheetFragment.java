package com.rjp.cnvteachers.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rjp.cnvteachers.AddWorksheet;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.adapters.WorksheetAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.WorksheetBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/15/2017.
 */

public class WorksheetFragment extends Fragment{

    private Context mContext;
    private API retrofitApi;
    private RecyclerView rvSearchResult;
    private Button btSearch;
    private FloatingActionButton fabadd;
    private Spinner spnClassName;
    private Spinner spnDivision;
    private ConfirmationDialogs objDialog;
    private SwipeRefreshLayout refreshView;
    private ArrayList<ClassBean> arrClass;
    private DivisonBean objDiv;
    private ClassBean objClass;
    private String Classid;
    private ArrayList<DivisonBean> arrDiv;
    private String Division;
    private ArrayList<WorksheetBean> arrList;
    private WorksheetAdapter adapt;
    private WorksheetBean obj;
    private EditText etSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_worksheet, container, false);
        mContext = getContext();

        initRetrofitClient();
        init(v);
        initData();
        setListners();
        return v;
    }

    private void setListners() {

        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // method call which is to be refresh
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try {
                    if (charSequence.length()>0) {
                        String str = charSequence.toString();
                        adapt.filter(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        spnClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objClass = (ClassBean) parent.getItemAtPosition(position);
                getdiv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field1));
                alert.setCancelable(false);

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });

                alert.show();
            }
        });

        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                objDiv = (DivisonBean) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setMessage(mContext.getResources().getString(R.string.error_input_field1));
                alert.setCancelable(false);

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });

                alert.show();
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Classid = "";
                if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                    Classid = objClass.getClass_id();
                }

                Division = "";
                if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
                    Division = objDiv.getDivision_name();
                }

                if ((!Classid.equals("0")) && (!Division.equals("Select Division"))) {



                    if (NetworkUtility.isOnline(mContext)) {
                        final ProgressDialog prog = new ProgressDialog(mContext);
                        prog.setMessage("Loading...");
                        prog.setCancelable(false);

                        prog.show();



                        String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
                        String acadyear = AppPreferences.getAcademicYear(mContext);
                        String empid = AppPreferences.getLoginObj(mContext).getEmpid();

                        refreshView.setRefreshing(true);
                        retrofitApi.getWorksheet(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, br_id, acadyear,empid, new Callback<ApiResults>() {
                            @Override
                            public void success(ApiResults apiResults, Response response) {

                                refreshView.setRefreshing(false);
                                if (prog.isShowing()) {
                                    prog.dismiss();
                                }
                                if(apiResults != null)
                                {
                                    arrList= apiResults.getWorksheet_list();

                                    adapt= new WorksheetAdapter(mContext,arrList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                    rvSearchResult.setLayoutManager(mLayoutManager);
                                    rvSearchResult.setItemAnimator(new DefaultItemAnimator());
                                    rvSearchResult.setAdapter(adapt);
                                }
                                else {
                                    objDialog.dataNotAvailable(new ConfirmationDialogs.okCancel() {
                                        @Override
                                        public void okButton() {
                                            setListners();
                                        }

                                        @Override
                                        public void cancelButton() {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                refreshView.setRefreshing(false);
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
                                setListners();
                            }

                            @Override
                            public void cancelButton() {

                            }
                        });
                    }

                }
                else
                {
                    //objDialog.okDialog("Error",mContext.getResources().getString(R.string.error_input_field1));
                    final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                    alert.setMessage(mContext.getResources().getString(R.string.error_input_field2));
                    alert.setCancelable(false);

                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.dismiss();
                        }
                    });

                    alert.show();
                }
            }
        });


        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setMessage("Wait...");
                prog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prog.dismiss();
                        Intent it = new Intent(mContext, AddWorksheet.class);
                      //  it.putExtra("obj",obj);
                        mContext.startActivity(it);
                    }
                }, 500);
            }
        });

    }

    private void getdiv() {

        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setMessage("Loading...");
            prog.setCancelable(false);

            prog.show();

            Classid = "";
            if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
                Classid = objClass.getClass_id();
            }

            retrofitApi.getDivison_list(AppPreferences.getInstObj(mContext).getCode(),Classid, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if (prog.isShowing()) {
                        prog.dismiss();
                    }

                    if (apiResults != null) {
                        if (apiResults.getDivison_list() != null) {
                            DivisonBean objdiv = new DivisonBean();
                            objdiv.setDiv_id("0");
                            objdiv.setDivision_name("Select Division\n" + "_______________");
                            arrDiv = apiResults.getDivison_list();
                            arrDiv.add(0, objdiv);
                            // ArrayAdapter<DivisonBean> adapter = new ArrayAdapter<DivisonBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrDiv);
                            DivisionListAdapter adapter=new DivisionListAdapter(getActivity(),R.layout.div_list_items,R.id.tvDiv,arrDiv);
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
                                objclas.setClasses("----------------------------");
                                objclas.setDept_name("Select Class");
                                arrClass = apiResults.getClass_list();
                                arrClass.add(0, objclas);
                                //   ArrayAdapter<ClassBean> adapter = new ArrayAdapter<ClassBean>(mContext, android.R.layout.simple_spinner_dropdown_item, arrClass);
                                ClassListAdapter adapter=new ClassListAdapter(getActivity(),R.layout.class_list_items,R.id.tvClass,arrClass);
                                spnClassName.setAdapter(adapter);
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
                        setListners();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }
        }

    private void init(View v) {
        objDialog = new ConfirmationDialogs(mContext);
        refreshView = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_view_task);
        refreshView.setColorSchemeResources(R.color.cyan_900,R.color.colorAccent,R.color.yellow_500,R.color.red_900);

        rvSearchResult = (RecyclerView) v.findViewById(R.id.rvSearchResult);
        btSearch = (Button) v.findViewById(R.id.btSearch);
        fabadd =(FloatingActionButton) v.findViewById(R.id.fabadd);
        spnClassName=(Spinner) v.findViewById(R.id.spnClassName);
        spnDivision=(Spinner) v.findViewById(R.id.tvDivision);
        etSearch=(EditText) v.findViewById(R.id.etSearch);

    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }
}
