package com.example.prototype_dresponder.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Nottification.NotificationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseQueries {

    public static void NotifyResponder(Context context,String MSG){
        FirebaseUtility.getFCMTokenCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot: task.getResult()){
                            String yourtoken = SharedConstants.getString(context,SharedConstants.YOUR_TOKEN_KEYNAME);
                            if(!yourtoken.equals(snapshot.getString("token"))){
                                String token = snapshot.getString("token");
                                NotificationUtils.sendMsgNotification(context,token,yourtoken,MSG);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(context,"Something wrong, please try again.");
                    }
                });
    }
    /** Admin query **/
     public static void AdminRegisterList(Context context,String fname,String email,String password){

         Map<String,Object> SignUpReq = new HashMap<>();
         SignUpReq.put("Fullname",fname);
         SignUpReq.put("Email",email);
         SignUpReq.put("Password",password);

        FirebaseUtility.getResponderPorfileCollection()
                .add(SignUpReq)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            AndroidUtils.showToast(context,"Registered Success");
                        }else{
                            AndroidUtils.showToast(context,"Something Wrong on save the data please try again later!");
                        }
                    }
                });
     }

}
