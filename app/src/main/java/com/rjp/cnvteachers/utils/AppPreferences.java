package com.rjp.cnvteachers.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.rjp.cnvteachers.beans.InstitutesBean;
import com.rjp.cnvteachers.beans.LoginBean;

public class AppPreferences {
    public static final String PREF_LOGIN = "login_pref";
    public static final String PREF_KEY_USR_NAME = "PREF_KEY_USR_NAME";
    public static final String IS_REMEMBER_IDPASS = "IS_REMEMBER_IDPASS";
    public static final String PREF_FCM_KEY = "PREF_FCM_KEY";
    public static final String INST_OBJ = "INST_OBJ";
    public static final String PREF_CURRENT_ADMNO = "PREF_CURRENT_ADMNO";
    public static final String PREF_ADMNO_COUNT = "PREF_ADMNO_COUNT";
    public static final String PREF_ADMNO = "PREF_ADMNO";
    public static final String PREF_KEY_BRANCH_ID = "PREF_KEY_BRANCH_ID";
    public static final String PREF_KEY_ACADEMIC_YEAR = "PREF_KEY_ACADEMIC_YEAR";
    public static final String CIRCULAR_COUNT = "CIRCULAR_COUNT";
    public static final String ACHIEVEMENT_COUNT = "ACHIEVEMENT_COUNT";


    private Context context;

    public AppPreferences(Context context) {
        super();
        this.context = context;
    }


    public static void setLoginObj(Context context, LoginBean obj) {
        setLoginObj(context, PREF_LOGIN, obj);
    }

    public static LoginBean getLoginObj(Context context) {
        return getLoginObj(context, PREF_LOGIN, null);
    }

    public static void setInstObj(Context context, InstitutesBean obj) {
        setInstObj(context, INST_OBJ, obj);
    }

    public static InstitutesBean  getInstObj(Context context) {
        return getInstObj(context, INST_OBJ, null);
    }

    public static boolean getIsRemember(Context context) {
        return getBoolean(context, IS_REMEMBER_IDPASS);
    }

    public static void setIsRemember(Context context, boolean res) {
        setBoolean(context, IS_REMEMBER_IDPASS, res);
    }

    // register to fcm
    public static void setFcmKey(Context context, String empId) {
        setString(context, PREF_FCM_KEY, empId);
    }

    public static String getFcmKey(Context context) {
        return getString(context, PREF_FCM_KEY, null);
    }

    public static void setCurrentAdmno(Context context, String admno) {
        setString(context, PREF_CURRENT_ADMNO, admno);
    }

    public static String getCurrentAdmno(Context context) {
        return getString(context, PREF_CURRENT_ADMNO, null);
    }


    public static String getEmpName(Context context) {
        return getString(context, PREF_KEY_USR_NAME, null);
    }

    public static void setAdmnoCount(Context context, int count) {
        setInt(context, PREF_ADMNO_COUNT, count);
    }

    public static int getAdmnoCount(Context context) {
        return getInt(context, PREF_ADMNO_COUNT, 0);
    }

    public static void setAdmno(Context context, String admno) {
        setString(context, PREF_ADMNO, admno);
    }

    public static String getAdmno(Context context) {
        return getString(context, PREF_ADMNO, null);
    }


    public static String getBranchId(Context context) {
        return getString(context, PREF_KEY_BRANCH_ID, null);
    }

    public static void setBranchId(Context context, String branchId) {
        setString(context, PREF_KEY_BRANCH_ID, branchId);
    }

    public static String getAcademicYear(Context context) {
        return getString(context, PREF_KEY_ACADEMIC_YEAR, null);
    }

    public static void setAcademicYear(Context context, String academicYear) {
        setString(context, PREF_KEY_ACADEMIC_YEAR, academicYear);
    }


    // circular count
    public static int getCircularCount(Context context)
    {
        return  getInt(context,CIRCULAR_COUNT,0);
    }

    public static void setCircularCount(Context context,int val)
    {
        setInt(context,CIRCULAR_COUNT,val);
    }

    /******************** GET SET METHODS *************************/

    private static void setLoginObj(Context context, String key, LoginBean val) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        Gson gson = new Gson();
        String data = gson.toJson(val);
        e.putString(key, data);
        e.commit();
    }

    private static LoginBean getLoginObj(Context context, String key, String def) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, def);
        LoginBean obj = gson.fromJson(json,LoginBean.class);
        return obj;
    }

    private static void setInstObj(Context context, String key, InstitutesBean val) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        Gson gson = new Gson();
        String data = gson.toJson(val);
        e.putString(key, data);
        e.commit();
    }

    private static InstitutesBean getInstObj(Context context, String key, String def) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, def);
        InstitutesBean obj = gson.fromJson(json,InstitutesBean.class);
        return obj;
    }

    private static String getString(Context context, String key, String def) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String s = prefs.getString(key, def);
        return s;
    }

    private static long getLong(Context context, String key, String def) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        long s = prefs.getLong(key, Long.parseLong((String) def));
        return s;
    }

    private static void setBoolean(Context context, String prefKey, boolean result) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putBoolean(prefKey, result).commit();

    }

    private static boolean getBoolean(Context context, String prefKey) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(prefKey, false);
    }

    private static long getLong(Context context, String key) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getLong(key, 0);
    }

    private static int getInt(Context context, String key, int def) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        int s = prefs.getInt(key, def);
        return s;
    }

    private static void setInt(Context context, String key, int val) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putInt(key, val);
        e.commit();
    }

    private static void setString(Context context, String key, String val) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    private static void setLong(Context context, String key, long val) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putLong(key, val);
        e.commit();
    }


    private static SharedPreferences getSharedPreferenceInstance(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    protected SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = getSharedPreferences();
        boolean b = prefs.getBoolean(key, def);
        return b;
    }

    private double getDouble(String key, double def) {
        SharedPreferences prefs = getSharedPreferences();
        double d = Double.parseDouble(prefs.getString(key, String.valueOf(def)));
        return d;
    }

    private void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }

    private void setDouble(String key, double val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putString(key, String.valueOf(val));
        e.commit();
    }

    private void setLong(String key, long val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putLong(key, val);
        e.commit();
    }


    public static int getAchievementCount(Context context)
    {
        return  getInt(context,ACHIEVEMENT_COUNT,0);
    }

    public static void setAchievementCount(Context context,int val)
    {
        setInt(context,ACHIEVEMENT_COUNT,val);
    }

}
