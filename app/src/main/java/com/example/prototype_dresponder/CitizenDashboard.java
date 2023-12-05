package com.example.prototype_dresponder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.prototype_dresponder.Constants.SharedConstants;
import com.example.prototype_dresponder.Utils.AndroidUtils;
import com.example.prototype_dresponder.Utils.FirebaseQueries;
import com.example.prototype_dresponder.Utils.FirebaseUtility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class CitizenDashboard extends AppCompatActivity implements LocationListener {

    /** Lcations objects **/
    private Location ulocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationProviderClient;

    /** Widgets**/
    Button FireAssistBtn,Signout;

    ProgressDialog progressDialog;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_dashboard);

        context = CitizenDashboard.this;

        // Add the permission first...
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        progressDialog = new ProgressDialog(this);

        Signout = findViewById(R.id.btnSignout);
        Signout.setOnClickListener(v -> {
            FirebaseUtility.getAuth().signOut();
            SharedConstants.SharedClear(this);
            Intent signout = new Intent(this,Login.class);
            startActivity(signout);
            finish();
        });

        FireAssistBtn = findViewById(R.id.btnFireBtn);
        FireAssistBtn.setOnClickListener(FireAssistanceBtn);

    }
    View.OnClickListener FireAssistanceBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            double lat = ulocation.getLatitude();
            double lng = ulocation.getLongitude();

            String NotifMSG = SharedConstants.getString(context,SharedConstants.KEY_ACC_FULLNAME)
                    +", has requesting an Fire Assistance";

            if (lat != 0 && lng != 0) {
                sendFireAssistanceReport(FirebaseUtility.getReportCollection(),lat,lng);

            }else{
                AndroidUtils.closeDialog(CitizenDashboard.this);
                AndroidUtils.showToast(CitizenDashboard.this,"Somethings wrong please, try again!");
            }

        }
    };

    private void sendFireAssistanceReport(CollectionReference ref,double lat,double lng){
        CollectionReference Ref = ref;
        if(lat != 0 && lng != 0) {

            progressDialog.setMessage("sending request!");
            progressDialog.show();

            Map<String, Object> request = new HashMap<>();
            request.put("Request_type",SharedConstants.Fire_Assitance_KEYNAME);
            request.put("Fullname", SharedConstants.getString(context, SharedConstants.KEY_ACC_FULLNAME));
            request.put("Latutude", lat);
            request.put("Longtitude", lng);

            Ref.add(request)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()) {
                                String Fullname = SharedConstants.getString(context,SharedConstants.KEY_ACC_FULLNAME);
                                String massage = Fullname + "is Reqeusting a FireAssistance please check for more information";

                                AndroidUtils.showToast(getApplicationContext(), "send successful");
                                FirebaseQueries.NotifyResponder(context,massage);
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AndroidUtils.showToast(getApplicationContext(),"Sending Failed");
                            progressDialog.dismiss();
                        }
                    });
        }
    }


    /**permissions **/
    private boolean isLocationPermissionGranted() {
        int fineLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void checkLocationPermission(Context context){
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CitizenDashboard.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
    }

    private void getCurLoc(Context context){
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CitizenDashboard.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },1);
            return;
        }
        //get your location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    ulocation = location;
                    AndroidUtils.showToast(CitizenDashboard.this,"Location has been set!");
                    AndroidUtils.closeDialog(context);
                }
            }
        });
    }

    private void getLocation(Context context){
        checkLocationPermission(context);
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, CitizenDashboard.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        ulocation = location;
        AndroidUtils.showToast(CitizenDashboard.this,"Location set!"+"\n"
        +"lat: "+location.getLatitude()+"\nlng: "+location.getLongitude());

        FireAssistBtn.setEnabled(true);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation(CitizenDashboard.this);

    }

}