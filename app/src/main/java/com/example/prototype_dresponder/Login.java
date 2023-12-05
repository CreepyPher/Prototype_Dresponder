package com.example.prototype_dresponder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Nottification.NotificationUtils;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    TextView CitizenSignup,ResponderSignup;
    EditText email,password;

    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUtility.getFirestore();

        email = (EditText) findViewById(R.id.editxtEmail);
        password = (EditText) findViewById(R.id.editxtPass);

        CitizenSignup = (TextView) findViewById(R.id.txtSCitizen);
        CitizenSignup.setOnClickListener(v -> {
            Intent Citizen = new Intent(getApplicationContext(),CitizenSignUpActivity.class);
            startActivity(Citizen);
        });

        ResponderSignup = (TextView) findViewById(R.id.txtSResponder);
        ResponderSignup.setOnClickListener(v -> {showDialog();});

        Login = findViewById(R.id.btnLogin);
        Login.setOnClickListener(v -> {

            String LEmail = email.getText().toString();
            String LPass = password.getText().toString();

            AndroidUtils.ProgressDialogMsg(Login.this,"Loging in Please Wait!");
            if(!validation(LEmail,LPass)){
                return;
            }else {
                LoginAcc(LEmail,LPass);
            }

        });

    }
    private boolean validation(String Email, String pass){
        if (Email.isEmpty()) {
            email.setError("Please fill the Email");
            AndroidUtils.closeDialog(Login.this);
            return false;
        }
        if(pass.isEmpty()){
            password.setError("Please fill the password");
            AndroidUtils.closeDialog(Login.this);
            return false;
        }
        return true;
    }

    public void LoginAcc(String Email , String Pass){
        FirebaseUtility.getCitizenProfilesCollection()
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String emailDoc = doc.getString("email");
                        String passDoc = doc.getString("password");
                        String Fullname = doc.getString("fullname");

                        if(emailDoc.equals(Email) && passDoc.equals(Pass)){
                            SharedPreferences.Editor s = SharedConstants.getPref(getApplicationContext()).edit();
                            s.putBoolean(SharedConstants.KEY_ISLOGIN_NAME, true);
                            s.putString(SharedConstants.KEY_USERTYPE, SharedConstants.USERTYPE_CITIZEN);
                            s.putString(SharedConstants.KEY_ACC_EMAIL,emailDoc);
                            s.putString(SharedConstants.KEY_ACC_ID,doc.getId());
                            s.putString(SharedConstants.KEY_ACC_FULLNAME,Fullname);
                            s.commit();

                            LoginAuth(emailDoc,passDoc);
                            Intent CitizenDash = new Intent(Login.this,CitizenDashboard.class);
                            startActivity(CitizenDash);
                            finish();
                            break;
                        }else if(Email.equals("admin141") && Pass.equals("admin123")){
                            Intent Admin = new Intent(Login.this,AdminDash.class);
                            startActivity(Admin);
                            finish();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(getApplicationContext(),"Something wrong when Login in,\n Please try Again Later");
                    }
                });
    }

    private void LoginAuth(String email, String password) {

        FirebaseUtility.getAuth().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"login Successful!!",Toast.LENGTH_LONG).show();
                            NotificationUtils.getToken(Login.this);
                            AndroidUtils.closeDialog(Login.this);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Somethings Wrong on Authenticate",Toast.LENGTH_LONG).show();
                        AndroidUtils.closeDialog(Login.this);
                    }
                });
    }

    public void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("We need to verify the responders credibility," +
                "so it may \ntake time to be Approve ");
        builder.setTitle("Notice!");
        builder.setCancelable(false);
        builder.setPositiveButton("Continue", (DialogInterface.OnClickListener) (dialog, which) -> {
            /** intent forresponder signout **/
            Intent signupRes = new Intent(Login.this,ResponderSignUp.class);
            startActivity(signupRes);
            dialog.cancel();
        });
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}