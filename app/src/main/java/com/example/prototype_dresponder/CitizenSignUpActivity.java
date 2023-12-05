package com.example.prototype_dresponder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CitizenSignUpActivity extends AppCompatActivity {
    private EditText etFullname, etEmail, etPassword, etCpassword,etAddress;
    private EditText Month,Day,Year,Age;
    private boolean isPassShowing = true;
    private boolean isCPassShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizensignup);

        showpass();

        etFullname = findViewById(R.id.et_firstname_signup);
        etEmail = findViewById(R.id.et_email_signup);
        etPassword = findViewById(R.id.et_password_signup);
        etCpassword = findViewById(R.id.et_cpassword_signup);
        etAddress = (EditText) findViewById(R.id.txtuserLocation);
        Age = findViewById(R.id.edittxtAge);
        Month = findViewById(R.id.edittxtMonth);
        Day = findViewById(R.id.edittxtDay);
        Year = findViewById(R.id.edittxtYear);

        Button btnSignup = (Button) findViewById(R.id.btn_signup_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AndroidUtils.ProgressDialogMsg(CitizenSignUpActivity.this,"Saving Data, Please wait!");

                String fname = etFullname.getText().toString();
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                String cpass = etCpassword.getText().toString();
                String address = etAddress.getText().toString();
                int age = Integer.parseInt(Age.getText().toString());
                int mm = Integer.parseInt(Month.getText().toString());
                int dd = Integer.parseInt(Day.getText().toString());
                int yy = Integer.parseInt(Year.getText().toString());

                Boolean dateValid = validationDATE(dd,mm,yy);
                if(!dateValid) {
                return;
                }else {
                    String bday = mm + "/" + dd + "/" + yy;
                    //int age = getAge(bday);
                    boolean isValid = validateData(fname, email, pass, cpass, address, age);
                    if (!isValid) {
                        return;
                    } else {
                        createAccountInFirebase(fname, email, pass, address, bday, age);
                    }
                }
            }
        });

    }
    public void showpass(){
        ImageButton ibShowPass = (ImageButton) findViewById(R.id.ib_showpass_signup);
        ibShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassText(ibShowPass);
            }
        });

        ImageButton ibShowCPass = (ImageButton) findViewById(R.id.ib_showcpass_signup);
        ibShowCPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCPassText(ibShowCPass);
            }
        });
    }
    private void showPassText(ImageButton ib) {

        if (isPassShowing) {
            ib.setColorFilter(getResources().getColor(R.color.black));
            etPassword.setTransformationMethod(null);
            isPassShowing = false;
        } else {
            ib.setColorFilter(getResources().getColor(R.color.white));
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
            isPassShowing = true;
        }
    }
    private void showCPassText(ImageButton ib) {

        if (isCPassShowing) {
            ib.setColorFilter(getResources().getColor(R.color.black));
            etCpassword.setTransformationMethod(null);
            isCPassShowing = false;
        } else {
            ib.setColorFilter(getResources().getColor(R.color.white));
            etCpassword.setTransformationMethod(new PasswordTransformationMethod());
            isCPassShowing = true;
        }
    }
    private boolean validationDATE(int dd,int MM,int YY) {
        if(dd > 31 || dd <= 0) {
            Day.setError("The day is not Valid");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if(MM > 12 || MM <= 0) {

            Month.setError("The Month id not Valid");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if(YY <= 0) {
            Year.setError("The year is not Valid");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        return true;
    }

    private boolean validateData(String fullname, String email, String password, String cpassword,String Address,int Age) {
        //validate the data that are input by user.
        if (fullname.isEmpty()) {
            etFullname.setError("Name is Required");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is Required");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email format is invalid");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is Required");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if (!password.equals(cpassword)) {
            etCpassword.setError("Confirm password must be the same as password.");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        if (Address.isEmpty()) {
            etAddress.setError("Address is required");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }

        if(Age <= 0){
            AndroidUtils.showToast(CitizenSignUpActivity.this,"Please set the Birth Day properly");
            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
            return false;
        }
        return true;
    }
    private void createAccountInFirebase(String fullname, String email, String password,String Address,String dbay,int Age) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //creating acc is done
                            /*Toast.makeText(Registration.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();*/
                        /*Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);*/

                            if (firebaseAuth != null) {
                                String userId = firebaseAuth.getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                Map<String, Object> user = new HashMap<>();
                                user.put("fullname", fullname);
                                user.put("date_of_birth",dbay);
                                user.put("age",Age);
                                user.put("email", email);
                                user.put("password",password);
                                user.put("address",Address);
                                user.put("userID", firebaseAuth.getCurrentUser().getUid());

                                FirebaseUtility.getCitizenProfilesCollection()
                                        .document(firebaseAuth.getCurrentUser()
                                        .getUid())
                                        .set(user)
                                        .addOnCompleteListener(t -> {
                                    {
                                        if (t.isSuccessful()) {
                                            Toast.makeText(CitizenSignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(CitizenSignUpActivity.this, CitizenDashboard.class);

                                                SharedPreferences.Editor s = SharedConstants.getPref(getApplicationContext()).edit();
                                                s.putString(SharedConstants.KEY_USERTYPE,SharedConstants.USERTYPE_CITIZEN);
                                                s.putString(SharedConstants.KEY_ACC_FULLNAME,fullname);
                                                s.putString(SharedConstants.KEY_ACC_ID,firebaseAuth.getCurrentUser().getUid());
                                                s.putString(SharedConstants.KEY_ACC_EMAIL,email);
                                                s.putString(SharedConstants.KEY_ACC_ID,firebaseAuth.getCurrentUser().getUid());
                                                s.apply();

                                            startActivity(intent);
                                            finish();
                                            
                                        } else {
                                            AndroidUtils.showToast(getApplicationContext(), t.getException().getMessage());
                                        }
                                    }
                                });
                            }
                        } else {
                            AndroidUtils.showToast(CitizenSignUpActivity.this,"Registration Failed");
                            AndroidUtils.closeDialog(CitizenSignUpActivity.this);
                        }
                    }
                });

    }
    public int getAge(String dobString) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }


}