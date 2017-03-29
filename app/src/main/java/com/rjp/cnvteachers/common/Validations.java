package com.rjp.cnvteachers.common;

import android.widget.EditText;

/**
 * Created by rohit on 21/4/16.
 */
public class Validations
{

    public static boolean hasTextAvailable(EditText editText)
    {
        String str = editText.getText().toString();
        if(str.length()==0)
        {
            editText.setError("REQUIRED");
            return false;
        }

        return true;
    }

    public static boolean hasText(EditText et)
    {
        boolean res =false;
        if(et.getText().toString().length()>0  )
        {
             res = true;
        }
        else
        {
            et.setError("REQUIRED");
        }

        return res;
    }
}
