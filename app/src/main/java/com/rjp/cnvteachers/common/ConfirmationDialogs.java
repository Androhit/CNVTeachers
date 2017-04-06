package com.rjp.cnvteachers.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rjp.cnvteachers.R;


/**
 * Created by bhagyashree on 21/4/16.
 */
public class ConfirmationDialogs
{

    private Context mContext;

    public ConfirmationDialogs(Context _cont)
    {
        mContext = _cont;
    }

    public void  loginDialog(final okCancel intere)
    {
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle("Login");
        alert.setMessage("Login Successfully");
        alert.setCancelable(false);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                        intere.okButton();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
                intere.cancelButton();
            }
        });

        alert.show();

    }

    public void  okDialog(String title, String msg)
    {
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
            }
        });

        alert.show();
    }

    /*public void warningDialog(String str)
    {
        Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(mContext.getResources().getColor(R.color.red_100));
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }*/

    public void noInternet(final okCancel intere)
    {
        View vw = View.inflate(mContext, R.layout.no_internet_dialog,null);

        final AlertDialog alert = new AlertDialog.Builder(mContext).create();

        Button btCancel= (Button)vw.findViewById(R.id.btn_cancel);
        Button btRetry= (Button)vw.findViewById(R.id.btn_retry);

//        alert.setTitle("Login");
//        alert.setMessage("Login Successfully");
//        alert.setCancelable(false);
        alert.setView(vw);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
                System.exit(0);
            }
        });

        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
                intere.okButton();
            }
        });

        /*alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
                intere.okButton();
            }
        });

        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
                intere.cancelButton();
            }
        });*/

        alert.show();
    }


    public static void noInternet(Context mContext, final okCancel intere)
    {
        try {
            View vw = View.inflate(mContext, R.layout.no_internet_dialog,null);

            final AlertDialog alert = new AlertDialog.Builder(mContext).create();

            Button btCancel= (Button)vw.findViewById(R.id.btn_cancel);
            Button btRetry= (Button)vw.findViewById(R.id.btn_retry);

//        alert.setTitle("Login");
//        alert.setMessage("Login Successfully");
//        alert.setCancelable(false);
            alert.setView(vw);

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                    System.exit(0);
                }
            });

            btRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                    intere.okButton();
                }
            });

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void successDialog(Context mContext, String str)
    {
        Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(mContext.getResources().getColor(R.color.white));
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void  okDialog(Context mContext, String title, String msg)
    {
        final AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);

        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
            }
        });

        alert.show();
    }

    public interface okCancel
    {
        public void okButton();
        public void cancelButton();
    }

    public static void  serverFailuerDialog(Context mContext, String error)
    {
        try {
            final AlertDialog alert = new AlertDialog.Builder(mContext).create();
            alert.setTitle(mContext.getResources().getString(R.string.server_failed_title));
            alert.setMessage(mContext.getResources().getString(R.string.server_failed_msg)+""+error);
            alert.setCancelable(false);

            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alert.dismiss();
                }
            });

            alert.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void  serverFailuerError(Context mContext, final okCancel intere
    )
    {
        try {
            View vw = View.inflate(mContext, R.layout.no_server_dialog,null);

            final AlertDialog alert = new AlertDialog.Builder(mContext).create();
            Button btCancel= (Button)vw.findViewById(R.id.btn_cancel);
            Button btUpdate= (Button)vw.findViewById(R.id.btn_update);
            alert.setView(vw);

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                    System.exit(0);
                }
            });

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    alert.dismiss();
                    intere.okButton();
                 }
            });
            alert.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dataNotAvailable(final okCancel intere)
    {
        View vw = View.inflate(mContext, R.layout.data_blank_dialog,null);

        final AlertDialog alert = new AlertDialog.Builder(mContext).create();

//        Button btCancel= (Button)vw.findViewById(R.id.btn_cancel);
        Button btRetry= (Button)vw.findViewById(R.id.btn_retry);

//        alert.setTitle("Login");
//        alert.setMessage("Login Successfully");
//        alert.setCancelable(false);
        alert.setView(vw);

       /* btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
                System.exit(0);
            }
        });*/

        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alert.dismiss();
                intere.okButton();
            }
        });

        alert.show();
    }



    public static void warningDialog(Context mContext, String str)
    {
        try {
            Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            v.setTextColor(mContext.getResources().getColor(R.color.red_100));
            toast.setGravity(Gravity.CENTER,0,0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
