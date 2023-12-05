package com.example.prototype_dresponder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.prototype_dresponder.Adapters.AdminDashAdapter;
import com.example.prototype_dresponder.Modals.AdminDashList;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AdminDash extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AdminDashList> list;
    AdminDashAdapter AdminAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);


        recyclerView = findViewById(R.id.AdmiRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        AdminAdapter = new AdminDashAdapter(this,list);
        recyclerView.setAdapter(AdminAdapter);

        getmainList();
    }
    private void getmainList(){
        FirebaseUtility.getResSignupCollection()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        if(task.getResult().isEmpty()){
                           AndroidUtils.showToast(AdminDash.this,"No Sign up Request!");
                        }
                        for(DocumentSnapshot snapshot : task.getResult()){
                            AdminDashList Adminlist = new AdminDashList
                                    (snapshot.getString("Fullname"),
                                    snapshot.getString(FirebaseUtility.ResSignup_ImgKEY),
                                    snapshot.getId());
                            list.add(Adminlist);
                        }
                        AdminAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AndroidUtils.showToast(AdminDash.this,"Failed to get Data!");
                    }
                });
    }

}