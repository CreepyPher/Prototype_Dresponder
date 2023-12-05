package com.example.prototype_dresponder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button Signup,Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signup = findViewById(R.id.btnSignup);
        Signup.setOnClickListener(v -> {

        });
        Login = findViewById(R.id.btnLogin);




    }
}