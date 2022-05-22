package fr.fleurdelage.fleurdelage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!ForegroundRunning()){
        startForegroundService(new Intent( this, Accelerometer.class ) );}

        }
        public boolean ForegroundRunning(){
            ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE)
                 ) {
                if (Accelerometer.class.getName().equals(service.service.getClassName())) {
                return true;}
            }
            return false;
        }
}




