package com.example.prototype_dresponder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Utils.AndroidUtils;

public class ResponderSignUp extends AppCompatActivity {
    Context context;
    private Button next;
    private ImageView back;
    private EditText Fullname,Email,Password,conpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_sign_up);
    context = ResponderSignUp.this;

    Fullname = (EditText) findViewById(R.id.editxtRfullname);
    Email = (EditText) findViewById(R.id.editxtRemail);
    Password = (EditText) findViewById(R.id.editxtRPass);
    conpass = (EditText) findViewById(R.id.editxtRconPass);

    back = (ImageButton) findViewById(R.id.BTN1imgback);
    back.setOnClickListener(v -> {
        Intent back = new Intent(ResponderSignUp.this,Login.class);
        startActivity(back);
    });

    next = findViewById(R.id.btnRNext);
    next.setOnClickListener(btnNext);

    }
    View.OnClickListener btnNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ProgressDialog progressDialog = AndroidUtils.progressDialog(context);
            progressDialog.setMessage("Loading plaese wait...");
            progressDialog.show();

            String fname = Fullname.getText().toString();
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            String cpass = conpass.getText().toString();

            boolean isvalid = validateData(fname, email, password, cpass);
            if (!isvalid) {
                return;
            }else{
                Intent next = new Intent(context, ResponderSignupP2.class);
                Bundle ResData = new Bundle();
                ResData.clear();
                ResData.putString(SharedConstants.Bundle_Fullname_KEY,fname+"");
                ResData.putString(SharedConstants.Bundle_Email_Key,email+"");
                ResData.putString(SharedConstants.Bundle_Password_KEY,cpass+"");

                next.putExtras(ResData);
                progressDialog.dismiss();
                startActivity(next);
            }
        }
    };
    private boolean validateData(String fullname, String email, String password,String Conpass) {
        //validate the data that are input by user.
        if (fullname.isEmpty()) {
            Fullname.setError("Name is Required");
            AndroidUtils.closeDialog(ResponderSignUp.this);
            return false;
        }
        if (password.isEmpty()) {
            Password.setError("Password is Required");
            AndroidUtils.closeDialog(ResponderSignUp.this);
            return false;
        }
        if (Conpass.isEmpty()) {
            conpass.setError("Please conform Password");
            AndroidUtils.closeDialog(ResponderSignUp.this);
            return false;
        }
        if(!password.equals(Conpass)){
            conpass.setError("Password and re-type password doesn't match");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Email format is invalid");
            AndroidUtils.closeDialog(ResponderSignUp.this);
            return false;
        }
        return true;
    }
}