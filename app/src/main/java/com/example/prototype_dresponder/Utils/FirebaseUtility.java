package com.example.prototype_dresponder.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtility {

    public static final String refChildResponderSignup = "ResponderSignUpDocs";
    public static final String ResSignup_ImgKEY = "ResSignupImgRef";

    public static FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }
    public static FirebaseFirestore getFirestore(){
        return FirebaseFirestore.getInstance();
    }

    public static CollectionReference getCitizenProfilesCollection(){
        return FirebaseFirestore.getInstance().collection("CitizenAcc");
    }
    public static CollectionReference getReportCollection(){
        return getFirestore().collection("AssistanceReport");
    }
    public static CollectionReference getFCMTokenCollection(){
        return getFirestore().collection("FCMtokens");
    }
    public static StorageReference getResponderSignupDocs(String Email,String rid){
        return FirebaseStorage.getInstance().getReference().child(refChildResponderSignup+"/"+rid+Email+"Ref");
    }
    public static String ImgRESsignupRefNAME(String Email,String rid){
        return rid+Email+"Ref";
    }
    public static CollectionReference getResSignupCollection(){
        return getFirestore().collection("ResponderSignUp");
    }
    public static StorageReference getResSignupDocs(String ImgID){
        return FirebaseStorage.getInstance().getReference().child(refChildResponderSignup+"/"+ImgID);
    }
    public static CollectionReference getResponderPorfileCollection(){
        return getFirestore().collection("RespondersAcc");
    }

}
