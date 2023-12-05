package com.example.prototype_dresponder.Nottification;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Network.ApiClient;
import com.example.prototype_dresponder.Network.ApiService;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtils {

    public static void getToken(Context context){
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            return;
                        }
                        String token = task.getResult();
                        SharedConstants.putString(context,SharedConstants.YOUR_TOKEN_KEYNAME,token);
                        String usertype = SharedConstants.getString(context,SharedConstants.KEY_USERTYPE);
                        SaveFCMtoken(context,token,usertype);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(context,"Failed to get your token");
                    }
                });
    }

    public static void SaveFCMtoken(Context context,String Token,String usertype){

        String Accid = SharedConstants.getString(context,SharedConstants.KEY_ACC_ID);

        Map<String, Object> FCMtoken = new HashMap<>();
        FCMtoken.put("AccID",SharedConstants.getString(context,SharedConstants.KEY_ACC_ID));
        FCMtoken.put("Email", SharedConstants.getString(context, SharedConstants.KEY_ACC_EMAIL));
        FCMtoken.put("UserType",usertype);
        FCMtoken.put("token", Token);

        FirebaseUtility.getFCMTokenCollection().document(Accid)
                .set(FCMtoken)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AndroidUtils.showToast(context,"Save Token");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(context,"Failed to get Token");
                    }
                });
    }
    /** Send notification **/
    /**
     *
     * @param context
     * @param RecieverToken
     * @param TOKEN
     * @param Message
     * Loop with the firebase collection if you want to send to many users
     */
    public static void sendMsgNotification(Context context,String RecieverToken,String TOKEN,String Message) {

        try {

            JSONArray tokens = new JSONArray();
            tokens.put(RecieverToken);

            JSONObject data = new JSONObject();
            data.put(SharedConstants.KEY_FCM_TOKEN,TOKEN);
            data.put("name","Trail_Msg");
            data.put(SharedConstants.KEY_MESSAGE,Message);

            JSONObject body = new JSONObject();
            body.put(SharedConstants.REMOTE_MSG_DATA,data);
            body.put(SharedConstants.REMOTE_MSG_REGISTRATION_IDS,tokens);

            sendNotification(context,body.toString());

        } catch (Exception ex) {
            AndroidUtils.showToast(context,ex.getMessage());
        }

    }

    private static void sendNotification(Context context,String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                SharedConstants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    try {
                        if(response.body() != null){
                            JSONObject responseJSON =  new JSONObject(response.body());
                            JSONArray results = responseJSON.getJSONArray("results");
                            if (responseJSON.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
                                //   showToast(error.getString("error"));
                                return;
                            }
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification send successfully!");
                }
                else{
                    AndroidUtils.showToast(context,"ERROR: "+response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                AndroidUtils.showToast(context,t.getMessage());
            }
        });
    }

}
