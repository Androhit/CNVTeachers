package com.rjp.cnvteachers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rjp.cnvteachers.Permission.PermissionsActivity;
import com.rjp.cnvteachers.Permission.PermissionsChecker;
import com.rjp.cnvteachers.adapters.ClassListAdapter;
import com.rjp.cnvteachers.adapters.DivisionListAdapter;
import com.rjp.cnvteachers.adapters.StudentAdapter;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.beans.ClassBean;
import com.rjp.cnvteachers.beans.DivisonBean;
import com.rjp.cnvteachers.beans.NotificationBeans;
import com.rjp.cnvteachers.beans.StudentBean;
import com.rjp.cnvteachers.beans.WorksheetBean;
import com.rjp.cnvteachers.common.ConfirmationDialogs;
import com.rjp.cnvteachers.common.DateOperations;
import com.rjp.cnvteachers.common.FileOperations;
import com.rjp.cnvteachers.common.RealPathUtils;
import com.rjp.cnvteachers.utils.AppPreferences;
import com.rjp.cnvteachers.utils.NetworkUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/15/2017.
 */

public class AddWorksheet extends AppCompatActivity{

    private Context mContext;
    private API retrofitApi;
    private WorksheetBean obj;
    private ConfirmationDialogs objDialog;
    private EditText etAim,etTitle,etDesc;
    private TextView tvFromDate,tvToDate,tvEmpName;
    private Spinner spnClassName,spnDivision;
    private Button btnUpload,btnAdd;
    private int mYear,mMonth,mDay;
    private int DATE_DIALOG_ID = 0;
    private ArrayList<ClassBean> arrClass;
    private DivisonBean objDiv;
    private ClassBean objClass;
    private String Classid;
    private ArrayList<DivisonBean> arrDiv;
    private PermissionsChecker checker;
    private DateOperations objDates;
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
    private int PICK_FILE = 1;
    private String Division;
    private byte[] UPLOADING_FILE_ARRAY = null;
    private String  FILE_TYPE = null;
    private ListView rvStud;
    private Button btnSearch;
    private ArrayList<StudentBean> arrList;
    private StudentAdapter adapt;
    private String TAG="Worksheet";
    private ArrayList<String> arrCheckedList;
    private List<String> studList;
    private TextView tvpath;
    ClassListAdapter adapter;
    private ProgressDialog prog;
    private ArrayList<NotificationBeans> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worksheet);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRetrofitClient();
        init();
        initData();
        initIntent();


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();
        setListners();
    }

    private void initIntent() {

        obj = (WorksheetBean) getIntent().getSerializableExtra("ObjWorksheet");
        if(obj!=null)
        {
            setTitle("Worksheet");
            etAim.setText(obj.getAim());
            etTitle.setText(obj.getTitle());
            etDesc.setText(obj.getDesc());
            tvFromDate.setText(obj.getFromDate());
            tvToDate.setText(obj.getToDate());
          //  String result = obj.getAttachment().substring(obj.getAttachment().lastIndexOf("/") + 1);
            tvpath.setText(obj.getAttachment());
        }
        else
        {
            setListners();
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
                            if(obj != null) {
                                objclas.setClass_id(obj.getClassid());
                                objclas.setClasses("----------------------------");
                                objclas.setDept_name(obj.getClass_id());
                            }else {
                                objclas.setClass_id("0");
                                objclas.setClasses("----------------------------");
                                objclas.setDept_name("Select Class");
                            }
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

    private void updateDisplay() {
        if(DATE_DIALOG_ID==0) {
            tvFromDate.setText(new StringBuilder()                        // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(""));
        }
        else
        {
            tvToDate.setText(new StringBuilder()                        // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(""));
        }
    }

    private void initRetrofitClient() {
        RetrofitClient.initRetrofitClient();
        retrofitApi = RetrofitClient.getRetrofitClient();

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };


    private void setListners() {

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DATE_DIALOG_ID = 0;
                DatePickerDialog dialog = new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DATE_DIALOG_ID = 1;
                DatePickerDialog dialog = new DatePickerDialog(mContext, mDateSetListener, mYear, mMonth, mDay);
                dialog.show();
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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setTitle("Upload File");
                alert.setMessage("Choose file from GALLERY");

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                        openGalleryIntent();
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });
                alert.show();

                //tvpath.setText(obj.getUpload_file());
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(obj != null) {
                    update_worksheet();
                }
                else
                {
                    worksheet_add();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_student();
            }
        });

    }


    private void update_worksheet() {
        Classid = "";
        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
            Classid = objClass.getClass_id();
        }

        Division = "";
        if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
            Division = objDiv.getDivision_name();
        }

        String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
        String acadyear = AppPreferences.getAcademicYear(mContext);
        String empid = AppPreferences.getLoginObj(mContext).getEmpid();
        String aim= etAim.getText().toString();
        String desc=etDesc.getText().toString();
        String title=etTitle.getText().toString();
        String From= tvFromDate.getText().toString();
        String To= tvToDate.getText().toString();

        StringBuilder chkId=new StringBuilder();
        String chk="";
        studList = adapt.get_Student();

        if(adapt!=null){
            for(String obj: studList)
            {
                chkId.append(obj);
                chkId.append(",");
            }
            Log.e(TAG,"ChkList"+chkId);
            chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
        }

        String AttachmentString = null;
        if(UPLOADING_FILE_ARRAY!=null) {
            AttachmentString = Base64.encodeToString(UPLOADING_FILE_ARRAY, Base64.NO_WRAP);
            String attachFile = AppPreferences.getBranchId(mContext);//+""+AppPreferences.getStudentInfo(mContext).getAddress1()+FILE_TYPE;
        }

        WorksheetBean objW=new WorksheetBean();
        objW.setEmpid(empid);
        objW.setAim(aim);

        objW.setDesc(desc);
        objW.setTitle(title);
        objW.setFromDate(From);
        objW.setToDate(To);
        objW.setBr_id(br_id);
        objW.setAcadyear(acadyear);
        objW.setAttachment(AttachmentString);
        objW.setClass_id(Classid);
        objW.setDivision(Division);
        objW.setId(obj.getId());
        objW.setChklist(chk);


        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String placeJSON = gson.toJson(objW);
        Log.e(TAG,"Object"+placeJSON);
        Log.e(TAG,"Student"+studList);


        if ((!Classid.equals("0")) && (!Division.equals("Select Division") && aim.length()!=0 && desc.length()!=0 && title.length()!=0) && (!From.equals(" From Date")) && (!To.equals(" To Date"))) {

        if (NetworkUtility.isOnline(mContext)) {
            final ProgressDialog prog = new ProgressDialog(mContext);
            prog.setTitle("Loading");
            prog.setMessage("Please wait");
            prog.show();

            retrofitApi.add_Worksheet("UpdateWorksheet",AppPreferences.getInstObj(mContext).getCode(),placeJSON, new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {

                    if (prog.isShowing()) {
                        prog.dismiss();
                    }
                    if(apiResults!=null)
                    {
                        if(!apiResults.getResult().equals("false"))
                        {
                            objDialog.successDialog(mContext,"worksheet updated successfully");
                            finish();
                            //makeDisabled();
                        }
                        else
                        {
                            Toast.makeText(mContext,"Error1 While updating, Please check internet connectivity.",Toast.LENGTH_LONG).show();
                            Log.e(TAG,"Success"+apiResults.getResult());
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext,"Error2 While updating, Please check internet connectivity.",Toast.LENGTH_LONG).show();
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
            alert.setMessage(mContext.getResources().getString(R.string.error_input));
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

    private void search_student() {

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

                retrofitApi.get_student(AppPreferences.getInstObj(mContext).getCode(), Classid, Division, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {
                        if (prog.isShowing()) {
                            prog.dismiss();
                        }
                        btnAdd.setVisibility(View.VISIBLE);
                        if (apiResults != null) {
                            int i,j;

                            if (apiResults.getStud().size() > 0) {

                                if(obj != null){

                                     for(i=0;i< apiResults.getStud().size();i++)
                                     {
                                         for(j=0;j<obj.getStudent_array().size();j++)
                                         {
                                             String Admno = apiResults.getStud().get(i).getAdmno();
                                             String admno = obj.getStudent_array().get(j);
                                             if(admno.equals(Admno))
                                             {
                                                 apiResults.getStud().get(i).setSelected(true);
                                                 break;
                                             }
                                         }
                                     }
                                }
                                adapt = new StudentAdapter(mContext, apiResults.getStud());
                                rvStud.setAdapter(adapt);
                                rvStud.setVisibility(View.VISIBLE);
                                setListViewHeightBasedOnItems(rvStud);
                                rvStud.setAdapter(adapt);

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
                        search_student();
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

    private Boolean setListViewHeightBasedOnItems(ListView rvStud) {
            ListAdapter listAdapter = rvStud.getAdapter();
            if (listAdapter != null) {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, rvStud);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = rvStud.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = rvStud.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                rvStud.setLayoutParams(params);
                rvStud.requestLayout();

                return true;

            } else {
                return false;
            }

    }

    private void worksheet_add() {

        Classid = "";
        if (objClass != null || (!(objClass.getClass_id().equals("0")))) {
            Classid = objClass.getClass_id();
        }

        Division = "";
        if (objDiv != null || (!(objDiv.getDiv_id().equals("0")))) {
            Division = objDiv.getDivision_name();
        }

        String br_id = AppPreferences.getLoginObj(mContext).getBr_id();
        String acadyear = AppPreferences.getAcademicYear(mContext);
        String empid = AppPreferences.getLoginObj(mContext).getEmpid();
        String aim= etAim.getText().toString();
        String desc=etDesc.getText().toString();
        String title=etTitle.getText().toString();
        String From= tvFromDate.getText().toString();
        String To= tvToDate.getText().toString();

        StringBuilder chkId=new StringBuilder();
        String chk="";
        studList = adapt.get_Student();

        if(adapt!=null){
            for(String obj: studList)
            {
                chkId.append(obj);
                chkId.append(",");
            }
            Log.e(TAG,"ChkList"+chkId);
            chk=chkId.length() > 0 ? chkId.substring(0, chkId.length()-1) : "";
        }


        if ((!Classid.equals("0")) && (!Division.equals("Select Division") && aim.length()!=0 && desc.length()!=0 && title.length()!=0) && (!From.equals(" From Date")) && (!To.equals(" To Date"))) {

                String AttachmentString = null;
                if(UPLOADING_FILE_ARRAY!=null) {
                    AttachmentString = Base64.encodeToString(UPLOADING_FILE_ARRAY, Base64.NO_WRAP);
                    String attachFile = AppPreferences.getBranchId(mContext);//+""+AppPreferences.getStudentInfo(mContext).getAddress1()+FILE_TYPE;
                }

                WorksheetBean obj=new WorksheetBean();
                obj.setEmpid(empid);
                obj.setAim(aim);

                obj.setDesc(desc);
                obj.setTitle(title);
                obj.setFromDate(From);
                obj.setToDate(To);
                obj.setBr_id(br_id);
                obj.setAcadyear(acadyear);
                obj.setAttachment(AttachmentString);
                obj.setClass_id(Classid);
                obj.setDivision(Division);

                obj.setChklist(chk);


                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                String placeJSON = gson.toJson(obj);
                Log.e(TAG,"Object"+placeJSON);
                Log.e(TAG,"Student"+studList);

            if (NetworkUtility.isOnline(mContext)) {
                final ProgressDialog prog = new ProgressDialog(mContext);
                prog.setTitle("Loading");
                prog.setMessage("Please wait");
                prog.show();

               retrofitApi.add_Worksheet("AddWorksheet",AppPreferences.getInstObj(mContext).getCode(),placeJSON, new Callback<ApiResults>() {
                    @Override
                    public void success(ApiResults apiResults, Response response) {

                       if (prog.isShowing()) {
                            prog.dismiss();
                        }
                        if(apiResults!=null)
                        {
                            if(!apiResults.getResult().equals("false"))
                            {
                                objDialog.successDialog(mContext,"worksheet added successfully");
                                finish();
                                //makeDisabled();
                            }
                            else
                            {
                                Toast.makeText(mContext,"Error1 While inserting, Please check internet connectivity.",Toast.LENGTH_LONG).show();
                                Log.e(TAG,"Success"+apiResults.getResult());
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext,"Error2 While inserting, Please check internet connectivity.",Toast.LENGTH_LONG).show();
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
            alert.setMessage(mContext.getResources().getString(R.string.error_input));
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

    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    private void openGalleryIntent() {
        try {
            if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
                startPermissionsActivity(PERMISSIONS_READ_STORAGE);
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Request Code " + requestCode);

        if (requestCode==PICK_FILE && resultCode == RESULT_OK)
        {
            if (data == null) {
                Toast.makeText(mContext,"Unable to load file from memory",Toast.LENGTH_LONG).show();
                return;
            }

            try {
                Uri referenceUri = data.getData();
                String referencePath = RealPathUtils.getRealPathFromURI_BelowAPI11(mContext, data.getData());
//
                UPLOADING_FILE_ARRAY = FileOperations.convertToByteArray(referencePath);
                //FILE_TYPE = myFile.getName().substring(myFile.getName().lastIndexOf("."));
                File fl = new File(referencePath);
                if(fl.exists()) {
                    Log.e(TAG, "file arr final " + referencePath);
                    Log.e(TAG, "file arr final " + UPLOADING_FILE_ARRAY.length);
                    tvpath.setText(fl.getName());
                }
                else
                {
                    tvpath.setText("Failed");
                }
                //ivAvatar.setImageBitmap(thePic);
                //uploadPictureToServer();


            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
                            if(obj!= null){
                                objdiv.setDiv_id(obj.getDiv_id());
                                objdiv.setDivision_name(obj.getDivision());
                            }else {
                                objdiv.setDiv_id("0");
                                objdiv.setDivision_name("Select Division\n" + "_______________");
                            }
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

    private void init() {
        objDialog = new ConfirmationDialogs(mContext);
        checker = new PermissionsChecker(mContext);
        objDates = new DateOperations();
        etAim=(EditText) findViewById(R.id.etAim);
        etDesc=(EditText) findViewById(R.id.etDesc);
        etTitle=(EditText) findViewById(R.id.etTitle);
        tvEmpName=(TextView) findViewById(R.id.tvEmpName);
        tvpath=(TextView) findViewById(R.id.tvpath);
        tvFromDate=(TextView) findViewById(R.id.tvFromDate);
        tvToDate=(TextView) findViewById(R.id.tvToDate);
        spnClassName=(Spinner) findViewById(R.id.spnClassName);
        spnDivision=(Spinner) findViewById(R.id.spnDivision);
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnSearch=(Button) findViewById(R.id.btnSearch);
        btnUpload=(Button) findViewById(R.id.btnUpload);
        rvStud=(ListView) findViewById(R.id.rvStud);

        tvEmpName.setText(""+AppPreferences.getLoginObj(mContext).getFirstname()+" "+AppPreferences.getLoginObj(mContext).getLastname());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}