package com.example.emergencybutton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;


    //Initialize variable
    /*
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;
*/

    @SuppressLint("MissingPermission")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


/*
        //Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


        //Assign variables for location
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);

   */

    }



    // Functions to get location

    public void getFromLocation(double latitude,
                                double longitude,
                                int maxResults) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize GeoCoder
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        //Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //Set Latitude on TextView

                        textView1.setText(String.valueOf(addresses.get(0).getLatitude()));
                        //textView1.setText("Latitude:" + location.getLatitude());
                        //Set Longitude

                        textView2.setText(String.valueOf(addresses.get(0).getLongitude()));
                        //textView2.setText("Longitude:" + location.getLongitude());

                        //Set country name
                        textView3.setText(addresses.get(0).getCountryName());
                        //Set Locality
                        textView4.setText(addresses.get(0).getLocality());
                        //Set Address
                        textView5.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }



    public void LocalisationBtn_onClick(View view) {
        //Check permission
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission granted
            getLocation();
        }
        else {
            //Permission is denied
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

*/

// Retrieve latitude and longitude

    // Functions to send a message
    private void sendMessage() {
        Double longitude = null;
        Double latitude = null;
        getFromLocation(latitude, longitude, 1);
        Uri uriGeo = Uri.parse("geo:" + latitude + "," + longitude);
        final Intent intentGeo = new Intent(Intent.ACTION_VIEW, uriGeo);
        startActivity(intentGeo);
        /*
        //Object sms_body = getFromLocation();
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts("sms", "123454568678", null));
        Intent.putExtra("sms_body", "baba");
        startActivity(intent);
        */
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
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
            }
            else {
                //When permission is denied
                //Display toast
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GeocodeListener {
    }
}