package com.example.prototype_dresponder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Nottification.NotificationUtils;
import com.example.prototype_dresponder.Utils.AndroidUtils;

public class LoadingActivity extends AppCompatActivity {
    private static String Email,Pass,usertype;
    private boolean isLoggedIn;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        context = LoadingActivity.this;
        LoadUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isLoggedIn){
                    if(usertype.equals(SharedConstants.USERTYPE_CITIZEN)){
                        AndroidUtils.showToast(context,"welcome "+ Email);
                        /** Citizen Intent here **/
                        NotificationUtils.getToken(LoadingActivity.this);
                        Intent citizen = new Intent(LoadingActivity.this,CitizenDashboard.class);
                        startActivity(citizen);
                        finish();
                    }else if(usertype.equals(SharedConstants.USERTYPE_RESPONDER)){
                    AndroidUtils.showToast(context,"welcome "+ Email);
                    /** Responder Intent here **/
//                                Intent responder = new Intent(LoadingActivity.this, respner class);
//                                startActivity(responder);
//                                finish();
                    }
                }else{
                    AndroidUtils.showToast(LoadingActivity.this,"no acc save plase Login again!");
                    Intent Main = new Intent(LoadingActivity.this, Login.class);
                    startActivity(Main);
                    finish();
 }

                }
            }, 2000);

    }

    public void LoadUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(SharedConstants.KEY_SHAREDPREF_NAME, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(SharedConstants.KEY_ISLOGIN_NAME, false);
        Email = sharedPreferences.getString(SharedConstants.KEY_ACC_EMAIL,"");
        Pass = sharedPreferences.getString(SharedConstants.KEY_ACC_PASS,"");
        usertype = sharedPreferences.getString(SharedConstants.KEY_USERTYPE,"");


    }

}