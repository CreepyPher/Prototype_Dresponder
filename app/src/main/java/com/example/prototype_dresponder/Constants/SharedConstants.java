package com.example.prototype_dresponder.Constants;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class SharedConstants {
    /** Acc Keys **/
    public static final String KEY_SHAREDPREF_NAME =  "DresponderPref";
    public static final String KEY_ISLOGIN_NAME = "IsLogin";
    public static final String KEY_ACC_ID = "Acc_id";
    public static final String KEY_ACC_EMAIL = "Acc_email";
    public static final String KEY_ACC_FULLNAME = "Fullname";
    public static final String KEY_ACC_PASS = "Acc_Pass";
    public static final String KEY_USERTYPE = "Acc_USERTYPE";
    public static final String USERTYPE_CITIZEN = "Citizen";
    public static final String USERTYPE_RESPONDER = "Responder";
    /**Notificatio**/

    public static final String YOUR_TOKEN_KEYNAME = "yourTOKEN";
    public static final String KEY_MESSAGE = "message";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    /** Data keys **/
    public static final String USER_LATLNG_NAME = "OWN_LATLNG";
    public static final String USER_LATITUDE_NAME = "OWN_lattude";
    public static final String USER_LONGTITUDE_NAME = "OWN_longtitude";
    public static final String Fire_Assitance_KEYNAME = "FireAssistanceReport";
    public static final String EarthQuake_Assistance_KEYNAME = "EarthQuakeAssistanceReport";

    /** responder data bundle **/
    public static final String Bundle_Fullname_KEY = "Fullname";
    public static final String Bundle_Email_Key = "Email";
    public static final String Bundle_Password_KEY = "Password";


    public static SharedPreferences getPref(Context context){
        return context.getSharedPreferences(KEY_SHAREDPREF_NAME,Context.MODE_PRIVATE);
    }
    public static void putString(Context context,String key,String value){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getString(Context context,String key){
        return getPref(context).getString(key,null);
    }

    public static void putBoolean(Context context,String key, Boolean value){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context,String key){
        return getPref(context).getBoolean(key,false);
    }
    public static void put_OWN_Latlng(Context context,LatLng latLng){
        SharedPreferences.Editor editor = getPref(context).edit();
        float lat = (float) latLng.latitude;
        float lng = (float) latLng.longitude;
        editor.putFloat(USER_LATITUDE_NAME,lat);
        editor.putFloat(USER_LONGTITUDE_NAME,lng);
        editor.apply();
    }
    public static LatLng get_OWN_latlng(Context context){
        float lat = getPref(context).getFloat(USER_LATITUDE_NAME,0);
        float lng = getPref(context).getFloat(USER_LONGTITUDE_NAME,0);
        if(lat != 0 && lng != 0){
            AndroidUtils.showToast(context,"stored Loc with value");
            return new LatLng(lat, lng);
        }
        return new LatLng(0,0);
    }

    public static void SharedClear(Context context){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.clear();
        editor.apply();
    }


    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String,String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAgCXH53Y:APA91bHsy2uZkNYuwpFC4ZXDCWr3M7woSKGHHlyrSfC7fczQkuWvmBoZOnSfq3Khy0W7k2j4iENAwRhLv-ay7NbwV3duvdEkGG-37vILFcxAp162nySYCfqOh8JPtVzpSwbAfnRJv-gw"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }

        return remoteMsgHeaders;
    }





}
