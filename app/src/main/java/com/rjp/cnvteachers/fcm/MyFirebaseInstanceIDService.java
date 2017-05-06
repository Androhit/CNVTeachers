package com.rjp.cnvteachers.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rjp.cnvteachers.R;
import com.rjp.cnvteachers.api.API;
import com.rjp.cnvteachers.api.RetrofitClient;
import com.rjp.cnvteachers.beans.ApiResults;
import com.rjp.cnvteachers.utils.AppPreferences;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shraddha on 4/29/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private API retrofitApi;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token)
    {
        if(AppPreferences.getLoginObj(MyFirebaseInstanceIDService.this).getEmpid() != null) {
            RetrofitClient.initRetrofitClient();
            retrofitApi = RetrofitClient.getRetrofitClient();

            retrofitApi.register_fcm_token(AppPreferences.getInstObj(MyFirebaseInstanceIDService.this).getCode(),AppPreferences.getLoginObj(MyFirebaseInstanceIDService.this).getEmpid(),0,token, getApplicationContext().getResources().getString(R.string.app_name), new Callback<ApiResults>() {
                @Override
                public void success(ApiResults apiResults, Response response) {
                    if(apiResults.getResult().equalsIgnoreCase("false"))
                    {
                        Log.e("FCM ","Token Reg failed");
                        AppPreferences.setFcmKey(MyFirebaseInstanceIDService.this,null);
                    }
                    else
                    {
                        AppPreferences.setFcmKey(MyFirebaseInstanceIDService.this,token);
                        Log.e("FCM ",""+apiResults.getResult());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    Log.e("FCM ","Token Reg failed "+error);
                }
            });
        }
    }
}

