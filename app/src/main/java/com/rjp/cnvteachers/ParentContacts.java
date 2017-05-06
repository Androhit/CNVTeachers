package com.rjp.cnvteachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.adapters.ParentListAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.ParentBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class ParentContacts extends AppCompatActivity{

    private Context mContext;
    private ConfirmationDialogs objDialog;
    private RecyclerView rvSearchResult;
    private Button btSearch;
    private Spinner spnClassName;
    private Spinner spnDivision;
    private API retrofitApi;
    private ArrayList<ClassBean> arrClass;
    private ClassListAdapter adapter;
    private ClassBean objClass;
    private ArrayList<DivisonBean> arrDiv;
    private String Classid;
    private DivisonBean objDiv;
    private String Division;
    private ParentListAdapter adapt;
    private ArrayList<ParentBean> arr;
    private EditText etsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_list);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRetrofitClient();
        init();
        initData();
        setListners();
    }

    private void setListners() {

        etsearch.addTextChangedListener(new TextWatcher() {
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
                search_parent();
            }
        });
    }

    private void search_parent() {
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
                prog.setTitle("Loading");
                prog.setMessage("Please wait");
                prog.show();

                retrofitApi.get_parents(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }

                        if (apiResults != null) {
                            int i,j;
                            arr=apiResults.getParent_list();

                            if (apiResults.getParent_list().size() > 0) {
                                adapt= new ParentListAdapter(mContext,arr);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                                rvSearchResult.setLayoutManager(mLayoutManager);
                                rvSearchResult.setItemAnimator(new DefaultItemAnimator());
                                rvSearchResult.setAdapter(adapt);

                            } else {
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

                        } else {
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
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                    }
                });


            } else {
                objDialog.noInternet(new ConfirmationDialogs.okCancel() {
                    @Override
                    public void okButton() {
                        search_parent();
                    }

                    @Override
                    public void cancelButton() {

                    }
                });
            }

        }
        else
        {
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
                            DivisionListAdapter adapter=new DivisionListAdapter(mContext,R.layout.div_list_items,R.id.tvDiv,arrDiv);
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
                            adapter = new ClassListAdapter(mContext, R.layout.class_list_items, R.id.tvClass, arrClass);

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
                    initData();
                }

                @Override
                public void cancelButton() {

                }
            });
        }
    }

    private void init() {
        objDialog = new ConfirmationDialogs(mContext);
        rvSearchResult = (RecyclerView) findViewById(R.id.rvSearchResult);
        btSearch = (Button) findViewById(R.id.btSearch);
        etsearch= (EditText) findViewById(R.id.etSearch);
        spnClassName=(Spinner) findViewById(R.id.spnClassName);
        spnDivision=(Spinner) findViewById(R.id.tvDivision);
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();
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
