package com.example.prototype_dresponder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResponderSignupP2 extends AppCompatActivity {
    Context context;
    private ImageButton Back;
    private ImageView ProffImg;
    private Button Submit,Select;
    Bitmap bitmap;

    private String Fname,Email,Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_signup_p2);
        context = ResponderSignupP2.this;

        //Intent bundle
        Bundle ResData = getIntent().getExtras();
        if(ResData.isEmpty()){
            AndroidUtils.showToast(context,"the bundle is empty");
        }else{
            Fname = ResData.getString(SharedConstants.Bundle_Fullname_KEY,"");
            Email = ResData.getString(SharedConstants.Bundle_Email_Key,"");
            Pass = ResData.getString(SharedConstants.Bundle_Password_KEY,"");
        }

        ProffImg = (ImageView) findViewById(R.id.imageView);

        Back = (ImageButton) findViewById(R.id.BTNimgback);
        Back.setOnClickListener(v -> {
            Intent back = new Intent(ResponderSignupP2.this,ResponderSignUp.class);
            startActivity(back);
        });
        Select = findViewById(R.id.btnselectIMG);
        Select.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 10);

        });
        Submit = findViewById(R.id.btnSubmit);
        Submit.setOnClickListener(SubmitBTN);

    }
    View.OnClickListener SubmitBTN = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uniqueID = UUID.randomUUID().toString();
            ProgressDialog progressDialog = AndroidUtils.progressDialog(context);
            progressDialog.setMessage("Submiting Data please wait...");
            progressDialog.show();


            Drawable drawable = ProffImg.getDrawable();
            Bitmap bitmap1 = ((BitmapDrawable) drawable).getBitmap();

            ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG,100,bytearray);
            byte[] data = bytearray.toByteArray();

            SubmitProffDocs(uniqueID,data,progressDialog);

        }
    };
    private void SubmitProffDocs(String Uid,byte[] Data,ProgressDialog progressDialog){
        FirebaseUtility.getResponderSignupDocs(this.Email,Uid)
                .putBytes(Data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        SubmitRegistration(Email,Fname,Pass,Uid,progressDialog);
                        AndroidUtils.showToast(context,"Img Proff submited!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(context,"Failed to submit Img");
                        progressDialog.dismiss();
                    }
                });
    }

    private void SubmitRegistration(String email,String fullname,String password,String uid,ProgressDialog progressDialog){

        Map<String,Object> SignUpReq = new HashMap<>();
        SignUpReq.put("Fullname",fullname);
        SignUpReq.put("Email",email);
        SignUpReq.put("Password",password);
        SignUpReq.put(FirebaseUtility.ResSignup_ImgKEY,FirebaseUtility.ImgRESsignupRefNAME(email,uid));

        FirebaseUtility.getResSignupCollection()
                .add(SignUpReq)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            AndroidUtils.showToast(context,"Registration Submited!");
                            Intent Done = new Intent(context, Login.class);
                            startActivity(Done);
                            progressDialog.dismiss();
                        }else{
                            AndroidUtils.showToast(context,"Registration Failed!");
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 10) {
            if(data != null) {
                Uri uri = data.getData();
                try {
                    bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ProffImg.setImageBitmap(bitmap);

                } catch (Exception e) {
                    AndroidUtils.showToast(context, "" + e);
                }
            }
        }super.onActivityResult(requestCode, resultCode, data);

    }

}