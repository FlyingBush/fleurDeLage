package com.example.emergencybutton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;


    //Initialize variable
    FusedLocationProviderClient fusedLocationProviderClient;


    @SuppressLint("MissingPermission")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


    }


    // Functions to send a message with location

    @SuppressLint("MissingPermission")
    private void sendMessage() {
        AtomicReference<Double> wayLatitude = new AtomicReference<>(0.0);
        AtomicReference<Double> wayLongitude = new AtomicReference<>(0.0);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, loc -> {
            if (loc != null) {
                wayLatitude.set(loc.getLatitude());
                wayLongitude.set(loc.getLongitude());
                System.out.println(String.format(Locale.US, "%s -- %s", wayLatitude.get(), wayLongitude.get()));

                // Format the associated uri
                Uri uriGeo = Uri.parse("geo:" + loc.getLatitude() + " , " + loc.getLongitude());
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("123454568678", null, uriGeo.toString(), null, null);
            } else {
                System.out.println("Cannot find location");
            }
        });
    }


    // Functions to make an emergency call
    //add 112 as default and an option to choose your emergency contact

    private void makeCall() {
        String number = "123454568678";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void EmergencyBtn_onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            makeCall();
            sendMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
                sendMessage();
            } else {
                //When permission is denied
                //Display toast
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}