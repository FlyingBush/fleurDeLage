package fr.fleurdelage.fleurdelage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.Locale;

import fr.fleurdelage.fleurdelage.MainActivity;

public class EmergencyButton extends AppCompatActivity {
    //Initialize variable
    private static final int REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_button);

        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EmergencyButton.this);
    }

    // Functions to send a message with location

    private void sendMessage() {
        System.out.println(fusedLocationProviderClient);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmergencyButton.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
        CancellationTokenSource cts = new CancellationTokenSource();
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cts.getToken())
                .addOnCompleteListener(this, complete->{
                    System.out.println(complete.isComplete());
                })
                .addOnFailureListener(this, failure->{
                    System.out.println(failure.getMessage());
                })
                .addOnSuccessListener(this, loc -> {
            if (loc != null) {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                System.out.printf(Locale.US, "%s -- %s%n", latitude, longitude);

                // Format the associated uri
                Uri uriGeo = Uri.parse("geo: " + loc.getLatitude() + "," + loc.getLongitude());
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+33666003183", null, "SEND HELP TO\n" + uriGeo.toString(), null, null);
            } else {
                System.out.println("Cannot find location");
            }
        });
    }

    // Functions to make an emergency call
    //add 112 as default and an option to choose your emergency contact

    private void makeCall() {
        String number = "+33666003183";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void EmergencyBtn_onClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EmergencyButton.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
        } else {
            makeCall();
            sendMessage();
        }
    }
}